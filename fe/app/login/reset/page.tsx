"use client";

import { Button, Label, TextInput } from "flowbite-react";
import Image from "next/image";
import logo from "@/public/assets/logo/Samsung_lettermark_black.png";
import { useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { isAxiosError } from "axios";
import { useRouter } from "next/navigation";
export default function ResetPasswordForm() {
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const localAxios = useLocalAxios(false);
  const router = useRouter();
  const handleSubmit = async () => {
    if (password === confirmPassword) {
      try {
        const response = await localAxios.put(`/member/change-password`, {
          password: password,
        });
        if (response.status === 200) {
          alert("비밀번호가 변경되었습니다.");
          router.push("/login");
        } else {
          alert("error");
        }
      } catch (error) {
        if (isAxiosError(error)) {
          if (error.response?.status === 403) {
            alert("다시 시도해주세요.");
            router.back();
          }
        }
      }
    } else {
      alert("패스워드를 확인해주세요.");
    }
  };
  return (
    <section className="bg-gray-50 dark:bg-gray-900">
      <div className="mx-auto flex flex-col items-center justify-center px-6 py-8 md:h-screen lg:py-0">
        <div className="w-full rounded-lg bg-white p-6 shadow dark:border dark:border-gray-700 dark:bg-gray-800 sm:max-w-md sm:p-8 md:mt-0">
          <div className="mb-6 flex items-center justify-center text-2xl font-semibold text-gray-900 dark:text-white">
            <Image width="200" height="200" src={logo} alt="logo" />
          </div>

          <div className="mt-4 space-y-4 md:space-y-5 lg:mt-5">
            <div className="grid grid-cols-1 gap-2">
              <Label htmlFor="password" className="dark:text-white">
                새로운 비밀번호
              </Label>
              <TextInput
                name="password"
                id="password"
                placeholder="••••••••"
                onChange={(e) => setPassword(e.target.value)}
                required
                type="password"
              />
            </div>
            <div className="grid grid-cols-1 gap-2">
              <Label htmlFor="confirm-password" className="dark:text-white">
                비밀번호 확인
              </Label>
              <TextInput
                name="confirm-password"
                id="confirm-password"
                placeholder="••••••••"
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                type="password"
              />
            </div>
            {/* <div className="flex items-start">
              <div className="flex h-5 items-center">
                <Checkbox aria-describedby="terms" id="terms" required />
              </div>
              <div className="ml-3 text-sm">
                <Label
                  htmlFor="terms"
                  className="text-gray-500 dark:text-gray-300"
                >
                  I accept the&nbsp;
                  <a
                    className="text-primary-600 dark:text-primary-500 font-medium hover:underline"
                    href="#"
                  >
                    Terms and Conditions
                  </a>
                </Label>
              </div>
            </div> */}
            <Button onClick={handleSubmit}>패스워드 변경</Button>
          </div>
        </div>
      </div>
    </section>
  );
}
