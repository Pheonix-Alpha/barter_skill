export default function HeroText() {
  return (
    <div className="w-[634px] h-[285px] mt-8">
      <h1 className="text-[65px] font-bold text-[#252525] leading-[1.1]">
        are you looking for skill?
      </h1>
      <p className="mt-[17px] text-[24px] text-gray-700 leading-[1.4]">
        Hire Great Freelancers, Fast.{" "}
        <span className="text-black font-medium">
          Spacelance
        </span>{" "}
        helps you hire elite freelancers at a moment's notice.
      </p>

      {/* Button + Search in one row */}
<div className="mt-12 flex gap-4 items-center">
  {/* Button */}
  <button className="w-[250px] h-[55px] bg-[#1E88E5] text-white rounded-[10px] text-lg font-semibold hover:bg-blue-600 transition">
    Find a Skill
  </button>

  {/* Search input */}
  <input
    type="text"
    placeholder="Enter skill name..."
    className="w-full h-[55px] px-6 rounded-[10px] border border-gray-300 shadow-sm text-lg focus:outline-none"
  />
</div>

      
    </div>
  );
}
