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

      {/* Right Panel – Groups + File Sharing */}
      <div className="w-72 border-l bg-gray-50 p-4 flex flex-col">
        {/* Groups */}
        <div className="flex-1 mb-4">
          <h3 className="text-md font-semibold mb-2">Groups</h3>
          <ul className="space-y-2 text-blue-600 text-sm">
            <li># Java Learners</li>
            <li># Design Team</li>
          </ul>
        </div>

        {/* File Sharing */}
        <div className="border-t pt-4">
          <h3 className="text-md font-semibold mb-2">File Sharing</h3>
          <ul className="text-sm text-gray-600 space-y-1">
            <li>📄 resume.pdf</li>
            <li>📸 project.png</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
