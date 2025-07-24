"use client";

import { useState } from "react";

export default function SkillSearch({ token }) {
  const [skill, setSkill] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  const handleSearch = async () => {
    if (!skill.trim()) return;
    try {
      const res = await fetch(`http://localhost:8080/api/matchmaking/search?skill=${skill}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        credentials: "include",
      });
      const data = await res.json();
      setSearchResults(data);
    } catch (err) {
      console.error("Search failed", err);
    }
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Search skills like Java, React, etc."
        value={skill}
        onChange={(e) => setSkill(e.target.value)}
        className="w-full p-2 border rounded mb-2"
      />

      <div
        onClick={handleSearch}
        className="bg-blue-500 text-white p-4 rounded mb-4 text-center font-semibold text-lg cursor-pointer hover:bg-blue-600"
      >
        Are you looking for skill? 🔍
      </div>

      {searchResults.length > 0 && (
        <div className="bg-white shadow p-4 rounded mb-4">
          <h2 className="text-lg font-semibold mb-2">Users with Skill: {skill}</h2>
          <ul className="space-y-2">
            {searchResults.map((user) => (
              <li
                key={user.id}
                className="p-3 border rounded flex justify-between items-center"
              >
                <div>
                  <p className="font-semibold">{user.username}</p>
                  <p className="text-sm text-gray-600">{user.email}</p>
                </div>
                <button
                  onClick={async () => {
                    const res = await fetch(`http://localhost:8080/api/friends/request/${user.id}`, {
                      method: "POST",
                      headers: {
                        Authorization: `Bearer ${token}`,
                      },
                      credentials: "include",
                    });
                    const text = await res.text();
                    alert(text);
                  }}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                >
                  Send Request
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
