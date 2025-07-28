import { useEffect, useState } from "react";
import axios from "axios";

const AcceptedLessonPanel = () => {
  const [acceptedRequests, setAcceptedRequests] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [scheduledTime, setScheduledTime] = useState("");
  const [duration, setDuration] = useState(60);
  const [notes, setNotes] = useState("");
  const [error, setError] = useState("");

  const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;

  useEffect(() => {
    const fetchAccepted = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/exchange/accepted", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setAcceptedRequests(res.data);
      } catch (err) {
        console.error("Error fetching accepted requests:", err);
      }
    };

    fetchAccepted();
  }, []);

  const openModal = (request) => {
    setSelectedRequest(request);
    setScheduledTime("");
    setDuration(60);
    setNotes("");
    setError("");
    setShowModal(true);
  };

  const handleSchedule = async () => {
    if (!selectedRequest || !scheduledTime || !duration) {
      setError("Please complete all fields.");
      return;
    }

    try {
      await axios.post(
        "http://localhost:8080/api/lessons/schedule",
        {
          receiverId: selectedRequest.userId,
          skillId: selectedRequest.skillId,
          time: scheduledTime,
          duration,
          notes,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      alert("Lesson scheduled successfully.");
      setShowModal(false);
    } catch (err) {
      setError("Error scheduling lesson: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow max-w-4xl mx-auto mt-6">
      <h2 className="text-2xl font-bold mb-4">✅ Accepted Requests</h2>
      {acceptedRequests.length === 0 ? (
        <p className="text-gray-500">No accepted skill requests.</p>
      ) : (
        <div className="space-y-4">
          {acceptedRequests.map((req) => (
            <div
              key={`${req.userId}-${req.skillId}`}
              className="flex justify-between items-center border rounded-lg p-4 shadow-sm"
            >
              <div>
                <p className="text-lg font-semibold">{req.username}</p>
                <p className="text-sm text-gray-600">Skill: {req.skillName}</p>
                <p className="text-sm text-green-600">Status: Accepted</p>
              </div>
              <button
                onClick={() => openModal(req)}
                className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded"
              >
                Schedule
              </button>
            </div>
          ))}
        </div>
      )}

      {showModal && selectedRequest && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-md">
            <h3 className="text-xl font-semibold mb-4">📅 Schedule Lesson</h3>
            <p className="mb-2 font-medium">
              With <strong>{selectedRequest.username}</strong> for{" "}
              <strong>{selectedRequest.skillName}</strong>
            </p>

            <input
              type="datetime-local"
              value={scheduledTime}
              onChange={(e) => setScheduledTime(e.target.value)}
              className="w-full border rounded px-3 py-2 mb-3"
            />
            <input
              type="number"
              placeholder="Duration (minutes)"
              value={duration}
              onChange={(e) => setDuration(Number(e.target.value))}
              className="w-full border rounded px-3 py-2 mb-3"
            />
            <textarea
              placeholder="Notes (optional)"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              className="w-full border rounded px-3 py-2 mb-3"
            />

            {error && <p className="text-red-500 text-sm">{error}</p>}

            <div className="flex justify-end gap-2">
              <button
                onClick={() => setShowModal(false)}
                className="px-4 py-2 border rounded"
              >
                Cancel
              </button>
              <button
                onClick={handleSchedule}
                className="px-4 py-2 bg-blue-600 text-white rounded"
              >
                Schedule
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AcceptedLessonPanel;
