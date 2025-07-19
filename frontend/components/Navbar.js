"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

export default function Navbar() {
  const pathname = usePathname();

  const navItems = [
    { name: "Home", href: "/" },
    { name: "Find Skill", href: "/find-skill" },
    { name: "Login", href: "/login" },
    { name: "Signup", href: "/register" }, // or /signup if that's your route
  ];

  return (
    <nav
      className="w-full h-[62px] bg-white/70 backdrop-blur-md rounded-full mt-10 
                 px-[15px] pt-[20px] pb-[25px] flex items-center justify-between shadow-sm"
    >
      <div className="text-2xl font-semibold text-gray-800">SkillExchange</div>

      <div className="flex space-x-8">
        {navItems.map((item) => (
          <Link key={item.name} href={item.href}>
            <span
              className={`text-lg font-medium transition-colors cursor-pointer ${
                pathname === item.href ? "text-[#1E88E5]" : "text-gray-700"
              } hover:text-[#1E88E5]`}
            >
              {item.name}
            </span>
          </Link>
        ))}
      </div>
    </nav>
  );
}
