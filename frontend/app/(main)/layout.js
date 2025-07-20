import MainLayout from "./MainLayout";

export const metadata = {
  title: "Skill Exchange App",
};

export default function Layout({ children }) {
  return <MainLayout>{children}</MainLayout>;
}
