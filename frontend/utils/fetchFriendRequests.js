export async function fetchFriendRequests() {
  const token = localStorage.getItem("token");

  const res = await fetch("http://localhost:8080/api/friends/received", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    throw new Error("Failed to load friend requests");
  }

  return res.json();
}
