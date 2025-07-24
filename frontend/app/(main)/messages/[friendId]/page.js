"use client";

import { use, useEffect, useState } from "react";
import { fetchMessages } from "@/utils/fetchMessages";
import { sendMessage } from "@/utils/sendMessage";

export default function ChatPage({ params }) {
  const { friendId } = use(params); // ✅ unwraps the async `params` properly

  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState("");

  useEffect(() => {
    async function loadChat() {
      try {
        const data = await fetchMessages(friendId);
        setMessages(data);
      } catch (err) {
        console.error("❌ Error loading messages", err);
      }
    }

    loadChat();
  }, [friendId]);

  async function handleSend() {
    if (!newMsg.trim()) return;

    try {
      const sent = await sendMessage(friendId, newMsg);
      setMessages((prev) => [...prev, sent]);
      setNewMsg("");
    } catch (err) {
      console.error("❌ Failed to send message", err);
    }
  }

  return (
    <div className="flex flex-col h-full justify-between">
      {/* Chat messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {messages.map((msg, idx) => {
          const isSentByMe = msg.sender?.id != friendId;

          return (
            <div
              key={msg.id || `${idx}-${msg.timestamp}`}
              className={`flex flex-col ${isSentByMe ? "items-end" : "items-start"}`}
            >
              <div
                className={`px-4 py-2 rounded-xl max-w-xs text-sm ${
                  isSentByMe ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-900"
                }`}
              >
                {msg.content}
              </div>

              <span className="text-[10px] text-gray-500 mt-1">
                {new Date(msg.timestamp).toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </span>
            </div>
          );
        })}
      </div>

      {/* Message input */}
      <div className="p-4 border-t flex items-center gap-2">
        <input
          type="text"
          value={newMsg}
          onChange={(e) => setNewMsg(e.target.value)}
          placeholder="Type a message..."
          className="flex-1 border rounded px-3 py-2"
        />
        <button
          onClick={handleSend}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Send
        </button>
      </div>
    </div>
  );
}
