"use client";

import ProfilePanel from "@/components/ProfilePanel";

export default function ProfilePage() {
  return (
    <div className="min-h-screen bg-gray-100 px-4 py-6 sm:px-6 md:px-10">
      <h1 className="text-xl sm:text-2xl font-bold mb-4 text-gray-800">
        My Profile
      </h1>

      <div className="bg-white shadow rounded-lg p-4 sm:p-6">
        <ProfilePanel />
      </div>
    </div>
  );
}
