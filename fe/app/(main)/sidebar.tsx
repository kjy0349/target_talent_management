"use client";

import { useSidebarContext } from "@/hooks/sidebar-context";
import { Sidebar, TextInput, Tooltip } from "flowbite-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import type { ComponentProps, FC, HTMLAttributeAnchorTarget } from "react";
import { useEffect, useState } from "react";
import { useAuthStore } from "@/stores/auth";

import {
  HiAdjustments,
  HiBell,
  HiChartPie,
  HiCog,
  HiFolder,
  HiSearch,
  HiUser,
  HiUsers,
  HiClipboard,
  HiMap,
  HiViewBoards,
  HiSpeakerphone,
} from "react-icons/hi";

import { twMerge } from "tailwind-merge";

interface SidebarItem {
  href?: string;
  target?: HTMLAttributeAnchorTarget;
  icon?: FC<ComponentProps<"svg">>;
  label: string;
  items?: SidebarItem[];
  badge?: string;
}

interface SidebarItemProps extends SidebarItem {
  pathname: string;
}

export function DashboardSidebar() {
  return (
    <>
      <div className="lg:hidden">
        <MobileSidebar />
      </div>
      <div className="hidden lg:block">
        <DesktopSidebar />
      </div>
    </>
  );
}

function DesktopSidebar() {
  const authStore = useAuthStore();
  const pathname = usePathname();
  const { isCollapsed, setCollapsed } = useSidebarContext().desktop;
  const [isPreview, setIsPreview] = useState(isCollapsed);
  useEffect(() => {
    if (isCollapsed) setIsPreview(false);
  }, [isCollapsed]);

  const preview = {
    enable() {
      if (!isCollapsed) return;

      setIsPreview(true);
      setCollapsed(false);
    },
    disable() {
      if (!isPreview) return;

      setCollapsed(true);
    },
  };

  return (
    <Sidebar
      // onMouseEnter={preview.enable}
      // onMouseLeave={preview.disable}
      aria-label="Sidebar with multi-level dropdown example"
      collapsed={isCollapsed}
      className={twMerge(
        "fixed inset-y-0 left-0 z-20 flex h-full shrink-0 flex-col border-r border-gray-200 pt-16 duration-75 dark:border-gray-700 lg:flex",
        isCollapsed && "hidden w-16",
      )}
      id="sidebar"
    >
      <div className="flex h-full flex-col justify-between">
        <div className="py-4">
          <Sidebar.Items>
            <Sidebar.ItemGroup className="mt-0 border-t-0 pb-1 pt-0">
              {pages.map((item) => (
                <SidebarItem key={item.label} {...item} pathname={pathname} />
              ))}
              {authStore && authStore.authLevel && authStore.authLevel < 2 && (
                <SidebarItem
                  href="/admin"
                  icon={HiViewBoards}
                  label="관리자"
                  pathname={pathname}
                />
              )}
            </Sidebar.ItemGroup>
            {/* <Sidebar.ItemGroup className="mt-2 pt-2">
              {externalPages.map((item) => (
                <SidebarItem key={item.label} {...item} pathname={pathname} />
              ))}
            </Sidebar.ItemGroup> */}
          </Sidebar.Items>
        </div>
        {/* <BottomMenu isCollapsed={isCollapsed} /> */}
      </div>
    </Sidebar>
  );
}

function MobileSidebar() {
  const authStore = useAuthStore();
  const pathname = usePathname();
  const { isOpen, close } = useSidebarContext().mobile;

  if (!isOpen) return null;

  return (
    <>
      <Sidebar
        aria-label="Sidebar with multi-level dropdown example"
        className={twMerge(
          "fixed inset-y-0 left-0 z-20 hidden h-full shrink-0 flex-col border-r border-gray-200 pt-16 dark:border-gray-700 lg:flex",
          isOpen && "flex",
        )}
        id="sidebar"
      >
        <div className="flex h-full flex-col justify-between">
          <div className="py-2">
            <form className="pb-3">
              <TextInput
                icon={HiSearch}
                type="search"
                placeholder="인재 검색"
                required
                size={32}
              />
            </form>
            <Sidebar.Items>
              <Sidebar.ItemGroup className="mt-0 border-t-0 pb-1 pt-0">
                {pages.map((item) => (
                  <SidebarItem key={item.label} {...item} pathname={pathname} />
                ))}
                {authStore &&
                  authStore.authLevel &&
                  authStore.authLevel < 2 && (
                    <SidebarItem
                      href="/admin"
                      icon={HiViewBoards}
                      label="관리자"
                      pathname={pathname}
                    />
                  )}
              </Sidebar.ItemGroup>
              {/* <Sidebar.ItemGroup className="mt-2 pt-2">
                {externalPages.map((item) => (
                  <SidebarItem key={item.label} {...item} pathname={pathname} />
                ))}
              </Sidebar.ItemGroup> */}
            </Sidebar.Items>
          </div>
          <BottomMenu isCollapsed={false} />
        </div>
      </Sidebar>
      <div
        onClick={close}
        aria-hidden="true"
        className="fixed inset-0 z-10 h-full w-full bg-gray-900/50 pt-16 dark:bg-gray-900/90"
      />
    </>
  );
}

function SidebarItem({
  href,
  target,
  icon,
  label,
  items,
  badge,
  pathname,
}: SidebarItemProps) {
  if (items) {
    const isOpen = items.some((item) => pathname.startsWith(item.href ?? ""));

    return (
      <Sidebar.Collapse
        icon={icon}
        label={label}
        open={isOpen}
        theme={{ list: "space-y-2 py-2  [&>li>div]:w-full" }}
      >
        {items.map((item) => (
          <Sidebar.Item
            key={item.label}
            as={Link}
            href={item.href}
            target={item.target}
            className={twMerge(
              "justify-center [&>*]:font-normal",
              pathname === item.href && "bg-gray-200 dark:bg-gray-700",
            )}
          >
            {item.label}
          </Sidebar.Item>
        ))}
      </Sidebar.Collapse>
    );
  }

  return (
    <Sidebar.Item
      as={Link}
      href={href}
      target={target}
      icon={icon}
      label={badge}
      className={twMerge(pathname === href && "bg-gray-200 dark:bg-gray-700")}
    >
      {label}
    </Sidebar.Item>
  );
}

function BottomMenu({ isCollapsed }: { isCollapsed: boolean }) {
  return (
    <div
      className={twMerge(
        "flex items-center justify-center gap-4",
        isCollapsed && "flex-col",
      )}
    >
      <button className="inline-flex cursor-pointer justify-center rounded p-2 text-gray-500 hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-700 dark:hover:text-white">
        <span className="sr-only">Tweaks</span>
        <HiAdjustments className="h-6 w-6" />
      </button>
      <Tooltip content="Settings page">
        <Link
          href="/users/settings"
          className="inline-flex cursor-pointer justify-center rounded p-2 text-gray-500 hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-700 dark:hover:text-white"
        >
          <span className="sr-only">Settings page</span>
          <HiCog className="h-6 w-6" />
        </Link>
      </Tooltip>
    </div>
  );
}

let pages: SidebarItem[] = [
  { href: "/board", icon: HiSpeakerphone, label: "공지사항" },
  { href: "/talent", icon: HiUser, label: "프로필" },
  { href: "/project", icon: HiFolder, label: "프로젝트" },
  { href: "/networking", icon: HiUsers, label: "네트워킹" },
  { href: "/techmap", icon: HiMap, label: "기술인재Pool" },
  { href: "/alarm", icon: HiBell, label: "알람 목록" },
];
