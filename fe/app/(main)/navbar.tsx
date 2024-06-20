"use client";

import { useSidebarContext } from "@/hooks/sidebar-context";
import { useMediaQuery } from "@/hooks/use-media-query";
import {
  Button,
  Dropdown,
  List,
  Navbar,
  TextInput,
  useThemeMode,
} from "flowbite-react";
import Image from "next/image";
import Link from "next/link";
import {
  HiBell,
  HiDocumentAdd,
  HiMenuAlt1,
  HiOutlinePlus,
  HiSearch,
  HiUsers,
} from "react-icons/hi";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import UserDropdown from "@/components/navbar/userdropdown";
import NotificationBellDropdown from "@/components/navbar/notification";
import { useEffect, useState } from "react";
import ProfileExcelModal from "@/components/profile/register/ProfileExcelModal";
import { useLocalAxios } from "../api/axios";
import { error } from "console";

export function DashboardNavbar() {
  const [isExcelModalOpen, setIsExcelModalOpen] = useState(false);
  const sidebar = useSidebarContext();
  const { computedMode } = useThemeMode();
  const isDesktop = useMediaQuery("(min-width: 1024px)");
  const searchParams = useSearchParams();
  function handleToggleSidebar() {
    if (isDesktop) {
      sidebar.desktop.toggle();
    } else {
      sidebar.mobile.toggle();
    }
  }
  const router = useRouter();
  const [keyword, setKeyword] = useState<string>("");
  const [list, setList] = useState<string[]>([]);
  const [isVisible, setIsVisible] = useState(false);

  const searchParamRouter: string = "/talent?search=" + keyword;

  const localAxios = useLocalAxios();

  const fetchNameList = async (keyword: string) => {
    if (keyword.trim().length > 0) {
      try {
        const response = await localAxios.post(`/profile/search-name`, {
          nameSearchString: keyword,
        });

        if (response.status === 200) {
          setList(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    }
  };

  useEffect(() => {
    fetchNameList(keyword);
  }, [keyword]);

  const handleChangeKeyword = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setKeyword(value);
  };

  const handleSubmitKeyword = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      if (keyword.trim().length > 0) {
        router.push(searchParamRouter);
        // setKeyword("");
      } else router.push("/talent");
    }
  };

  const pathname = usePathname();

  useEffect(() => setKeyword(searchParams.get("search") || ""), [pathname]);

  const handleClickListItem = (item: string) => {
    const searchParamRouter: string = "/talent?search=" + item;
    router.push(searchParamRouter);
  };

  return (
    <Navbar
      fluid
      className="fixed top-0 z-30 w-full border-b border-gray-200 bg-white p-0 dark:border-gray-700 dark:bg-gray-800 sm:p-0"
    >
      <div className="w-full p-3 pr-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <button
              onClick={handleToggleSidebar}
              className="mr-3 cursor-pointer rounded p-2 text-gray-600 hover:bg-gray-100 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
            >
              <span className="sr-only">Toggle sidebar</span>
              <div className="hidden lg:block">
                <HiMenuAlt1 className="size-6 dark:fill-white" />
              </div>
            </button>
            {computedMode === "light" && (
              <Navbar.Brand as={Link} href="/" className="mr-14">
                <Image
                  className="mr-3 h-8"
                  alt=""
                  src="/assets/logo/Samsung_lettermark_black.png"
                  width={128}
                  height={32}
                />
              </Navbar.Brand>
            )}
            {computedMode !== "light" && (
              <Navbar.Brand as={Link} href="/" className="mr-14">
                <Image
                  className="mr-3 h-8"
                  alt=""
                  src="/assets/logo/Samsung_lettermark_white.png"
                  width={128}
                  height={32}
                />
              </Navbar.Brand>
            )}
            <form className="relative hidden lg:block lg:pl-2">
              <TextInput
                className="w-full lg:w-96"
                icon={HiSearch}
                id="search"
                name="search"
                value={keyword}
                placeholder="인재 찾기"
                required
                type="search"
                autoComplete="off"
                onChange={handleChangeKeyword}
                onKeyDown={handleSubmitKeyword}
                onFocus={() => setIsVisible(true)}
                onBlur={() => setTimeout(() => setIsVisible(false), 100)}
              />
              <List
                unstyled
                className="absolute z-10 mt-1 w-96 divide-y divide-gray-200 bg-gray-50"
                hidden={!isVisible}
              >
                {list.map((item, index) => (
                  <List.Item
                    key={index}
                    className="cursor-pointer px-2 py-1 hover:bg-slate-200 hover:underline"
                    onClick={() => handleClickListItem(item)}
                  >
                    {item}
                  </List.Item>
                ))}
              </List>
            </form>
          </div>

          <div className="flex items-center lg:gap-3">
            <div className="flex items-center">
              <Dropdown
                color="dark"
                label={
                  <>
                    <HiUsers className="my-auto mr-2 inline-block" />
                    <span className="whitespace-nowrap">인재 추가하기</span>
                  </>
                }
              >
                <Dropdown.Item
                  as="button"
                  className="w-full"
                  onClick={() => router.push("/talent/register")}
                >
                  <div className="flex items-center gap-2">
                    <HiOutlinePlus />
                    <span>새 인재 등록</span>
                  </div>
                </Dropdown.Item>
                <Dropdown.Item
                  as="button"
                  className="w-full"
                  onClick={() => {
                    setIsExcelModalOpen(true);
                  }}
                >
                  <div className="flex items-center gap-2">
                    <HiDocumentAdd />
                    <span>데이터 가져오기</span>
                  </div>
                </Dropdown.Item>
              </Dropdown>

              <NotificationBellDropdown />
              {/* <Tooltip
                content={
                  computedMode === "light"
                    ? "Toggle dark mode"
                    : "Toggle light mode"
                }
              >
                <DarkThemeToggle className="mr-2" />
              </Tooltip> */}
              <UserDropdown />
            </div>
            <ProfileExcelModal
              isModalOpen={isExcelModalOpen}
              onClose={() => setIsExcelModalOpen(false)}
            />
          </div>
        </div>
      </div>
    </Navbar>
  );
}
