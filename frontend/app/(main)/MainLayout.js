"use client";

import { useState } from "react";
import { usePathname } from "next/navigation";
import Sidebar from "@/components/Sidebar";
import ProfilePanel from "@/components/ProfilePanel";
import { IoMenu } from "react-icons/io5";
import "../globals.css";

export default function MainLayout({ children }) {
  const pathname = usePathname();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const isMessagePage = pathname.startsWith("/messages");
  const isFriendsPage = pathname.startsWith("/friends");

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <div className="flex h-screen relative">
      {/* Mobile Hamburger Icon */}
      <button
        className="absolute top-4 left-4 z-50 md:hidden text-3xl"
        onClick={toggleSidebar}
      >
        <IoMenu />
      </button>

      {/* Sidebar: hidden on mobile unless open */}
      <div
        className={`z-40 fixed inset-y-0 left-0 bg-white shadow-md transition-transform duration-300 transform ${
          sidebarOpen ? "translate-x-0" : "-translate-x-full"
        } md:relative md:translate-x-0 md:block`}
      >
        <Sidebar shrink={isMessagePage} />
      </div>

      {/* Main content */}
      <main
        className={`flex-1 overflow-y-auto bg-gray-100 ${
          isMessagePage || isFriendsPage ? "p-0" : "p-6"
        }`}
      >
        {children}
      </main>

      {/* Profile panel: hide on small screens */}
      {!isMessagePage && !isFriendsPage && (
        <div className="hidden lg:block">
          <ProfilePanel />
        </div>
      )}
    </div>
  );
}
