"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function DashboardPage() {
  const [skill, setSkill] = useState("");
  const [type, setType] = useState("offering");
  const [results, setResults] = useState([]);
  const [matches, setMatches] = useState([]);
  const [disabledRequests, setDisabledRequests] = useState(new Set());
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
      return;
    }

    // Restore disabled requests
    const stored = localStorage.getItem("disabledRequests");
    if (stored) {
      setDisabledRequests(new Set(JSON.parse(stored)));
    }

    // Fetch suggested matches
    fetch("http://localhost:8080/api/match/matches", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch matches");
        return res.json();
      })
      .then((data) => {
        console.log("🔁 Fetched Matches:", data);
        setMatches(data);
      })
      .catch((err) => console.error(err));
  }, [router]);

  const handleSearch = async () => {
    const token = localStorage.getItem("token");
    if (!skill || !token) return;

    const endpoint = `http://localhost:8080/api/match/${type}?skill=${encodeURIComponent(skill)}`;

    try {
      const res = await fetch(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Search failed");

      const data = await res.json();
      console.log("🔍 Search Results:", data);
      setResults(data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSendRequest = async (receiverId, skillId, type) => {
    const token = localStorage.getItem("token");
    const key = `${receiverId}-${skillId}-${type}`;

    if (disabledRequests.has(key)) return;

    try {
      const res = await fetch("http://localhost:8080/api/exchange/request", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          targetUserId: receiverId,
          offeredSkillId: skillId,
          wantedSkillId: skillId,
          type,
        }),
      });

      if (!res.ok) throw new Error("Request failed");

      alert("Skill exchange request sent!");

      setDisabledRequests((prev) => {
        const updated = new Set(prev).add(key);
        localStorage.setItem("disabledRequests", JSON.stringify([...updated]));
        return updated;
      });
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

      {/* 🤝 Suggested Matches */}
      <div>
        <h2 className="text-2xl font-semibold mb-4">Suggested Matches</h2>
        {matches.length === 0 ? (
          <p className="text-gray-500">No matches found yet.</p>
        ) : (
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {matches.map((user) => (
              <UserCard
                key={user.id}
                user={user}
                disabledRequests={disabledRequests}
                onRequest={handleSendRequest}
              />
            ))}
          </div>
        )}
      </div>

      {/* 🔎 Search Results */}
      <div className="mb-14 mt-12">
        <h2 className="text-2xl font-bold mb-6 text-gray-800">Search Results</h2>

        {results.length === 0 ? (
          <p className="text-gray-500 text-center">No users found for this skill.</p>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {results.map((user) => (
              <UserCard
                key={user.id}
                user={user}
                disabledRequests={disabledRequests}
                onRequest={handleSendRequest}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

// ✅ Extracted reusable card component
function UserCard({ user, disabledRequests, onRequest }) {
  return (
    <div className="bg-white border border-gray-200 rounded-2xl p-5 shadow-sm hover:shadow-md transition">
      <h3 className="text-xl font-bold mb-2">{user.username}</h3>

      <div className="mb-3 text-sm">
        <p className="mb-1">
          <span className="font-medium text-gray-800">They offer:</span>{" "}
          <span className="text-gray-700">{user.offeringSkills.join(", ") || "None"}</span>
        </p>
        <p>
          <span className="font-medium text-gray-800">They want:</span>{" "}
          <span className="text-gray-700">{user.wantingSkills.join(", ") || "None"}</span>
        </p>
      </div>

      {/* Offering Skills (You can request these) */}
      {user.offeringSkills?.length > 0 && (
        <div className="mb-4">
          <p className="text-gray-700 font-medium mb-1">🎯 They Offer:</p>
          <ul className="space-y-1 ml-2">
            {user.offeringSkills.map((skillName, i) => {
              const skillId = user.offeringSkillIds[i];
              const key = `${user.id}-${skillId}-OFFERED`;
              return (
                <li key={i} className="flex justify-between items-center text-sm">
                  <span>{skillName}</span>
                  <button
                    disabled={disabledRequests.has(key)}
                    onClick={() => onRequest(user.id, skillId, "OFFERED")}
                    className={`ml-2 px-3 py-1 rounded-xl text-sm transition-all ${
                      disabledRequests.has(key)
                        ? "bg-gray-300 text-white cursor-not-allowed"
                        : "bg-blue-600 text-white hover:bg-blue-700"
                    }`}
                  >
                    {disabledRequests.has(key) ? "Requested" : "Request"}
                  </button>
                </li>
              );
            })}
          </ul>
        </div>
      )}

      {/* Wanting Skills (You can offer these) */}
      {user.wantingSkills?.length > 0 && (
        <div>
          <p className="text-gray-700 font-medium mb-1">💡 They Want:</p>
          <ul className="space-y-1 ml-2">
            {user.wantingSkills.map((skillName, i) => {
              const skillId = user.wantingSkillIds[i];
              const key = `${user.id}-${skillId}-WANTED`;
              return (
                <li key={i} className="flex justify-between items-center text-sm">
                  <span>{skillName}</span>
                  <button
                    disabled={disabledRequests.has(key)}
                    onClick={() => onRequest(user.id, skillId, "WANTED")}
                    className={`ml-2 px-3 py-1 rounded-xl text-sm transition-all ${
                      disabledRequests.has(key)
                        ? "bg-gray-300 text-white cursor-not-allowed"
                        : "bg-blue-600 text-white hover:bg-blue-700"
                    }`}
                  >
                    {disabledRequests.has(key) ? "Offered" : "Offer"}
                  </button>
                </li>
              );
            })}
          </ul>
        </div>
      )}
    </div>
  );
}
