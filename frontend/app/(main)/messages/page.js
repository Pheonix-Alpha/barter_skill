"use client";
import api from "@/utils/api";
import { useEffect, useState } from "react";
import { Search, Send } from "lucide-react";
import { jwtDecode } from "jwt-decode";

export default function MessagesPage() {
  const [users, setUsers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [currentUserId, setCurrentUserId] = useState(null);

  // Get current user ID from JWT
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = jwtDecode(token);
      setCurrentUserId(decoded.id);
    }
  }, []);

  // Fetch all users
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await api.get("/users");
        setUsers(res.data);
      } catch (err) {
        console.error("❌ Failed to fetch users:", err);
      }
    };

    fetchUsers();
  }, []);

  // Fetch messages when a user is selected
  useEffect(() => {
    const fetchMessages = async () => {
      if (!selectedUserId) return;

      try {
        const res = await api.get(`/chat/with/${selectedUserId}`);
        setMessages(res.data);
      } catch (err) {
        console.error("❌ Failed to fetch messages:", err);
      }
    };

    fetchMessages();
  }, [selectedUserId]);

  // Auto-scroll chat to bottom when messages change
  useEffect(() => {
    const chatBox = document.getElementById("chat-box");
    if (chatBox) {
      chatBox.scrollTop = chatBox.scrollHeight;
    }
  }, [messages]);

  // Send new message
  const handleSend = async () => {
    if (!newMessage.trim() || !selectedUserId) return;

    try {
      await api.post(`/chat/send/${selectedUserId}`, {
        message: newMessage,
      });
      setNewMessage("");
      const res = await api.get(`/chat/with/${selectedUserId}`);
      setMessages(res.data);
    } catch (err) {
      console.error("❌ Failed to send message:", err);
    }
  };

  return (
    <div className="flex h-screen">
      {/* Left Panel: User List */}
      <div className="w-64 bg-gray-50 border-r p-4 flex flex-col">
        <h2 className="text-xl font-semibold mb-4">Users</h2>

        <div className="relative mb-4">
          <Search className="absolute left-3 top-2.5 text-gray-400" />
          <input
            type="text"
            placeholder="Search users..."
            className="pl-10 pr-3 py-2 w-full border rounded focus:outline-none"
          />
        </div>

        <div className="flex-1 overflow-y-auto space-y-2">
          {users.map((user) => (
            <div
              key={user.id}
              onClick={() => setSelectedUserId(user.id)}
              className={`cursor-pointer p-2 rounded hover:bg-blue-100 ${
                selectedUserId === user.id ? "bg-blue-200 font-semibold" : ""
              }`}
            >
              {user.username}
            </div>
          ))}
        </div>
      </div>

      {/* Center Panel: Chat */}
      <div className="flex-1 flex flex-col bg-white">
        <div
          id="chat-box"
          className="flex-1 overflow-y-auto p-6 space-y-3 bg-gray-50"
        >
          {/* Date Header */}
          {messages.length > 0 && (
            <div className="text-center text-gray-500 text-sm mb-4">
              {new Date(messages[0].timestamp).toLocaleDateString()}
            </div>
          )}

          {messages.map((msg, i) => {
            const isMe = msg.sender?.id === currentUserId;
            return (
              <div
                key={i}
                className={`max-w-md p-3 rounded-lg text-sm shadow ${
                  isMe
                    ? "bg-blue-600 text-white self-end"
                    : "bg-gray-200 text-gray-900 self-start"
                }`}
              >
                <div>{msg.message}</div>
                <div className="text-xs mt-1 text-right text-gray-300">
                  {new Date(msg.timestamp).toLocaleString()}
                </div>
              </div>
            );
          })}
        </div>

        {/* Message input */}
        {selectedUserId && (
          <div className="border-t p-4 flex gap-2">
            <input
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Type your message..."
              className="flex-1 border rounded px-4 py-2"
            />
            <button
              onClick={handleSend}
              className="bg-blue-600 text-white px-4 py-2 rounded flex items-center gap-1"
            >
              <Send className="w-4 h-4" /> Send
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
