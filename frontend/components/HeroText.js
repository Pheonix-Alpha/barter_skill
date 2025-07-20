"use client";
import { useRouter } from "next/navigation";

export default function HeroText() {
  const router = useRouter();

  return (
    <div className="flex items-center gap-12 mt-8 w-full">
      {/* Left Text Section */}
      <div className="w-[634px]">
        <h1 className="text-[65px] font-bold text-[#252525] leading-[1.1]">
          are you looking for skill?
        </h1>
        <p className="mt-[17px] text-[24px] text-gray-700 leading-[1.4]">
          Hire Great Freelancers, Fast.{" "}
          <span className="text-black font-medium">Spacelance</span> helps you
          hire elite freelancers at a moment's notice.
        </p>

        {/* Button + Search */}
        <div className="mt-12 flex gap-4 items-center">
          <button
            className="w-[250px] h-[55px] bg-[#1E88E5] text-white rounded-[10px] text-lg font-semibold hover:bg-blue-600 transition"
            onClick={() => router.push("/find-skill")}
          >
            Find a Skill
          </button>

          <input
            type="text"
            placeholder="Enter skill name..."
            className="w-full h-[55px] px-6 rounded-[10px] border border-gray-300 shadow-sm text-lg focus:outline-none"
          />
        </div>
      </div>

      {/* Right Image Section */}
      <div className="flex-shrink-0">
        <img
          src="/images/Hero-image.png" // replace with your actual image path
          alt="Skill Illustration"
          className="w-[400px] h-auto object-contain"
        />
      </div>
    </div>
  );
}
