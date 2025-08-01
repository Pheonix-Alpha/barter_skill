"use client";

import { useEffect, useState } from "react";

export default function AdminRequestsPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");

    async function fetchRequests() {
      try {
        const res = await fetch("http://localhost:8080/api/admin/requests", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) throw new Error("Failed to fetch requests");

        const data = await res.json();
        setRequests(data);
      } catch (err) {
        setError(err.message || "Something went wrong");
      } finally {
        setLoading(false);
      }
    }

    fetchRequests();
  }, []);

  const handleDelete = async (id) => {
    const token = localStorage.getItem("token");
    const confirmDelete = confirm("Are you sure you want to delete this request?");
    if (!confirmDelete) return;

    try {
      const res = await fetch(`http://localhost:8080/api/admin/requests/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error("Failed to delete request");

      setRequests((prev) => prev.filter((req) => req.id !== id));
    } catch (err) {
      alert(err.message || "Failed to delete request");
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">🔁 Skill Exchange Requests</h2>

      {loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : requests.length === 0 ? (
        <p className="text-gray-500">No requests found.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white shadow rounded overflow-hidden">
            <thead className="bg-gray-100 text-left text-sm text-gray-600">
              <tr>
                <th className="px-4 py-2">Sender</th>
                <th className="px-4 py-2">Receiver</th>
                <th className="px-4 py-2">Offered → Wanted</th>
                <th className="px-4 py-2">Status</th>
                <th className="px-4 py-2">Type</th>
                <th className="px-4 py-2">Created</th>
                <th className="px-4 py-2 text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req) => (
                <tr key={req.id} className="border-t text-sm">
                  <td className="px-4 py-2">{req.requesterUsername}</td>
                  <td className="px-4 py-2">{req.targetUsername}</td>
                  <td className="px-4 py-2">
                    {req.offeredSkill} → {req.wantedSkill}
                  </td>
                  <td className="px-4 py-2">{req.status}</td>
                  <td className="px-4 py-2">{req.type}</td>
                  <td className="px-4 py-2">{new Date(req.createdAt).toLocaleString()}</td>
                  <td className="px-4 py-2 text-right">
                    <button
                      onClick={() => handleDelete(req.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded text-xs"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
