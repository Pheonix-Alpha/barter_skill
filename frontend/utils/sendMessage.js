export async function sendMessage(receiverId, message) {
  const token = localStorage.getItem("token");

  const res = await fetch(`http://localhost:8080/api/chat/send/${receiverId}`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ message }),
  });

  if (!res.ok) {
    const errorText = await res.text();
    console.error("Server error:", res.status, errorText);
    throw new Error("Failed to send message");
  }

  return await res.json();
}
