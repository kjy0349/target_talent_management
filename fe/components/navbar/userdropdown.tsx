"use client";
import { Avatar, Dropdown } from "flowbite-react";
import { useAuthStore } from "@/stores/auth";
import { useTokenStore } from "@/stores/token";
import { useRouter } from "next/navigation";
import useFilterStore from "@/stores/filter";
export default function UserDropdown() {
  const user = useAuthStore();
  const token = useTokenStore();
  const filter = useFilterStore();
  const router = useRouter();
  const handleLogout = () => {
    user.clearAuth();
    token.clearAccessToken();
    filter.clearFilter();
    sessionStorage.removeItem("access-token");
    alert("로그아웃 되었습니다.");
    router.push("/login");
    //temp
    localStorage.removeItem("recentProfiles");
    localStorage.removeItem("recentProjects");
  };
  return (
    <Dropdown
      className="rounded"
      arrowIcon={false}
      inline
      label={
        <span>
          <span className="sr-only">User menu</span>
          <Avatar alt="" rounded size="sm" />
        </span>
      }
    >
      <Dropdown.Header className="px-4 py-3">
        <span className="font-one block text-sm font-bold">{user.name} ({user.departmentName})</span>
        <span className="block truncate text-sm font-medium">({user.email})</span>
      </Dropdown.Header>
      {/* <Dropdown.Item>Dashboard</Dropdown.Item>
      <Dropdown.Item>Settings</Dropdown.Item>
      <Dropdown.Item>Earnings</Dropdown.Item>
      <Dropdown.Divider /> */}
      <Dropdown.Item onClick={handleLogout}>로그아웃</Dropdown.Item>
    </Dropdown>
  );
}
