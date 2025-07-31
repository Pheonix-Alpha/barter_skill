"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function DashboardPage() {
  const [skill, setSkill] = useState("");
  const [type, setType] = useState("offering");
  const [results, setResults] = useState([]);
  const [matches, setMatches] = useState([]);
  const [disabledRequests, setDisabledRequests] = useState(new Set());
  const [friendIds, setFriendIds] = useState(new Set());

  const router = useRouter();

  const parseJwt = (token) => {
    try {
      return JSON.parse(atob(token.split(".")[1]));
    } catch (e) {
      return null;
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
      return;
    }

    const payload = parseJwt(token);
    const currentUserId = Number(payload?.userId);

    fetch("http://localhost:8080/api/friends/list", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        const ids = new Set(data.map((u) => u.id));
        setFriendIds(ids);
      })
      .catch((err) => console.error("Failed to fetch friends:", err));

    const stored = localStorage.getItem("disabledRequests");
    if (stored) {
      setDisabledRequests(new Set(JSON.parse(stored)));
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
      .then((data) => {
        setMatches(data.filter((user) => user.id !== currentUserId));
      })
      .catch((err) => console.error(err));
  }, [router]);

  const handleSearch = async () => {
    const token = localStorage.getItem("token");
    if (!skill || !token) return;

    const endpoint = `http://localhost:8080/api/match/${type}?skill=${encodeURIComponent(
      skill
    )}`;

    const payload = parseJwt(token);
    const currentUserId = Number(payload?.userId);

    try {
      const res = await fetch(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Search failed");

      const data = await res.json();
      setResults(data.filter((user) => user.id !== currentUserId));
    } catch (err) {
      console.error(err);
    }
  };

  const handleSendFriendRequest = async (receiverId) => {
    const token = localStorage.getItem("token");
    const friendKey = `${receiverId}-FRIEND-FRIEND`;

    if (disabledRequests.has(friendKey)) return;

    try {
      const res = await fetch(
        `http://localhost:8080/api/friends/request/${receiverId}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) throw new Error("Friend request failed");

      alert("Friend request sent!");

      setDisabledRequests((prev) => {
        const updated = new Set(prev).add(friendKey);
        localStorage.setItem("disabledRequests", JSON.stringify([...updated]));
        return updated;
      });
    } catch (err) {
      console.error(err);
      alert("Failed to send friend request.");
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
    <div className="p-4 sm:p-6 max-w-6xl mx-auto">
      <h1 className="text-2xl font-bold mb-6 text-center sm:text-left">Skill Matchmaking</h1>

      {/* Search */}
      <div className="flex flex-col sm:flex-row gap-3 mb-10">
        <input
          type="text"
          value={skill}
          onChange={(e) => setSkill(e.target.value)}
          placeholder="Enter a skill..."
          className="border p-2 rounded w-full sm:flex-1"
        />
        <select
          value={type}
          onChange={(e) => setType(e.target.value)}
          className="border p-2 rounded w-full sm:w-auto"
        >
          <option value="offering">Offering</option>
          <option value="wanting">Wanting</option>
        </select>
        <button
          onClick={handleSearch}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 w-full sm:w-auto"
        >
          Search
        </button>
      </div>

      {/* Suggested Matches */}
      <section className="mb-12">
        <h2 className="text-xl font-semibold mb-4">Suggested Matches</h2>
        {matches.length === 0 ? (
          <p className="text-gray-500">No matches found yet.</p>
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {matches.map((user) => (
              <UserCard
                key={user.id}
                user={user}
                disabledRequests={disabledRequests}
                onRequest={handleSendRequest}
                onFriendRequest={handleSendFriendRequest}
                friendIds={friendIds}
              />
            ))}
          </div>
        )}
      </section>

      {/* Search Results */}
      <section className="mb-20">
        <h2 className="text-xl font-semibold mb-4">Search Results</h2>
        {results.length === 0 ? (
          <p className="text-gray-500">No users found for this skill.</p>
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {results.map((user) => (
              <UserCard
                key={user.id}
                user={user}
                disabledRequests={disabledRequests}
                onRequest={handleSendRequest}
                onFriendRequest={handleSendFriendRequest}
                friendIds={friendIds}
              />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

function UserCard({ user, disabledRequests, onRequest, onFriendRequest, friendIds }) {
  return (
    <div className="relative bg-white border rounded-xl p-5 shadow-sm hover:shadow-md transition text-sm sm:text-base">
      {/* Friend Button */}
      {!friendIds?.has(user.id) &&
        (() => {
          const friendKey = `${user.id}-FRIEND-FRIEND`;
          const isDisabled = disabledRequests.has(friendKey);
          return (
            <button
              className={`absolute top-2 right-2 text-xl ${
                isDisabled ? "opacity-30 cursor-not-allowed" : "hover:opacity-80"
              }`}
              disabled={isDisabled}
              onClick={() => onFriendRequest(user.id)}
              title={isDisabled ? "Friend request sent" : "Send friend request"}
            >
              🤝
            </button>
          );
        })()}

      <h3 className="text-lg font-bold mb-2">{user.username}</h3>

      <p className="mb-2 text-gray-600">{user.email}</p>

      <div className="mb-3">
        <p className="font-medium text-gray-800 mb-1">🎯 They Offer:</p>
        <ul className="space-y-1 ml-2">
          {user.offeringSkills.map((skillName, i) => {
            const skillId = user.offeringSkillIds[i];
            const key = `${user.id}-${skillId}-OFFERED`;
            return (
              <li key={i} className="flex justify-between items-center">
                <span>{skillName}</span>
                <button
                  disabled={disabledRequests.has(key)}
                  onClick={() => onRequest(user.id, skillId, "OFFERED")}
                  className={`ml-2 px-3 py-1 rounded-full text-sm ${
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

      <div>
        <p className="font-medium text-gray-800 mb-1">💡 They Want:</p>
        <ul className="space-y-1 ml-2">
          {user.wantingSkills.map((skillName, i) => {
            const skillId = user.wantingSkillIds[i];
            const key = `${user.id}-${skillId}-WANTED`;
            return (
              <li key={i} className="flex justify-between items-center">
                <span>{skillName}</span>
                <button
                  disabled={disabledRequests.has(key)}
                  onClick={() => onRequest(user.id, skillId, "WANTED")}
                  className={`ml-2 px-3 py-1 rounded-full text-sm ${
                    disabledRequests.has(key)
                      ? "bg-gray-300 text-white cursor-not-allowed"
                      : "bg-green-600 text-white hover:bg-green-700"
                  }`}
                >
                  {disabledRequests.has(key) ? "Offered" : "Offer"}
                </button>
              </li>
            );
          })}
        </ul>
      </div>
    </div>
  );
}
