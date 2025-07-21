"use client";
import api from "@/utils/api";
import { useEffect, useState } from "react";
import { Check, X, Send } from "lucide-react";
import { jwtDecode } from "jwt-decode";

export default function FriendsPanel() {
  const [allUsers, setAllUsers] = useState([]);
  const [friends, setFriends] = useState([]);
  const [sentRequests, setSentRequests] = useState([]);
  const [receivedRequests, setReceivedRequests] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = jwtDecode(token);
      setCurrentUser({ id: decoded.id, username: decoded.username });
    }

    const fetchAll = async () => {
      try {
        const [all, sent, received, friendList] = await Promise.all([
          api.get("/users"),
          api.get("/friends/sent"),
          api.get("/friends/received"),
          api.get("/friends/list"),
        ]);
        setAllUsers(all.data);
        setSentRequests(sent.data);
        setReceivedRequests(received.data);
        setFriends(friendList.data);
      } catch (err) {
        console.error("Error fetching users:", err);
      }
    };

    fetchAll();
  }, []);

  const sendRequest = async (userId) => {
    await api.post(`/friends/request/${userId}`);
    setSentRequests((prev) => [...prev, allUsers.find((u) => u.id === userId)]);
  };

  const acceptRequest = async (userId) => {
    await api.post(`/friends/accept/${userId}`);
    setFriends((prev) => [...prev, allUsers.find((u) => u.id === userId)]);
    setReceivedRequests((prev) => prev.filter((u) => u.id !== userId));
  };

  const rejectRequest = async (userId) => {
    await api.post(`/friends/reject/${userId}`);
    setReceivedRequests((prev) => prev.filter((u) => u.id !== userId));
  };

  const isFriend = (userId) => friends.some((u) => u.id === userId);
  const isSent = (userId) => sentRequests.some((u) => u.id === userId);
  const isReceived = (userId) => receivedRequests.some((u) => u.id === userId);

  return (
    <div className="p-6 space-y-3">
      <h2 className="text-xl font-bold mb-2">All Users</h2>
      {allUsers
        .filter((u) => u.id !== currentUser?.id)
        .map((user) => (
          <div
            key={user.id}
            className="flex items-center justify-between border p-3 rounded"
          >
            <span>{user.username}</span>
            {isFriend(user.id) ? (
              <span className="text-green-600">Friends</span>
            ) : isSent(user.id) ? (
              <span className="text-yellow-600">Requested</span>
            ) : isReceived(user.id) ? (
              <div className="flex gap-2">
                <button
                  onClick={() => acceptRequest(user.id)}
                  className="bg-green-500 text-white px-2 py-1 rounded"
                >
                  <Check size={16} />
                </button>
                <button
                  onClick={() => rejectRequest(user.id)}
                  className="bg-red-500 text-white px-2 py-1 rounded"
                >
                  <X size={16} />
                </button>
              </div>
            ) : (
              <button
                onClick={() => sendRequest(user.id)}
                className="bg-blue-500 text-white px-2 py-1 rounded flex items-center gap-1"
              >
                <Send size={16} /> Request
              </button>
            )}
          </div>
        ))}
    </div>
  );
}
