"use client";

import { useEffect, useState } from "react";

export default function AdminLessonsPage() {
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");

    async function fetchLessons() {
      try {
        const res = await fetch("http://localhost:8080/api/admin/lessons", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) throw new Error("Failed to fetch lessons");

        const data = await res.json();
        setLessons(data);
      } catch (err) {
        setError(err.message || "Something went wrong");
      } finally {
        setLoading(false);
      }
    }

    fetchLessons();
  }, []);

  const handleDelete = async (lessonId) => {
    const token = localStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/api/admin/lessons/${lessonId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      setLessons((prev) => prev.filter((l) => l.id !== lessonId));
    } else {
      alert("Failed to delete lesson");
    }
  };

  return (
    <div className="p-6">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">📚 Scheduled Lessons</h2>

      {loading ? (
        <p className="text-gray-500">Loading lessons...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : (
        <div className="overflow-x-auto rounded shadow">
          <table className="min-w-full text-sm bg-white">
            <caption className="text-left text-lg font-semibold px-4 py-3 text-gray-700">
              Lessons Overview
            </caption>
            <thead className="bg-gray-100 text-gray-700">
              <tr>
                <th className="px-4 py-2 text-left">User 1</th>
                <th className="px-4 py-2 text-left">User 2</th>
                <th className="px-4 py-2 text-left">Skill</th>
                <th className="px-4 py-2 text-left">Scheduled Time</th>
                <th className="px-4 py-2 text-left">Zoom Link</th>
                <th className="px-4 py-2 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {lessons.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center py-6 text-gray-500">
                    No lessons found.
                  </td>
                </tr>
              ) : (
                lessons.map((lesson) => (
                   <tr key={lesson.id} className="border-t hover:bg-gray-50 transition">
        <td className="px-4 py-2">{lesson.senderUsername}</td>
        <td className="px-4 py-2">{lesson.receiverUsername}</td>
        <td className="px-4 py-2">{lesson.skillName}</td>
        <td className="px-4 py-2">
          {new Date(lesson.scheduledTime).toLocaleString()}
        </td>
                    <td className="px-4 py-2">
                      {lesson.platformLink ? (
                        <a
                            href={lesson.platformLink}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-blue-600 underline hover:text-blue-800"
                        >
                          Join Link
                        </a>
                      ) : (
                        <span className="text-gray-400">Pending</span>
                      )}
                    </td>
                    <td className="px-4 py-2">
                      <button
                        onClick={() => handleDelete(lesson.id)}
                        className="text-red-600 hover:underline hover:text-red-800"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
