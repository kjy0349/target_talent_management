import { SidebarProvider } from "@/hooks/sidebar-context";
import AdminPage from "./page";
import { sidebarCookie } from "@/lib/sidebar-cookie";
import { DashboardNavbar } from "../(main)/navbar";
import { DashboardSidebar } from "../admin/sidebar";
import { LayoutContent } from "./layout-content";
import { PropsWithChildren } from "react";

export default function AdminLayout({ children }: PropsWithChildren) {
  return (
    <SidebarProvider initialCollapsed={sidebarCookie.get().isCollapsed}>
      <DashboardNavbar />
      <div className="mt-16 flex items-start">
        <DashboardSidebar />
        <LayoutContent>{children}</LayoutContent>
      </div>
    </SidebarProvider>
  );
}
