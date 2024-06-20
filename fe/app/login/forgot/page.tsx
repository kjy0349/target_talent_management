"use client";

import { useState } from "react";
import { Button, Label, TextInput } from "flowbite-react";
import Image from "next/image";
import logo from "@/public/assets/logo/Samsung_lettermark_black.png";
import { useRouter } from "next/navigation";
import { useLocalAxios } from "@/app/api/axios";
import { isAxiosError } from "axios";

export default function ResetPasswordForm() {
  const [knoxId, setKnoxId] = useState("");
  const [waitingCode, setWaitingCode] = useState(false);
  const [code, setCode] = useState("");
  const router = useRouter();
  const localAxios = useLocalAxios(false);
  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const response = await localAxios.post(`/member/forgot`, knoxId);
      if (response.status === 200) {
        setWaitingCode(true);
        alert("인증번호가 발송되었습니다. 이메일을 확인해주세요.");
      } else {
        alert("애러");
      }
    } catch (error) {
      if (isAxiosError(error)) {
        if (error.response?.status === 404) {
          alert("올바르지 않은 이메일입니다.");
        }
      }
    }
  };

  const handleCode = async () => {
    const params = new URLSearchParams({ authorizationCode: code });
    try {
      const response = await localAxios.get(`/member/${knoxId}/verify`, {
        params,
      });
      if (response.status === 200) {
        setWaitingCode(true);
        alert("인증이 완료되었습니다.");
        router.push("/login/reset");
      } else {
        alert("애러");
      }
    } catch (error) {
      if (isAxiosError(error)) {
        if (error.response?.status === 405) {
          alert("올바르지 않은 인증번호 입니다.");
        } else if (error.response?.status === 500) {
          alert("서버와 연결할 수 없습니다.");
        }
      }
    }
  };

  return (
    <section className="bg-gray-50 dark:bg-gray-900">
      <div className="mx-auto flex flex-col items-center justify-center px-4 py-6 md:h-screen lg:py-0">
        <div className="w-full space-y-10 rounded-lg bg-white p-4 shadow dark:border dark:border-gray-700 dark:bg-gray-800 sm:max-w-md sm:p-8 md:mt-0">
          <div className="mb-4 flex items-center justify-center text-2xl font-semibold text-gray-900 dark:text-white">
            <Image width="200" height="200" src={logo} alt="logo" />
          </div>
          <form
            className="mt-4 space-y-4 md:space-y-5 lg:mt-3"
            onSubmit={handleSubmit}
          >
            <div className="grid grid-cols-6 gap-2">
              <Label
                htmlFor="email"
                className="col-span-6 text-lg font-bold dark:text-white"
              >
                비밀번호를 찾고자하는 아이디를 입력해주세요.
              </Label>
              <TextInput
                name="knoxId"
                id="knoxId"
                placeholder="name@samsung.com"
                required
                type="email"
                value={knoxId}
                className="col-span-4"
                onChange={(e) => setKnoxId(e.target.value)}
              />
              <Button type="submit" className="col-span-2 ">
                이메일 인증
              </Button>
            </div>
          </form>
          {waitingCode ? (
            <div className="grid grid-cols-6 gap-2">
              <Label
                htmlFor="authorizationCode"
                className="col-span-6 text-lg font-bold dark:text-white"
              >
                인증 번호
              </Label>
              <TextInput
                id="authorizationCode"
                name="authorizationCode"
                className="col-span-4"
                onChange={(e) => setCode(e.target.value)}
              />
              <Button className="col-span-2" onClick={handleCode}>
                인증 확인
              </Button>
            </div>
          ) : null}
        </div>
      </div>
    </section>
  );
}
