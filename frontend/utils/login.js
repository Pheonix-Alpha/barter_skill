// utils/login.js
export async function login(username, password) {
  const res = await fetch("http://localhost:8080/login", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    credentials: "include", // 🟢 important to store JSESSIONID cookie
    body: new URLSearchParams({ username, password }),
  });

  if (!res.ok) {
    throw new Error("Login failed");
  }

  return "Login successful";
}
