"use client";

import Sidebar from "@/components/Sidebar";
import ProfilePanel from "@/components/ProfilePanel";
import { usePathname } from "next/navigation";
import "../globals.css";

export default function MainLayout({ children }) {
  const pathname = usePathname();

  const isMessagePage = pathname.startsWith("/messages");
  const isFriendsPage = pathname.startsWith("/friends");

  return (
    <div className="flex h-screen">
      {/* Sidebar: shrink only on message page, not on friends */}
      <Sidebar shrink={isMessagePage} />

      {/* Main content */}
      <main
        className={`flex-1 overflow-y-auto bg-gray-100 ${
          isMessagePage || isFriendsPage ? "p-0" : "p-6"
        }`}
      >
        {children}
      </main>

      {/* Right panel shown only when not on messages or friends */}
      {!isMessagePage && !isFriendsPage && <ProfilePanel />}
    </div>
  );
}
