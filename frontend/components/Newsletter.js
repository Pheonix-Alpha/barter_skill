export default function Newsletter() {
  return (
    <div className="w-full bg-[#F2FAFA] py-16 flex flex-col items-center justify-center">
      {/* Top Text Box */}
      <div className="w-[800px] text-center mb-10">
        <h2 className="text-4xl font-bold text-[#252525]">
          Newsletter Subscription
        </h2>
        <p className="text-lg text-[#9D9D9D]">
          Subscribe to our newsletter to get new skill work
        </p>
      </div>

      {/* Input and Button Box */}
      <div className="w-[662px] h-[194px] flex flex-col items-center justify-center space-y-6">
        <input
          type="email"
          placeholder="Enter your email address"
          className="w-full h-[60px] px-6 border bg-[#FFFFFF] border-gray-300 rounded-md text-lg focus:outline-none"
        />
        <button className="w-[300px] h-[76px] bg-[#1E88E5] text-white text-xl font-semibold rounded-[10px] hover:bg-blue-600 transition-colors">
          Subscribe
        </button>
      </div>
    </div>
  );
}
