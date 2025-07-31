"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

export default function EditProfilePage() {
  const [user, setUser] = useState(null);
  const [username, setUsername] = useState("");
  const [bio, setBio] = useState("");
  const [offeringSkills, setOfferingSkills] = useState("");
  const [wantingSkills, setWantingSkills] = useState("");
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
      return;
    }

    fetch("http://localhost:8080/api/users/me/profile", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        setUser(data);
        setUsername(data.username || "");
        setBio(data.bio || "");
        setOfferingSkills(data.offeringSkills?.join(", ") || "");
        setWantingSkills(data.wantingSkills?.join(", ") || "");
      })
      .catch(() => {
        router.push("/login");
      });
  }, [router]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");

    const payload = {
      username,
      bio,
      offeringSkills: offeringSkills
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean),
      wantingSkills: wantingSkills
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean),
    };

    try {
      const res = await fetch("http://localhost:8080/api/users/update", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error("Failed to update profile");

      alert("Profile updated!");
      router.refresh();
      router.push("/profile?updated=true");
    } catch (err) {
      console.error(err);
      alert("Error updating profile");
    }
  };

  if (!user) return <div className="text-center p-6">Loading profile...</div>;

  return (
    <div className="min-h-screen bg-gray-100 px-4 py-6">
      <div className="max-w-2xl mx-auto bg-white shadow-md rounded-lg p-6 sm:p-8">
        <h2 className="text-2xl font-bold mb-6 text-center">Edit Profile</h2>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Username (Read-only) */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Username
            </label>
            <input
              type="text"
              value={username}
              readOnly
              className="w-full px-4 py-2 border rounded bg-gray-100 text-gray-600 cursor-not-allowed"
            />
          </div>

          {/* Bio */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Bio
            </label>
            <textarea
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              rows={3}
              placeholder="Tell us about yourself..."
              className="w-full px-4 py-2 border rounded resize-none"
            />
          </div>

          {/* Offering Skills */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Offering Skills
            </label>
            <input
              type="text"
              value={offeringSkills}
              onChange={(e) => setOfferingSkills(e.target.value)}
              placeholder="e.g. Java, React, SQL"
              className="w-full px-4 py-2 border rounded"
            />
          </div>

          {/* Wanting Skills */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Wanting Skills
            </label>
            <input
              type="text"
              value={wantingSkills}
              onChange={(e) => setWantingSkills(e.target.value)}
              placeholder="e.g. Python, DevOps"
              className="w-full px-4 py-2 border rounded"
            />
          </div>

          {/* Buttons */}
          <div className="flex flex-col sm:flex-row justify-between gap-4">
            <button
              type="submit"
              className="bg-blue-600 text-white px-5 py-2 rounded hover:bg-blue-700 text-sm"
            >
              Save Changes
            </button>
            <button
              type="button"
              onClick={() => router.push("/profile")}
              className="bg-gray-300 text-gray-800 px-5 py-2 rounded hover:bg-gray-400 text-sm"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
