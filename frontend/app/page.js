import Categories from "@/components/Categories";
import Footer from "@/components/Footer";
import HeroText from "@/components/HeroText";
import Navbar from "@/components/Navbar";
import Newsletter from "@/components/Newsletter";
import "./globals.css"

export default function Home() {
  return (
    <main className="min-h-screen bg-[#F2FAFA] flex flex-col items-center">
      <div className="w-full max-w-[1295px]">
        <Navbar />
           <div className="flex justify-start mt-12">
              <HeroText />
          </div>
      </div>

       {/* Middle Section */}
      <Categories />

      <Newsletter />
      <Footer />
    </main>
  );
}
