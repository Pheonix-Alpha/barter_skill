"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function DashboardPage() {
  const [skill, setSkill] = useState("");
  const [type, setType] = useState("offering");
  const [results, setResults] = useState([]);
  const [matches, setMatches] = useState([]);
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
      return;
    }

    fetch("http://localhost:8080/api/match/matches", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch matches");
        return res.json();
      })
      .then((data) => setMatches(data))
      .catch((err) => console.error(err));
  }, [router]);

  const handleSearch = async () => {
    const token = localStorage.getItem("token");
    if (!skill || !token) return;

    const endpoint = `http://localhost:8080/api/match/${type}?skill=${encodeURIComponent(
      skill
    )}`;

    try {
      const res = await fetch(endpoint, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error("Search failed");

      const data = await res.json();
      setResults(data);
    } catch (err) {
      console.error(err);
    }
  };

 const handleSendRequest = async (receiverId, offeredSkillId, type) => {
  const token = localStorage.getItem("token");

  // TODO: Replace this with a real selection from the logged-in user's own wanted skills
  const wantedSkillId = 1;

  try {
    const res = await fetch("http://localhost:8080/api/exchange/request", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        targetUserId: receiverId,
        offeredSkillId,
        wantedSkillId,
        type,
      }),
    });

    if (!res.ok) throw new Error("Request failed");

    alert("Skill exchange request sent!");
  } catch (err) {
    console.error(err);
    alert("Failed to send request.");
  }
};


  return (
    <div className="p-6 max-w-6xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Skill Matchmaking</h1>

      {/* 🔍 Search Section */}
      <div className="flex gap-2 mb-8">
        <input
          type="text"
          value={skill}
          onChange={(e) => setSkill(e.target.value)}
          placeholder="Enter a skill..."
          className="border p-2 rounded w-full"
        />
        <select
          value={type}
          onChange={(e) => setType(e.target.value)}
          className="border p-2 rounded"
        >
          <option value="offering">Offering</option>
          <option value="wanting">Wanting</option>
        </select>
        <button
          onClick={handleSearch}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Search
        </button>
      </div>

    
      {/* 🔎 Search Results */}
<div className="mb-14">
  <h2 className="text-xl font-semibold mb-3">Search Results</h2>
  {results.length === 0 ? (
    <p className="text-gray-500">No users found.</p>
  ) : (
    <ul className="space-y-6">
      {results.map((user) => (
        <li key={user.id} className="border p-4 rounded shadow-sm">
          <div className="mb-2">
            <strong className="text-lg">{user.username}</strong>
            <div className="text-sm text-gray-600">{user.bio}</div>
          </div>

          {/* Offered Skills */}
          <div className="mb-3">
            <span className="font-medium">They Offer:</span>
            <ul className="ml-4 mt-1">
              {user.offeringSkills.map((skillName, index) => (
                <li key={index} className="flex items-center justify-between mt-1">
                  <span>{skillName}</span>
                  <button
                    onClick={() =>
                      handleSendRequest(user.id, user.offeringSkillIds[index], "OFFERED")
                    }
                    className="ml-4 bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 text-sm"
                  >
                    Request
                  </button>
                </li>
              ))}
            </ul>
          </div>

          {/* Wanted Skills */}
          <div>
            <span className="font-medium">They Want:</span>
            <ul className="ml-4 mt-1">
              {user.wantingSkills.map((skillName, index) => (
                <li key={index} className="flex items-center justify-between mt-1">
                  <span>{skillName}</span>
                  <button
                    onClick={() =>
                      handleSendRequest(user.id, user.wantingSkillIds[index], "WANTED")
                    }
                    className="ml-4 bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 text-sm"
                  >
                    Offer
                  </button>
                </li>
              ))}
            </ul>
          </div>
        </li>
      ))}
    </ul>
  )}
</div>


      {/* 🤝 Suggested Matches */}
      <div>
        <h2 className="text-2xl font-semibold mb-4">Suggested Matches</h2>

        {matches.length === 0 ? (
          <p className="text-gray-500">No matches found yet.</p>
        ) : (
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {matches.map((user) => (
              <div
                key={user.id}
                className="bg-white shadow-md rounded-2xl p-5 border border-gray-200 hover:shadow-lg transition"
              >
                <div className="mb-3">
                  <h3 className="text-xl font-bold">{user.username}</h3>
                  <p className="text-sm text-gray-600">{user.bio}</p>
                </div>

                <div className="text-sm mb-3">
                  <div className="mb-1">
                    <span className="font-medium text-gray-800">They offer:</span>{" "}
                    <span className="text-gray-700">
                      {user.offeringSkills.join(", ") || "None"}
                    </span>
                  </div>
                  <div>
                    <span className="font-medium text-gray-800">They want:</span>{" "}
                    <span className="text-gray-700">
                      {user.wantingSkills.join(", ") || "None"}
                    </span>
                  </div>
                </div>

                {/* 🔁 Dropdown to send request for offered skills */}
                {user.offeringSkills.length > 0 && user.offeringSkillIds && (
                  <select
                    className="w-full border px-2 py-2 rounded-xl text-sm"
                    onChange={(e) => {
                      const selectedSkillId = e.target.value;
                      if (selectedSkillId) {
                        handleSendRequest(user.id, Number(selectedSkillId), "OFFERED");
                        e.target.selectedIndex = 0; // Reset dropdown
                      }
                    }}
                  >
                    <option value="">Request a skill...</option>
                    {user.offeringSkills.map((skillName, index) => (
                      <option key={index} value={user.offeringSkillIds[index]}>
                        {skillName}
                      </option>
                    ))}
                  </select>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
