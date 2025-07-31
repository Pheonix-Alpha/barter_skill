import Categories from "@/components/Categories";
import Footer from "@/components/Footer";
import HeroText from "@/components/HeroText";
import Navbar from "@/components/Navbar";
import Newsletter from "@/components/Newsletter";
import TestimonialSection from "@/components/TestimonialSection";
import FAQSection from "@/components/FAQSection";
import "./globals.css";

export default function Home() {
  return (
    <main className="min-h-screen bg-[#F2FAFA] flex flex-col items-center">
      {/* Container for navbar and hero section */}
      <div className="w-full px-4 sm:px-6 md:px-8 lg:px-12 xl:px-0 max-w-[1295px]">
        {/* Navbar */}
        <Navbar />

        {/* Hero Section */}
        <div className="flex justify-start mt-8 sm:mt-12">
          <HeroText />
        </div>
      </div>

      {/* Middle Sections */}
      <div className="w-full px-4 sm:px-6 md:px-8">
        <Categories />
        <Newsletter />
        <TestimonialSection />
        <FAQSection />
      </div>

      {/* Footer */}
      <Footer />
    </main>
  );
}
