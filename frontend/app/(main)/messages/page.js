"use client"


import { useEffect } from "react";
import { jwtDecode } from "jwt-decode";

import api from "@/utils/api"; // adjust the path if needed

export default function TestFriendsAPI() {
  useEffect(() => {
  const testSendFriendRequest = async () => {
    try {
      const token = localStorage.getItem("token");
      const decoded = jwtDecode(token);
console.log("Decoded JWT Token:", decoded);
const currentUserId = decoded.userId;

      console.log("Current Logged In User ID:", currentUserId);

      const receiverId = 4; // 👈 Replace with actual user ID to test
      console.log("Sending request to:", receiverId);

      const res = await api.post(`/friends/request/${receiverId}`);
      console.log("✅ Friend Request Sent:", res.data);
    } catch (err) {
        console.error("❌ Friend Request Failed:");
  console.log(err); // 👈 full error
    }
  };

  testSendFriendRequest();
}, []);


  return <div>Testing Friend API...</div>;
}
