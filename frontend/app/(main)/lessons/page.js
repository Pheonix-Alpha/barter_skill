"use client";

import { useEffect, useState } from "react";

export default function LessonsPage() {
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/lessons", {
      credentials: "include", // send cookies (important for Spring Security)
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to load lessons");
        return res.json();
      })
      .then(setLessons)
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-semibold mb-4">Your Scheduled Lessons</h2>

      {loading ? (
        <p>Loading...</p>
      ) : lessons.length === 0 ? (
        <p>No lessons scheduled yet.</p>
      ) : (
        <ul className="space-y-4">
          {lessons.map((lesson) => (
            <li
              key={lesson.id}
              className="p-4 border rounded-lg shadow-sm bg-white"
            >
              <p>
                <strong>With:</strong>{" "}
                {lesson.otherUser?.username || "Unknown"}
              </p>
              <p>
                <strong>Topic:</strong> {lesson.topic}
              </p>
              <p>
                <strong>Scheduled At:</strong>{" "}
                {new Date(lesson.scheduledAt).toLocaleString()}
              </p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
