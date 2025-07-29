"use client";

import ProfilePanel from "@/components/ProfilePanel";

export default function ProfilePage() {
  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h1 className="text-2xl font-bold mb-4">My Profile</h1>
      <ProfilePanel />
    </div>
  );
}
