"use client";
import { useSidebarContext } from "../../hooks/sidebar-context";
import { PropsWithChildren } from "react";
import { twMerge } from "tailwind-merge";
import DashboardFooter from "../(main)/footer";

export function LayoutContent({ children }: PropsWithChildren) {
  const sidebar = useSidebarContext();

  return (
    <>
      <div
        id="main-content"
        className={twMerge(
          "relative h-full min-h-full w-full overflow-y-auto bg-gray-50 dark:bg-gray-900",
          sidebar.desktop.isCollapsed ? "lg:ml-16" : "lg:ml-64",
        )}
      >
        {children}
        <DashboardFooter />
      </div>
    </>
  );
}
