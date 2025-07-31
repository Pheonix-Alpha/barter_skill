"use client";

import React, { useState, useEffect } from "react";
import { fetchFriends } from "@/utils/fetchFriends";
import { fetchFriendRequests } from "@/utils/fetchFriendRequests";
import { useRouter } from "next/navigation";
import { ChevronDown, ChevronUp } from "lucide-react";

export default function FriendList() {
  const [friends, setFriends] = useState([]);
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showRequests, setShowRequests] = useState(false);
  const router = useRouter();

  useEffect(() => {
    async function loadFriends() {
      try {
        const data = await fetchFriends();
        setFriends(data);
      } catch (err) {
        console.error("Failed to fetch friends:", err);
      } finally {
        setLoading(false);
      }
    }

    loadFriends();
  }, []);

  async function toggleRequests() {
    if (showRequests) {
      setShowRequests(false);
    } else {
      try {
        const reqs = await fetchFriendRequests();
        setRequests(reqs);
        setShowRequests(true);
      } catch (err) {
        console.error("Failed to fetch requests:", err);
      }
    }
  }

  async function handleAcceptRequest(requesterId) {
    const token = localStorage.getItem("token");
    try {
      const res = await fetch(
        `http://localhost:8080/api/friends/accept/${requesterId}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) throw new Error("Failed to accept request");

      setRequests((prev) => prev.filter((r) => r.id !== requesterId));
      const newFriend = requests.find((r) => r.id === requesterId);
      if (newFriend) setFriends((prev) => [...prev, newFriend]);

      alert("Friend request accepted!");
    } catch (err) {
      console.error("Error accepting request:", err);
      alert("Failed to accept friend request.");
    }
  }

  if (loading) return <div className="p-4 text-sm">Loading friends...</div>;

  return (
    <div className="w-full h-full flex flex-col p-4 sm:p-6">
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg sm:text-xl font-bold">Friends</h2>
        <button onClick={toggleRequests} aria-label="Toggle Friend Requests">
          {showRequests ? <ChevronUp size={20} /> : <ChevronDown size={20} />}
        </button>
      </div>

      {/* Friend Requests Section */}
      {showRequests && (
        <div className="mb-4">
          <h3 className="text-sm font-semibold mb-2">📥 Received Requests</h3>
          {requests.length === 0 ? (
            <p className="text-gray-500 text-sm">No new requests.</p>
          ) : (
            <ul className="space-y-2">
              {requests.map((req) => (
                <li
                  key={req.id}
                  className="bg-yellow-50 p-2 rounded flex items-center justify-between gap-3 text-sm"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 flex items-center justify-center rounded-full bg-yellow-400 text-white font-bold">
                      {req.username[0]?.toUpperCase()}
                    </div>
                    <span>{req.username}</span>
                  </div>
                  <button
                    onClick={() => handleAcceptRequest(req.id)}
                    className="text-xs sm:text-sm bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded"
                  >
                    Accept
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      {/* Friends List */}
      <ul className="space-y-2 flex-1 overflow-y-auto pr-1">
        {friends.map((friend) => (
          <li key={friend.id}>
            <div
              onClick={() => router.push(`/messages/${friend.id}`)}
              className="cursor-pointer hover:bg-blue-100 p-2 rounded flex items-center gap-3 transition-colors"
            >
              <div className="w-8 h-8 flex items-center justify-center rounded-full bg-blue-500 text-white font-bold">
                {friend.username[0]?.toUpperCase()}
              </div>
              <span className="text-sm sm:text-base font-medium">
                {friend.username}
              </span>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
