import type { Metadata } from "next";
import { Flowbite, ThemeModeScript } from "flowbite-react";
import "./globals.css";
import type { PropsWithChildren } from "react";

export const metadata: Metadata = {
  title: "인재 Pool 관리 서비스",
  description: "TATA",
  icons: {
    icon: "/assets/logo/favicon.ico",
  },
};

export default function RootLayout({ children }: PropsWithChildren) {
  return (
    <html lang="ko">
      <head>
        <ThemeModeScript />
      </head>
      <body className="bg-gray-50 text-[14px] dark:bg-gray-900 2xl:text-[16px]">
        <Flowbite>{children}</Flowbite>
      </body>
    </html>
  );
}
