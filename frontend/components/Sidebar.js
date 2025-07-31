"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
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
  const router = useRouter();

  const navItems = [
    { icon: <LayoutDashboard size={20} />, href: "/dashboard", label: "Dashboard" },
    { icon: <User size={20} />, href: "/profile", label: "Profile" },
    { icon: <Inbox size={20} />, href: "/inbox", label: "Inbox" },
    { icon: <BookOpen size={20} />, href: "/lessons", label: "Lessons" },
    { icon: <MessageSquare size={20} />, href: "/messages?userId=2", label: "Messages" },
    { icon: <Users size={20} />, href: "/friends", label: "Friends" },
  ];

  const bottomLinks = [
    { icon: <Settings size={20} />, href: "/settings", label: "Settings" },
    {
      icon: <LogOut size={20} />,
      label: "Logout",
      red: true,
      onClick: () => {
        localStorage.removeItem("token");
        router.push("/login");
      },
    },
  ];

  return (
    <aside
      className={`
        ${shrink ? "w-16" : "w-64"} 
        h-full md:h-screen bg-white border-r 
        flex flex-col justify-between py-4 
        transition-all duration-300 ease-in-out
      `}
    >
      {/* Top Section */}
      <div className="flex flex-col items-center gap-6 px-2">
        <Link href="/dashboard" className="mt-2">
          <img
            src="/images/logo.png"
            alt="Logo"
            className={`transition-all ${shrink ? "w-8 h-8" : "w-10 h-10"}`}
          />
        </Link>

        <nav className="flex flex-col items-center gap-2 w-full">
          {navItems.map(({ icon, href, label }) => {
            const isActive = pathname.startsWith(href.split("?")[0]);
            return (
              <Link
                key={href}
                href={href}
                className={`flex items-center gap-3 px-4 py-2 rounded w-full 
                  ${isActive ? "bg-blue-100 text-blue-600" : "text-gray-600 hover:bg-gray-100"}
                  ${shrink ? "justify-center" : ""}
                `}
                title={label}
              >
                {icon}
                {!shrink && <span className="text-sm font-medium">{label}</span>}
              </Link>
            );
          })}
        </nav>
      </div>

      {/* Bottom Section */}
      <div className="flex flex-col items-center gap-2 px-2 mb-2">
        {bottomLinks.map(({ icon, href, label, red, onClick }) => {
          const isActive = pathname === href;
          const className = `
            flex items-center gap-3 px-4 py-2 rounded w-full 
            ${isActive ? "bg-blue-100 text-blue-600" : red ? "text-red-500" : "text-gray-600 hover:bg-gray-100"}
            ${shrink ? "justify-center" : ""}
          `;
          return onClick ? (
            <button key={label} onClick={onClick} className={className} title={label}>
              {icon}
              {!shrink && <span className="text-sm font-medium">{label}</span>}
            </button>
          ) : (
            <Link key={href} href={href} className={className} title={label}>
              {icon}
              {!shrink && <span className="text-sm font-medium">{label}</span>}
            </Link>
          );
        })}
      </div>
    </aside>
  );
};

export default Sidebar;
