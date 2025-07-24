// utils/fetchFriends.js
export async function fetchFriends() {
  const token = localStorage.getItem("token");

  const res = await fetch("http://localhost:8080/api/friends/list", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  if (!res.ok) {
    throw new Error("Failed to fetch friends");
  }

  return await res.json();
}
