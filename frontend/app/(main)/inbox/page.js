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
    setCurrentUserId(Number(payload?.userId));

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
      const confirmDecline = window.confirm("Are you sure you want to reject this skill exchange request?");
      if (!confirmDecline) return;
    }

    setUpdatingId(id);
    const token = localStorage.getItem("token");

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

  if (loading) {
    return (
      <div className="p-6 flex justify-center items-center min-h-screen">
        <div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-12 w-12"></div>
      </div>
    );
  }

  if (error) {
    return <p className="p-6 text-red-600 font-semibold">{error}</p>;
  }

  if (requests.length === 0) {
    return <p className="p-6 text-gray-600">No skill exchange requests found.</p>;
  }

  return (
    <div className="px-4 py-6 max-w-4xl mx-auto w-full">
      <h1 className="text-2xl font-bold mb-6 text-center sm:text-left">
        Inbox - Skill Exchange Requests
      </h1>
      <ul className="space-y-4">
        {[...requests].reverse().map((req) => {
          const isTargetUser = Number(currentUserId) === Number(req.target?.id);
          const requesterName = req.requester?.username || "Unknown";
          const targetName = req.target?.username || "Unknown";
          const skill = req.skill?.name || "Unknown Skill";
          const requestedSkill = req.wantedSkillName;
          const offeredSkill = req.offeredSkillName;

          return (
            <li
              key={req.id}
              className="p-4 border rounded-lg shadow-sm bg-white"
              role="region"
              aria-label={`Skill exchange request from ${requesterName}`}
            >
              <p className="text-base mb-1">
                <strong>{requesterName}</strong> wants to{" "}
                {req.type === "REQUEST" ? (
                  <>
                    learn <strong>{requestedSkill || skill}</strong> and is offering{" "}
                    <strong>{offeredSkill || "a skill"}</strong>
                  </>
                ) : (
                  <>
                    teach <strong>{offeredSkill || skill}</strong> in exchange for{" "}
                    <strong>{requestedSkill || "a skill"}</strong>
                  </>
                )}
              </p>

              <p className="text-sm text-gray-500">
                Requested on: {new Date(req.createdAt).toLocaleString()}
              </p>

              <p className="mt-2 text-gray-700">
                Status: <em className="font-medium">{req.status}</em>
              </p>

              {req.status?.toUpperCase() === "PENDING" && isTargetUser && (
                <div className="flex flex-wrap gap-3 mt-4">
                  <button
                    onClick={() => respondToRequest(req.id, "ACCEPTED")}
                    disabled={updatingId === req.id}
                    className={`px-4 py-2 text-sm rounded text-white ${
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
                    className={`px-4 py-2 text-sm rounded text-white ${
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

     
    </div>
  );
}
