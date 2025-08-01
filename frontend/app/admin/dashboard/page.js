"use client";
import AdminRequestCard from "@/components/AdminRequestCard";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  Users,
  Repeat,
  BookOpen,
  MessageCircle,
  Inbox,
} from "lucide-react";


export default function AdminDashboard() {
  const [totalUsers, setTotalUsers] = useState(null);
  const [totalExchanges, setTotalExchanges] = useState(null);
  const [lessonsScheduled, setLessonsScheduled] = useState(0);
  const [activeChats, setActiveChats] = useState(0);
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) return;

    const headers = { Authorization: `Bearer ${token}` };

    axios
      .get("http://localhost:8080/api/admin/dashboard/stats", { headers })
      .then((res) => {
        const data = res.data;
        setTotalUsers(data.totalUsers);
        setTotalExchanges(data.totalExchanges);
        setLessonsScheduled(data.lessonsScheduled || 0);
        setActiveChats(data.activeChats || 0);
      })
      .catch((err) => {
        console.error("Failed to fetch admin stats:", err);
      });

    axios
      .get("http://localhost:8080/api/admin/requests", { headers })
      .then((res) => {
        setRequests(res.data || []);
      })
      .catch((err) => {
        console.error("Failed to fetch requests:", err);
      });
  }, []);

  const handleDeleteRequest = async (requestId) => {
    const token = localStorage.getItem("token");
    const headers = { Authorization: `Bearer ${token}` };

    try {
      await axios.delete(`http://localhost:8080/api/admin/requests/${requestId}`, {
        headers,
      });
      setRequests((prev) => prev.filter((r) => r.id !== requestId));
    } catch (err) {
      console.error("Error deleting request:", err);
    }
  };

  return (
    <div className="flex-1 bg-gray-50 py-8 px-4 md:px-6 lg:px-12">
      <div className="max-w-7xl mx-auto">
        <h2 className="text-3xl sm:text-4xl font-bold text-gray-800 mb-6 sm:mb-8 flex flex-wrap items-center gap-3">
          📊 Admin Dashboard
        </h2>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4 sm:gap-6 mb-8">
          <StatCard
            title="Total Users"
            value={totalUsers}
            icon={<Users className="w-5 h-5 sm:w-6 sm:h-6" />}
            color="blue"
          />
          <StatCard
            title="Skill Exchanges"
            value={totalExchanges}
            icon={<Repeat className="w-5 h-5 sm:w-6 sm:h-6" />}
            color="green"
          />
          <StatCard
            title="Lessons Scheduled"
            value={lessonsScheduled}
            icon={<BookOpen className="w-5 h-5 sm:w-6 sm:h-6" />}
            color="purple"
          />
          <StatCard
            title="Active Chats"
            value={activeChats}
            icon={<MessageCircle className="w-5 h-5 sm:w-6 sm:h-6" />}
            color="yellow"
          />
          <StatCard
            title="Total Requests"
            value={requests.length}
            icon={<Inbox className="w-5 h-5 sm:w-6 sm:h-6" />}
            color="red"
          />
        </div>

        {/* Request List */}
        <div className="mt-10">
          <h3 className="text-xl font-semibold text-gray-700 mb-4">
            📨 Skill Exchange Requests
          </h3>
          {requests.length === 0 ? (
            <p className="text-gray-500 text-sm">No requests found.</p>
          ) : (
            <div className="grid gap-4">
              {requests.map((req) => (
                <AdminRequestCard
                  key={req.id}
                  request={req}
                  onDelete={handleDeleteRequest}
                />
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function StatCard({ title, value, icon, color }) {
  const bgColor = {
    blue: "bg-blue-100",
    green: "bg-green-100",
    purple: "bg-purple-100",
    yellow: "bg-yellow-100",
    red: "bg-red-100",
  }[color];

  const textColor = {
    blue: "text-blue-600",
    green: "text-green-600",
    purple: "text-purple-600",
    yellow: "text-yellow-600",
    red: "text-red-600",
  }[color];

  return (
    <div className="bg-white rounded-xl shadow-md hover:shadow-lg transition-shadow duration-300 p-4 sm:p-5 flex items-center gap-4">
      <div className={`${bgColor} ${textColor} p-3 sm:p-4 rounded-full`}>
        {icon}
      </div>
      <div className="overflow-hidden">
        <p className="text-sm text-gray-500 truncate">{title}</p>
        <p className="text-2xl sm:text-3xl font-bold text-gray-800">
          {value !== null ? value : "—"}
        </p>
      </div>
    </div>
  );
}
