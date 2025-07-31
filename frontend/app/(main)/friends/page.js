"use client";

import FriendList from "@/components/FriendList";

export default function FriendsPage() {
  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-100 px-4 py-6">
      <div className="w-full max-w-md h-[90vh] rounded-lg shadow bg-white flex flex-col overflow-hidden">
        <FriendList />
      </div>
    </div>
  );
}
