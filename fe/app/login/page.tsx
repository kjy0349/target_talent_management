"use client";

import { Button, Card, Label, TextInput } from "flowbite-react";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import logo from "@/public/assets/logo/Samsung_lettermark_black.png";
import { useTokenStore } from "@/stores/token";
import { useLocalAxios } from "@/app/api/axios";
import { useAuthStore } from "@/stores/auth";

export default function DefaultLoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const localAxios = useLocalAxios(false);
  const userStore = useAuthStore();
  const tokenStore = useTokenStore();
  const router = useRouter();

  const handleIdChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };
  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    // 로그인 해서 확인하는 로직 넣는 부분
    event.preventDefault();
    try {
      const response = await localAxios.post("/member/login", {
        id: email,
        password: password,
      });
      if (response.status === 200) {
        tokenStore.setAccessToken(response.data.accessToken);
        sessionStorage.setItem(
          "access-token",
          JSON.stringify(response.data.accessToken),
        );
        userStore.setAuth(response.data.member);
        userStore.setEmail(email);
        userStore.setIsCheckedPopup(false);
        // 인증했는지에 따라 위치를 이동시킨다.
        if (userStore.isSecuritySigned) {
          router.push("/");
        } else {
          router.push("/");
        }
      } else {
        alert("로그인 다른 코드 발생!");
        console.log("로그인 실패");
      }
    } catch (error) {
      // console.log(error?.response?.status);
      console.error("로그인 에러", error);
      alert("아이디 또는 비밀번호를 다시 확인해주세요.");
    }
  };

  return (
    <section className="bg-gray-50 dark:bg-gray-900">
      <div className="mx-auto flex flex-col items-center justify-center px-6 py-8 md:h-screen lg:py-0">
        <div className="w-full rounded-lg bg-white shadow dark:border dark:border-gray-700 dark:bg-gray-800 sm:max-w-md md:mt-0 xl:p-0">
          <Card className="shadow-none">
            <div className="mb-6 flex items-center justify-center text-2xl font-semibold text-gray-900 dark:text-white">
              <Image width="200" height="200" src={logo} alt="logo" />
            </div>
            <form className="space-y-4 md:space-y-6" onSubmit={handleSubmit}>
              <div>
                <Label htmlFor="email" className="mb-2 block dark:text-white">
                  Knox ID
                </Label>
                <TextInput
                  id="id"
                  name="id"
                  placeholder="name@samsung.com"
                  required
                  type="email"
                  value={email}
                  onChange={handleIdChange}
                />
              </div>
              <div>
                <Label
                  htmlFor="password"
                  className="mb-2 block dark:text-white"
                >
                  비밀번호
                </Label>
                <TextInput
                  id="password"
                  name="password"
                  placeholder="••••••••"
                  required
                  type="password"
                  value={password}
                  onChange={handlePasswordChange}
                />
              </div>
              <div className="flex items-center justify-end">
                <Link
                  href="/login/forgot"
                  className="text-primary-600 dark:text-primary-500 text-sm font-medium hover:underline"
                >
                  비밀번호 찾기
                </Link>
              </div>
              <Button type="submit" className="w-full">
                로그인/시작하기
              </Button>
            </form>
          </Card>
        </div>
      </div>
    </section>
  );
}
