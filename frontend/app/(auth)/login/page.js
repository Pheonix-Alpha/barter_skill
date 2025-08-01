"use client";

import { useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function LoginPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
     const res = await axios.post("http://localhost:8080/api/auth/login", formData);
const { token, role } = res.data;

localStorage.setItem("token", token);
  localStorage.setItem("role", role);

 console.log("Role:", res.data.role); // Must be "ADMIN"


// Route based on role
if (role === "ADMIN") {
  router.push("/admin");
} else {
  router.push("/dashboard");
}

    } catch (err) {
      setError(err.response?.data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F2FAFA] flex items-center justify-center px-4">
      <div className="w-full max-w-[1080px] h-auto md:h-[80vh] bg-white rounded-xl shadow-lg flex flex-col md:flex-row overflow-hidden">
        {/* Left Box */}
        <div className="w-full md:w-1/2 h-[200px] md:h-full relative">
          <img
            src="/images/login-image.png"
            alt="Login Illustration"
            className="absolute top-0 left-0 w-full h-full object-cover z-0"
            onError={(e) => (e.target.style.display = "none")}
          />
          <div className="absolute top-0 left-0 w-full h-full flex items-center justify-center z-20 bg-black/40 md:bg-transparent">
            <h1 className="text-4xl md:text-5xl font-bold text-white">SkillExchange</h1>
          </div>
        </div>

        {/* Right Box */}
        <div className="w-full md:w-1/2 h-full flex items-center justify-center px-6 py-10 md:px-12 md:py-16">
          <form className="w-full max-w-md" onSubmit={handleLogin}>
            <h2 className="text-3xl md:text-4xl font-bold text-[#252525] mb-8 text-center">Login</h2>

            {/* Username Input */}
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Enter your username"
              autoComplete="username"
              className="w-full mb-5 px-5 py-4 border border-gray-300 rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-[#1E88E5]"
              required
            />

            {/* Password Input */}
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Enter your password"
              autoComplete="current-password"
              className="w-full mb-6 px-5 py-4 border border-gray-300 rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-[#1E88E5]"
              required
            />

            {/* Error Message */}
            {error && <p className="text-red-600 mb-4 text-sm text-center">{error}</p>}

            {/* Login Button */}
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-[#1E88E5] text-white py-4 rounded-lg text-lg font-semibold hover:bg-blue-600 transition"
            >
              {loading ? "Logging in..." : "Login"}
            </button>

            {/* Links */}
            <div className="mt-6 text-sm text-gray-600 text-center">
              <a href="#" className="block mb-2 hover:underline">
                Forgot your password?
              </a>
              <p>
                Don’t have an account?{" "}
                <Link href="/register">
                  <span className="text-[#1E88E5] hover:underline font-medium">Registration</span>
                </Link>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
