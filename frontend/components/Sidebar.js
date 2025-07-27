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

const Sidebar = ({ shrink = false }) => {
  const pathname = usePathname();

  const navItems = [
    { icon: <LayoutDashboard />, href: "/dashboard", label: "Dashboard" },
    { icon: <User />, href: "/profile", label: "Profile" }, 
    { icon: <Inbox />, href: "/inbox", label: "Inbox" },
    { icon: <BookOpen />, href: "/lessons", label: "Lessons" },
    { icon: <MessageSquare />, href: "/messages?userId=2", label: "Messages" },
    { icon: <User />, href: "/friends", label: "Friends" },
  ];

  const bottomLinks = [
    { icon: <Settings />, href: "/settings", label: "Settings" },
    { icon: <LogOut />, href: "/logout", label: "Logout", red: true },
  ];

  return (
    <aside
      className={`${
        shrink ? "w-16" : "w-64"
      } h-screen bg-white border-r flex flex-col justify-between py-4`}
    >
      {/* Logo + Navigation */}
      <div className="flex flex-col items-center gap-6">
        <Link href="/dashboard">
          <img
            src="/images/logo.png"
            alt="Logo"
            className={`transition-all ${shrink ? "w-8 h-8" : "w-10 h-10"}`}
          />
        </Link>

        <nav className="flex flex-col items-center gap-4 w-full">
          {navItems.map(({ icon, href, label }) => (
            <Link
              key={href}
              href={href}
              className={`flex items-center gap-2 p-2 rounded hover:bg-blue-100 w-full justify-center ${
                pathname.startsWith(href.split("?")[0])
                  ? "bg-blue-200 text-blue-600"
                  : "text-gray-500"
              }`}
              title={label}
            >
              {icon}
              {!shrink && <span className="text-sm">{label}</span>}
            </Link>
          ))}
        </nav>
      </div>

      {/* Bottom Links */}
      <div className="flex flex-col items-center gap-4">
        {bottomLinks.map(({ icon, href, label, red }) => (
          <Link
            key={href}
            href={href}
            className={`flex items-center gap-2 p-2 rounded hover:bg-blue-100 w-full justify-center ${
              pathname === href
                ? "bg-blue-200 text-blue-600"
                : red
                ? "text-red-500"
                : "text-gray-500"
            }`}
            title={label}
          >
            {icon}
            {!shrink && <span className="text-sm">{label}</span>}
          </Link>
        ))}
      </div>
    </aside>
  );
};

export default Sidebar;
