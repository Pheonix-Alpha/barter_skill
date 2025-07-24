"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

// Simple JWT decode helper to get payload without extra libs
function parseJwt(token) {
  try {
    return JSON.parse(atob(token.split(".")[1]));
  } catch (e) {
    return null;
  }
}

export default function DashboardPage() {
  const router = useRouter();
  const [token, setToken] = useState("");
  const [currentUserId, setCurrentUserId] = useState(null);

  const [skill, setSkill] = useState("");
  const [type, setType] = useState("offering"); // "offering" or "wanting"

  const [searchResults, setSearchResults] = useState([]);
  const [loading, setLoading] = useState(false);

  const [matches, setMatches] = useState([]);

  // Skill Exchange states
  const [exchangeRequests, setExchangeRequests] = useState([]);
  const [loadingExchange, setLoadingExchange] = useState(false);
  const [targetUserId, setTargetUserId] = useState("");
  const [skillId, setSkillId] = useState("");

  useEffect(() => {
    const localToken = localStorage.getItem("token");
    if (!localToken) {
      router.push("/login");
    } else {
      setToken(localToken);

      // Decode JWT to get user id
      const payload = parseJwt(localToken);
      if (payload && payload.id) {
        setCurrentUserId(payload.id);
      }

      fetchMatches(localToken);
      fetchExchangeRequests(localToken);
    }
  }, [router]);

  // Fetch matchmaking suggested matches
  const fetchMatches = async (token) => {
    try {
      const res = await fetch("http://localhost:8080/api/match/matches", {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        credentials: "include",
      });

      if (!res.ok) throw new Error("Failed to fetch matches");

      const data = await res.json();
      setMatches(data);
    } catch (err) {
      console.error(err);
    }
  };

  // Search users by skill
  const handleSearch = async () => {
    if (!skill.trim()) {
      setSearchResults([]);
      return;
    }

    setLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8080/api/match/${type}?skill=${skill}`,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error("Search failed");

      const data = await res.json();
      setSearchResults(data);
    } catch (err) {
      console.error("Search failed", err);
    } finally {
      setLoading(false);
    }
  };

  // Send friend request (existing)
  const sendFriendRequest = async (id) => {
    const res = await fetch(`http://localhost:8080/api/friends/request/${id}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      credentials: "include",
    });

    const text = await res.text();
    alert(text);
  };

  // Fetch Skill Exchange requests (sent and received)
  const fetchExchangeRequests = async (token) => {
    setLoadingExchange(true);
    try {
      const res = await fetch("http://localhost:8080/api/exchange/my-requests", {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        credentials: "include",
      });

      if (!res.ok) throw new Error("Failed to fetch skill exchange requests");

      const data = await res.json();
      setExchangeRequests(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoadingExchange(false);
    }
  };

  // Create new Skill Exchange request
  const createExchangeRequest = async () => {
    if (!targetUserId.trim() || !skillId.trim()) {
      alert("Please enter target user ID and skill ID");
      return;
    }

    try {
      const res = await fetch("http://localhost:8080/api/exchange/request", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        credentials: "include",
        body: JSON.stringify({
          targetUserId: parseInt(targetUserId),
          skillId: parseInt(skillId),
        }),
      });

      if (!res.ok) throw new Error("Failed to create skill exchange request");

      const data = await res.json();
      alert("Skill exchange request sent!");
      setTargetUserId("");
      setSkillId("");
      fetchExchangeRequests(token);
    } catch (err) {
      console.error(err);
      alert("Error sending request");
    }
  };

  // Respond to skill exchange request
  const respondToExchangeRequest = async (id, status) => {
    try {
      const res = await fetch(
        `http://localhost:8080/api/exchange/respond/${id}?status=${status}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`,
          },
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error("Failed to respond to request");

      const data = await res.json();
      alert(`Request ${status.toLowerCase()}`);
      fetchExchangeRequests(token);
    } catch (err) {
      console.error(err);
      alert("Error responding to request");
    }
  };

  return (
    <div className="p-6 max-w-3xl mx-auto space-y-6">
      {/* Skill Search Section */}
      <div className="bg-white shadow p-4 rounded">
        <h2 className="text-lg font-semibold mb-2">Search Users by Skill</h2>
        <div className="flex flex-col sm:flex-row sm:items-center gap-3">
          <input
            type="text"
            value={skill}
            onChange={(e) => setSkill(e.target.value)}
            placeholder="Enter skill name"
            className="flex-1 px-4 py-2 border rounded"
          />
          <select
            value={type}
            onChange={(e) => setType(e.target.value)}
            className="px-4 py-2 border rounded"
          >
            <option value="offering">Offering</option>
            <option value="wanting">Wanting</option>
          </select>
          <button
            onClick={handleSearch}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Search
          </button>
        </div>
      </div>

      {/* Matchmaking Section */}
      <div className="bg-white shadow p-4 rounded">
        <h2 className="text-lg font-semibold mb-2">Suggested Matches</h2>
        {matches.length === 0 ? (
          <p className="text-gray-500">No matches found.</p>
        ) : (
          <ul className="space-y-2">
            {matches.map((user) => (
              <li
                key={user.id}
                className="p-3 border rounded flex justify-between items-center"
              >
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 flex items-center justify-center rounded-full bg-green-600 text-white font-bold">
                    {user.username[0]?.toUpperCase()}
                  </div>
                  <div>
                    <p className="font-semibold">{user.username}</p>
                    <p className="text-sm text-gray-600">{user.email}</p>
                  </div>
                </div>

                <button
                  onClick={() => sendFriendRequest(user.id)}
                  className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                >
                  Send Request
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>

      {/* Search Results Section */}
      {searchResults.length > 0 && (
        <div className="bg-white shadow p-4 rounded">
          <h2 className="text-lg font-semibold mb-2">Search Results</h2>
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ul className="space-y-2">
              {searchResults.map((user) => (
                <li
                  key={user.id}
                  className="p-3 border rounded flex justify-between items-center"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 flex items-center justify-center rounded-full bg-blue-600 text-white font-bold">
                      {user.username[0]?.toUpperCase()}
                    </div>
                    <div>
                      <p className="font-semibold">{user.username}</p>
                      <p className="text-sm text-gray-600">{user.email}</p>
                    </div>
                  </div>

                  <button
                    onClick={() => sendFriendRequest(user.id)}
                    className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                  >
                    Send Request
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      {/* Skill Exchange Section */}
      <div className="bg-white shadow p-4 rounded">
        <h2 className="text-lg font-semibold mb-2">Skill Exchange Requests</h2>

        {/* Create Request */}
        <div className="flex flex-col sm:flex-row gap-3 mb-4">
          <input
            type="text"
            placeholder="Target User ID"
            value={targetUserId}
            onChange={(e) => setTargetUserId(e.target.value)}
            className="px-3 py-2 border rounded flex-1"
          />
          <input
            type="text"
            placeholder="Skill ID"
            value={skillId}
            onChange={(e) => setSkillId(e.target.value)}
            className="px-3 py-2 border rounded flex-1"
          />
          <button
            onClick={createExchangeRequest}
            className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700"
          >
            Send Request
          </button>
        </div>

        {/* Requests List */}
        {loadingExchange ? (
          <p>Loading...</p>
        ) : exchangeRequests.length === 0 ? (
          <p className="text-gray-500">No skill exchange requests found.</p>
        ) : (
         <ul className="space-y-2 max-h-72 overflow-auto">
  {exchangeRequests.map((req) => (
    <li
      key={req.id}
      className="p-3 border rounded flex flex-col sm:flex-row justify-between items-start sm:items-center"
    >
      <div>
        <p>
          <strong>Requester:</strong> {req.requesterUsername}
        </p>
        <p>
          <strong>Target:</strong> {req.targetUsername}
        </p>
        <p>
          <strong>Skill:</strong> {req.skillName}
        </p>
        <p>
          <strong>Status:</strong>{" "}
          <span
            className={
              req.status === "PENDING"
                ? "text-yellow-600"
                : req.status === "ACCEPTED"
                ? "text-green-600"
                : "text-red-600"
            }
          >
            {req.status}
          </span>
        </p>
      </div>

      {/* Show respond buttons only if current user is the target and status is PENDING */}
      {req.status === "PENDING" && req.targetUserId === currentUserId ? (
        <div className="space-x-2 mt-2 sm:mt-0">
          <button
            onClick={() => respondToExchangeRequest(req.id, "ACCEPTED")}
            className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
          >
            Approve
          </button>
          <button
            onClick={() => respondToExchangeRequest(req.id, "REJECTED")}
            className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
          >
            Reject
          </button>
        </div>
      ) : null}
    </li>
  ))}
</ul>

        )}
      </div>

      {/* Upcoming Tasks */}
      <div className="bg-white shadow p-4 rounded">
        <h2 className="text-lg font-semibold mb-2">Upcoming Tasks</h2>
        <ul className="text-sm text-gray-600 list-disc list-inside space-y-1">
          <li>📅 Java Interview on Thursday</li>
          <li>📘 Lesson with Mentor on Friday</li>
        </ul>
      </div>
    </div>
  );
}
