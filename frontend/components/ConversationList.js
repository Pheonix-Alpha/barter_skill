"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";

export default function ConversationList() {
  const [conversations, setConversations] = useState([]);
  const router = useRouter();

  useEffect(() => {
    const fetchConversations = async () => {
      const token = localStorage.getItem("token");

      try {
        const res = await axios.get("http://localhost:8080/api/messages/conversations", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setConversations(res.data);
      } catch (err) {
        console.error("❌ Failed to load conversations", err);
      }
    };

    fetchConversations();
  }, []);

  return (
    <div className="w-72 border-r bg-white p-4 overflow-y-auto">
      <h2 className="text-lg font-semibold mb-4">Chats</h2>
      <ul className="space-y-3">
        {conversations.map((c) => {
          const initial = c.username?.charAt(0)?.toUpperCase() || "?";

          return (
            <li
              key={c.userId}
              onClick={() => router.push(`/messages/${c.userId}`)}
              className="cursor-pointer flex items-center gap-3 hover:bg-gray-100 p-2 rounded"
            >
              {/* Initial-based profile circle */}
              <div className="w-10 h-10 rounded-full bg-blue-600 text-white flex items-center justify-center text-sm font-semibold">
                {initial}
              </div>

              <div className="overflow-hidden">
                <p className="font-medium text-sm truncate">{c.username}</p>
                <p className="text-xs text-gray-500 truncate max-w-[160px]">
                  {c.lastMessage || "No messages yet"}
                </p>
              </div>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
