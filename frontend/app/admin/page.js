"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function AdminIndex() {
  const router = useRouter();

  useEffect(() => {
    // Delay until client-side hydration
    const role = typeof window !== "undefined" ? localStorage.getItem("role") : null;

    if (role !== "ADMIN") {
      router.replace("/unauthorized"); // Use replace to prevent back nav
    } else {
      router.replace("/admin/dashboard");
    }
  }, [router]);

  return <div className="p-4 text-gray-600">Redirecting...</div>; // Optional fallback UI
}
