"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  LayoutDashboard,
  Inbox,
  BookOpen,
  MessageSquare,
  Users,
  User,
  Settings,
  LogOut,
} from "lucide-react";

const Sidebar = () => {
  const pathname = usePathname();

  const navItems = [
    { icon: <LayoutDashboard />, href: "/dashboard", label: "Dashboard" },
    { icon: <Inbox />, href: "/inbox", label: "Inbox" },
    { icon: <BookOpen />, href: "/lessons", label: "Lessons" },
    { icon: <MessageSquare />, href: "/messages?userId=2", label: "Messages" },
    { icon: <Users />, href: "/groups", label: "Groups" },
    { icon: <User />, href: "/friends", label: "Friends" },
  ];

  const bottomLinks = [
    { icon: <Settings />, href: "/settings", label: "Settings" },
    { icon: <LogOut />, href: "/logout", label: "Logout", red: true },
  ];

  return (
    <aside className="w-16 h-screen bg-white border-r flex flex-col items-center justify-between py-4">
      {/* Logo */}
      <div className="flex flex-col items-center gap-6">
        <Link href="/dashboard">
          <img src="/images/logo.png" alt="Logo" className="w-8 h-8" />
        </Link>

        {navItems.map(({ icon, href, label }) => (
          <Link
            key={href}
            href={href}
            className={`p-2 rounded hover:bg-blue-100 ${
              pathname.startsWith(href.split("?")[0])
                ? "bg-blue-200 text-blue-600"
                : "text-gray-500"
            }`}
            title={label}
          >
            {icon}
          </Link>
        ))}
      </div>

      {/* Bottom Links */}
      <div className="flex flex-col items-center gap-4">
        {bottomLinks.map(({ icon, href, label, red }) => (
          <Link
            key={href}
            href={href}
            className={`p-2 rounded hover:bg-blue-100 ${
              pathname === href
                ? "bg-blue-200 text-blue-600"
                : red
                ? "text-red-500"
                : "text-gray-500"
            }`}
            title={label}
          >
            {icon}
          </Link>
        ))}
      </div>
    </aside>
  );
};

export default Sidebar;
