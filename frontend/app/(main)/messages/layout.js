// app/messages/layout.js
"use client";

import ConversationList from "@/components/ConversationList";

export default function MessageLayout({ children }) {
  return (
    <div className="flex h-screen">
      {/* Left Panel – Conversation List */}
      <ConversationList />

      {/* Center Panel – Chat Area */}
      <div className="flex-1 bg-gray-100 p-4 overflow-y-auto">
        {children}
      </div>

     
    </div>
  );
}
