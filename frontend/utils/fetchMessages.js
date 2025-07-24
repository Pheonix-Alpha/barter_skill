// utils/fetchMessages.js
export async function fetchMessages(userId, limit = 20, offset = 0) {
  const token = localStorage.getItem("token");

  const res = await fetch(`http://localhost:8080/api/chat/with/${userId}?limit=${limit}&offset=${offset}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    throw new Error("Failed to fetch messages");
  }

  return await res.json();
}
