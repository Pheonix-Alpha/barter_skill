"use client";

import { useEffect, useState } from "react";

export default function SkillExchangePanel() {
  const [token, setToken] = useState("");
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentUserId, setCurrentUserId] = useState(null);

  const [targetUserId, setTargetUserId] = useState("");
  const [skillId, setSkillId] = useState("");
  const [type, setType] = useState("REQUEST");

  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    if (!storedToken) return;

    setToken(storedToken);
    const payload = parseJwt(storedToken);
    setCurrentUserId(payload?.userId);
    fetchRequests(storedToken);
  }, []);

  const parseJwt = (token) => {
    try {
      return JSON.parse(atob(token.split(".")[1]));
    } catch (e) {
      return null;
    }
  };

  const fetchRequests = async (authToken) => {
    setLoading(true);
    try {
      const res = await fetch("http://localhost:8080/api/exchange/my-requests", {
        headers: { Authorization: `Bearer ${authToken}` },
        credentials: "include",
      });
      if (!res.ok) throw new Error("Failed to fetch requests");
      const data = await res.json();
      setRequests(data);
    } catch (err) {
      console.error(err);
      alert("Error loading requests");
    } finally {
      setLoading(false);
    }
  };

  const createRequest = async () => {
    if (!targetUserId || !skillId) {
      alert("Please provide both Target User ID and Skill ID");
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
          targetUserId: Number(targetUserId),
          skillId: Number(skillId),
          type,
        }),
      });
      if (!res.ok) throw new Error("Failed to create request");
      await res.json();
      alert("Request created successfully!");
      fetchRequests(token);
    } catch (err) {
      console.error(err);
      alert("Error creating request");
    }
  };

  const respondToRequest = async (id, status) => {
    try {
      const res = await fetch(`http://localhost:8080/api/exchange/respond/${id}?status=${status}`, {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
        credentials: "include",
      });
      if (!res.ok) throw new Error("Failed to update request");
      await res.json();
      alert(`Request ${status.toLowerCase()} successfully!`);
      fetchRequests(token);
    } catch (err) {
      console.error(err);
      alert("Error responding to request");
    }
  };

  return (
    <div className="p-4 max-w-3xl mx-auto space-y-6 bg-white shadow rounded">
      <h2 className="text-xl font-semibold">Skill Exchange Panel</h2>

      {/* New Request Form */}
      <div className="space-y-2">
        <h3 className="font-semibold">Create New Request</h3>
        <input
          type="number"
          placeholder="Target User ID"
          value={targetUserId}
          onChange={(e) => setTargetUserId(e.target.value)}
          className="border p-2 rounded w-full"
        />
        <input
          type="number"
          placeholder="Skill ID"
          value={skillId}
          onChange={(e) => setSkillId(e.target.value)}
          className="border p-2 rounded w-full"
        />
        <select
          value={type}
          onChange={(e) => setType(e.target.value)}
          className="border p-2 rounded w-full"
        >
          <option value="REQUEST">Request</option>
          <option value="OFFER">Offer</option>
        </select>
        <button
          onClick={createRequest}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Send Request
        </button>
      </div>

      {/* Requests */}
      <div>
        <h3 className="font-semibold mb-2">My Requests</h3>
        {loading ? (
          <p>Loading...</p>
        ) : requests.length === 0 ? (
          <p>No requests found.</p>
        ) : (
          <ul className="space-y-3">
            {requests.map((req) => {
              const isTarget = currentUserId === req.target?.id;
              const requesterName = req.requester?.username ?? "Unknown";
              const targetName = req.target?.username ?? "Unknown";
              const skillName = req.skill?.name ?? "Unknown";

              return (
                <li key={req.id} className="border p-3 rounded flex flex-col gap-1">
                  <p>
                    <strong>Requester:</strong> {requesterName} |{" "}
                    <strong>Target:</strong> {targetName}
                  </p>
                  <p>
                    <strong>Skill:</strong> {skillName} |{" "}
                    <strong>Type:</strong> {req.type} |{" "}
                    <strong>Status:</strong> {req.status}
                  </p>

                  {req.status === "PENDING" && isTarget && (
                    <div className="flex gap-2 mt-2">
                      <button
                        onClick={() => respondToRequest(req.id, "ACCEPTED")}
                        className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                      >
                        Approve
                      </button>
                      <button
                        onClick={() => respondToRequest(req.id, "REJECTED")}
                        className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                      >
                        Reject
                      </button>
                    </div>
                  )}
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </div>
  );
}
