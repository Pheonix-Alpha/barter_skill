"use client";

import Sidebar from "@/components/Sidebar";
import ProfilePanel from "@/components/ProfilePanel";
import { usePathname } from "next/navigation";
import "../globals.css";

export const metadata = {
  title: "Skill Exchange App",
};

export default function MainLayout({ children }) {
  const pathname = usePathname();
  const showProfile = !pathname.startsWith("/messages");

  return (
    <div className="flex h-screen">
      <Sidebar />
      <main className="flex-1 overflow-y-auto p-6 bg-gray-100">
        {children}
      </main>
      {showProfile && <ProfilePanel />}
    </div>
  );
}
