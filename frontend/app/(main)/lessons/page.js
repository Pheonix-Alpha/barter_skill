"use client";
import { useEffect, useState } from "react";
import axios from "axios";

const LessonPanel = () => {
  const [acceptedRequests, setAcceptedRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [scheduledTime, setScheduledTime] = useState("");
  const [duration, setDuration] = useState(60);
  const [notes, setNotes] = useState("");
  const [error, setError] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  const token = typeof window !== "undefined" ? localStorage.getItem("token") : "";

  const fetchAcceptedRequests = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/lessons/accepted-requests", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setAcceptedRequests(res.data);
    } catch (err) {
      console.error("Failed to fetch accepted requests:", err);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        await axios.get("http://localhost:8080/api/users/me/profile", {
          headers: { Authorization: `Bearer ${token}` },
        });
        await fetchAcceptedRequests();
      } catch (err) {
        console.error("Error loading data:", err);
      } finally {
        setLoading(false);
      }
    };

    if (token) fetchData();
  }, [token]);

  const handleScheduleClick = (request) => {
    setSelectedRequest(request);
    setScheduledTime("");
    setDuration(60);
    setNotes("");
    setError("");
    setSuccessMsg("");
    setShowModal(true);
  };

  const handleScheduleSubmit = async () => {
    if (!selectedRequest || !scheduledTime || !duration) {
      setError("Please complete all required fields.");
      return;
    }

    const { userId: receiverId, skillId } = selectedRequest;

    if (!receiverId || !skillId) {
      setError("Missing receiverId or skillId");
      return;
    }

    try {
      setSubmitting(true);

      await axios.post(
        "http://localhost:8080/api/lessons/schedule",
        {
          receiverId,
          skillId,
          time: scheduledTime,
          duration,
          notes,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      // Update only that specific entry with lessonScheduled = true
      setAcceptedRequests((prev) =>
        prev.map((req) =>
          req.userId === receiverId && req.skillId === skillId
            ? { ...req, lessonScheduled: true }
            : req
        )
      );

      setSuccessMsg("Lesson scheduled successfully!");

      // Auto-close modal after a short delay
      setTimeout(() => {
        setShowModal(false);
        setSelectedRequest(null);
        setSuccessMsg("");
      }, 2000);
    } catch (err) {
      setError("Failed to schedule: " + (err.response?.data?.message || err.message));
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h2 className="text-2xl font-bold mb-6">📌 Schedule Lessons</h2>

      {acceptedRequests.length === 0 ? (
        <p className="text-gray-500">No accepted skill exchange requests found.</p>
      ) : (
        <div className="grid gap-4">
          {acceptedRequests.map((req, index) => (
            <div
              key={`${req.userId}-${req.skillId}-${index}`} // fallback composite key
              className="border rounded-lg p-4 shadow flex justify-between items-center"
            >
              <div>
                <p className="text-lg font-semibold">{req.skillName}</p>
                <p className="text-sm text-gray-600">
                  With: <span className="font-medium">{req.username}</span>
                </p>
              </div>

              {req.lessonScheduled ? (
                <button
                  className="bg-green-500 text-white px-4 py-2 rounded cursor-not-allowed"
                  disabled
                >
                  ✅ Scheduled
                </button>
              ) : (
                <button
                  onClick={() => handleScheduleClick(req)}
                  className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                >
                  Schedule
                </button>
              )}
            </div>
          ))}
        </div>
      )}

      {showModal && selectedRequest && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-md">
            <h3 className="text-xl font-semibold mb-4">Schedule Lesson</h3>
            <p className="mb-2 text-sm text-gray-600">
              Skill: <span className="font-bold">{selectedRequest.skillName}</span>
            </p>

            <div className="space-y-3">
              <input
                type="datetime-local"
                value={scheduledTime}
                onChange={(e) => setScheduledTime(e.target.value)}
                className="w-full border rounded px-3 py-2"
              />
              <input
                type="number"
                placeholder="Duration (minutes)"
                value={duration}
                onChange={(e) => setDuration(Number(e.target.value))}
                className="w-full border rounded px-3 py-2"
              />
              <textarea
                placeholder="Notes (optional)"
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                className="w-full border rounded px-3 py-2"
              ></textarea>

              {error && <p className="text-red-500 text-sm">{error}</p>}
              {successMsg && <p className="text-green-600 text-sm">{successMsg}</p>}

              <div className="flex justify-end gap-2 mt-4">
                <button
                  onClick={() => {
                    setShowModal(false);
                    setSelectedRequest(null);
                  }}
                  className="px-4 py-2 border rounded"
                >
                  Cancel
                </button>
                <button
                  onClick={handleScheduleSubmit}
                  disabled={submitting}
                  className={`px-4 py-2 text-white rounded ${
                    submitting ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700"
                  }`}
                >
                  {submitting ? "Scheduling..." : "Schedule"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default LessonPanel;
