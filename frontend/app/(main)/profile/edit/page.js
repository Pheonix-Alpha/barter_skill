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
    offeringSkills: Array.isArray(offeringSkills)
      ? offeringSkills
      : (offeringSkills || "").split(",").map((s) => s.trim()).filter(Boolean),
    wantingSkills: Array.isArray(wantingSkills)
      ? wantingSkills
      : (wantingSkills || "").split(",").map((s) => s.trim()).filter(Boolean),
  };

  try {
    console.log("Sending payload:", payload);

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

    router.refresh(); // ✅ force revalidation of profile page
    router.push("/profile?updated=true");
 // ✅ go to profile page

  } catch (err) {
    console.error(err);
    alert("Error updating profile");
  }
};


  if (!user) return <div className="text-center p-6">Loading profile...</div>;

  return (
    <div className="max-w-xl mx-auto bg-white shadow p-6 rounded mt-6">
      <h2 className="text-2xl font-bold mb-4">Edit Profile</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium">Username</label>
         <input
  type="text"
  value={username}
  readOnly
  className="w-full px-3 py-2 border rounded bg-gray-100 cursor-not-allowed"
/>

        </div>

        <div>
          <label className="block text-sm font-medium">Bio</label>
          <textarea
            value={bio}
            onChange={(e) => setBio(e.target.value)}
            rows={3}
            className="w-full px-3 py-2 border rounded"
          />
        </div>

        <div>
          <label className="block text-sm font-medium">Offering Skills</label>
          <input
            type="text"
            value={offeringSkills}
            onChange={(e) => setOfferingSkills(e.target.value)}
            placeholder="e.g. Java, React, SQL"
            className="w-full px-3 py-2 border rounded"
          />
        </div>

        <div>
          <label className="block text-sm font-medium">Wanting Skills</label>
          <input
            type="text"
            value={wantingSkills}
            onChange={(e) => setWantingSkills(e.target.value)}
            placeholder="e.g. Python, DevOps"
            className="w-full px-3 py-2 border rounded"
          />
        </div>

        <div className="flex justify-between">
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Save Changes
          </button>
          <button
            type="button"
            onClick={() => router.push("/profile")}
            className="bg-gray-300 text-gray-800 px-4 py-2 rounded hover:bg-gray-400"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
