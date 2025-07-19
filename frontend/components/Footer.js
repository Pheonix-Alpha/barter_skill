import { FaInstagram, FaTwitter, FaFacebook } from "react-icons/fa";

export default function Footer() {
  return (
    <footer className="w-full bg-white py-16 px-8 text-[#252525]">
      <div className="max-w-[1295px] mx-auto grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12">
        {/* Column 1: SkillExchange Info */}
        <div>
          <h3 className="text-2xl font-bold mb-4">SkillExchange</h3>
          <p className="text-sm text-gray-600 mb-6">
            Powerful Freelance Marketplace System with ability to change the
            Users (Freelancers & Clients)
          </p>
          <div className="flex space-x-4">
            <FaInstagram className="text-xl hover:text-[#1E88E5] cursor-pointer" />
            <FaTwitter className="text-xl hover:text-[#1E88E5] cursor-pointer" />
            <FaFacebook className="text-xl hover:text-[#1E88E5] cursor-pointer" />
          </div>
        </div>

        {/* Column 2: For Teacher */}
        <div>
          <h4 className="text-xl font-semibold mb-4">For Teacher</h4>
          <ul className="space-y-3 text-gray-600">
            <li className="hover:text-[#1E88E5] cursor-pointer">Find Teacher</li>
            <li className="hover:text-[#1E88E5] cursor-pointer">Post Skill</li>
            <li className="hover:text-[#1E88E5] cursor-pointer">Privacy Policy</li>
          </ul>
        </div>

        {/* Column 3: For Learner */}
        <div>
          <h4 className="text-xl font-semibold mb-4">For Learner</h4>
          <ul className="space-y-3 text-gray-600">
            <li className="hover:text-[#1E88E5] cursor-pointer">Find Skill</li>
            <li className="hover:text-[#1E88E5] cursor-pointer">Create Account</li>
          </ul>
        </div>

        {/* Column 4: Contact Info */}
        <div>
          <h4 className="text-xl font-semibold mb-4">Call Us</h4>
          <ul className="space-y-3 text-gray-600">
            <li>India</li>
            <li>+91 123 456 7890</li>
            <li>support@skillexchange.com</li>
          </ul>
        </div>
      </div>

      {/* Bottom center text */}
      <div className="text-center mt-12 text-sm text-gray-500">
        © 2022 Spacelance. All rights reserved.
      </div>
    </footer>
  );
}
