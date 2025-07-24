"use client";

import FriendList from "@/components/FriendList";

export default function FriendsPage() {
  return (
    <div className="h-screen w-screen flex items-center justify-center bg-gray-100">
      <div className="w-96 h-[90vh] rounded-lg shadow bg-white flex flex-col overflow-hidden">
        <FriendList />
      </div>
    </div>
  );
}
