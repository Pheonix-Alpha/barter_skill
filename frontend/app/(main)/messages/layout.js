"use client";

import { useState, useEffect } from "react";
import FriendList from "@/components/FriendList";
import ChatPage from "./[friendId]/page"; // Dynamically rendered child
import { useParams } from "next/navigation";

export default function MessageLayout({ children }) {
  const [isMobile, setIsMobile] = useState(false);
  const params = useParams(); // gives friendId when chatting

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 768);
    };

    handleResize(); // set on load
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const hasSelectedFriend = !!params?.friendId;

  return (
    <div className="flex h-screen overflow-hidden">
      {/* FRIEND LIST */}
      {(!isMobile || !hasSelectedFriend) && (
        <div className="w-full md:w-72 border-r bg-white">
          <FriendList />
        </div>
      )}

      {/* CHAT PANEL */}
      {(!isMobile || hasSelectedFriend) && (
        <div className="flex-1 bg-gray-100 p-4 overflow-y-auto">
          {children}
        </div>
      )}
    </div>
  );
}
