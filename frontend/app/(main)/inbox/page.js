"use client";

import { useEffect, useState } from "react";

export default function InboxPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [updatingId, setUpdatingId] = useState(null);
  const [currentUserId, setCurrentUserId] = useState(null);

  useEffect(() => {
    const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;

    if (!token) {
      setError("Authentication token not found. Please log in.");
      setLoading(false);
      return;
    }

    const payload = parseJwt(token);
    setCurrentUserId(payload?.userId);

    fetch("http://localhost:8080/api/exchange/my-requests", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || "Failed to fetch requests");
        }
        return res.json();
      })
      .then((data) => {
        setRequests(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const parseJwt = (token) => {
    try {
      return JSON.parse(atob(token.split(".")[1]));
    } catch (e) {
      return null;
    }
  };

  const respondToRequest = (id, status) => {
    if (status === "REJECTED") {
      const confirmDecline = window.confirm(
        "Are you sure you want to reject this skill exchange request?"
      );
      if (!confirmDecline) return;
    }

    setUpdatingId(id);
    const token = localStorage.getItem("token");

    if (!token) {
      alert("Authentication token not found. Please log in.");
      setUpdatingId(null);
      return;
    }

    fetch(`http://localhost:8080/api/exchange/respond/${id}?status=${status}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || "Failed to update request");
        }
        return res.json();
      })
      .then((updatedRequest) => {
        setRequests((prev) =>
          prev.map((req) => (req.id === updatedRequest.id ? updatedRequest : req))
        );
        alert(`Request ${status.toLowerCase()} successfully.`);
      })
      .catch((err) => alert(err.message))
      .finally(() => setUpdatingId(null));
  };

  if (loading)
    return (
      <div className="p-6 flex justify-center items-center">
        <div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-12 w-12"></div>
      </div>
    );

  if (error)
    return <p className="p-6 text-red-600 font-semibold">{error}</p>;

  if (requests.length === 0)
    return <p className="p-6 text-gray-600">No skill exchange requests found.</p>;

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <h1 className="text-2xl font-semibold mb-6">Inbox - Skill Exchange Requests</h1>
      <ul>
        {requests.map((req) => {
          const isTargetUser = currentUserId === req.target?.id;
          const requesterName = req.requester?.username || "System";
          const skillName = req.skill?.name || req.skillName || "Unknown Skill";

          return (
            <li
              key={req.id}
              className="mb-6 p-4 border rounded shadow-sm bg-white"
              role="region"
              aria-label={`Skill exchange request from ${requesterName}`}
            >
              <p className="mb-1">
                Request received from <strong>{requesterName}</strong> to exchange skill{" "}
                <strong>{skillName}</strong>
              </p>
              <p className="mb-2">
                Status: <em>{req.status}</em>
              </p>

              {req.status === "PENDING" && isTargetUser && (
                <div className="space-x-3">
                  <button
                    onClick={() => respondToRequest(req.id, "ACCEPTED")}
                    disabled={updatingId === req.id}
                    className={`px-4 py-2 rounded text-white ${
                      updatingId === req.id
                        ? "bg-green-300 cursor-not-allowed"
                        : "bg-green-600 hover:bg-green-700"
                    }`}
                  >
                    Accept
                  </button>
                  <button
                    onClick={() => respondToRequest(req.id, "REJECTED")}
                    disabled={updatingId === req.id}
                    className={`px-4 py-2 rounded text-white ${
                      updatingId === req.id
                        ? "bg-red-300 cursor-not-allowed"
                        : "bg-red-600 hover:bg-red-700"
                    }`}
                  >
                    Reject
                  </button>
                </div>
              )}
            </li>
          );
        })}
      </ul>

      <style jsx>{`
        .loader {
          border-top-color: #3498db;
          animation: spin 1s linear infinite;
        }
        @keyframes spin {
          to {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </div>
  );
}
