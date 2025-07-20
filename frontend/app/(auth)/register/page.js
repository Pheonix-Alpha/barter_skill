"use client";

import { useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function RegistrationPage() {
  const router = useRouter();

  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    role: "USER",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");
    console.log("Registering with:", formData);


    try {
      const res = await axios.post("http://localhost:8080/api/auth/register", formData);
 // your backend endpoint
      router.push("/login"); // redirect to login after successful registration
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="h-screen bg-[#F2FAFA] flex items-center justify-center overflow-hidden">
      <div className="w-[1080px] max-w-[1920px] h-[80vh] bg-white p-2 rounded-xl shadow-lg flex overflow-hidden">
        {/* Left Box */}
        <div className="w-[600px] h-full relative overflow-hidden">
          <img
            src="/images/login-image.png"
            alt="Registration Illustration"
            className="absolute top-0 left-0 w-full h-full object-cover z-0"
          />
          <div className="absolute top-0 left-0 w-full h-full flex items-center justify-center z-20">
            <h1 className="text-5xl font-bold text-white">SkillExchange</h1>
          </div>
        </div>

        {/* Right Box */}
        <div className="w-[500px] h-full flex items-center justify-center px-12 py-10">
          <form className="w-full max-w-md" onSubmit={handleRegister}>
            <h2 className="text-4xl font-bold text-[#252525] mb-8 flex justify-center">Registration</h2>

            {/* Name */}
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Enter your name"
              className="w-full mb-5 px-5 py-4 border border-gray-300 rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-[#1E88E5]"
              required
            />

            {/* Email */}
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Enter your email"
              className="w-full mb-5 px-5 py-4 border border-gray-300 rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-[#1E88E5]"
              required
            />

            {/* Password */}
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Create a password"
              className="w-full mb-6 px-5 py-4 border border-gray-300 rounded-lg text-lg focus:outline-none focus:ring-2 focus:ring-[#1E88E5]"
              required
            />

            {/* Error */}
            {error && <p className="text-red-600 mb-4 text-sm text-center">{error}</p>}

            {/* Submit */}
            <button
              type="submit"
              className="w-full bg-[#1E88E5] text-white py-4 rounded-lg text-lg font-semibold hover:bg-blue-600 transition"
            >
              Register
            </button>

            {/* Link to Login */}
            <div className="mt-6 text-sm text-gray-600 text-center">
              <p>
                Already have an account?{" "}
                <Link href="/login">
                  <span className="text-[#1E88E5] hover:underline font-medium">Login</span>
                </Link>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
