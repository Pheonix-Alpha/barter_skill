"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function DashboardPage() {
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login"); // block access if not logged in
    }
  }, [router]);

  return (
    <div>
      {/* Search */}
      <input
        type="text"
        placeholder="Search..."
        className="w-full p-2 border rounded mb-4"
      />

      {/* Blue Skill Box */}
      <div className="bg-blue-500 text-white p-4 rounded mb-4 text-center font-semibold text-lg">
        Are you looking for skill?
      </div>

      {/* Upcoming Tasks */}
      <div className="bg-white shadow p-4 rounded">
        <h2 className="text-lg font-semibold mb-2">Upcoming Tasks</h2>
        <ul className="text-sm text-gray-600">
          <li>📅 Java Interview on Thursday</li>
          <li>📘 Lesson with Mentor on Friday</li>
        </ul>
      </div>
    </div>
  );
}
