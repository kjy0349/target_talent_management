import { Footer } from "flowbite-react";
import type { FC } from "react";

const DashboardFooter: FC = function () {
  return (
    <Footer
      className="relative bottom-0 w-full rounded-none p-1 md:p-2 xl:p-4"
      container
    >
      <Footer.Copyright by="Samsung all rights reserved." year={2024} />
      <Footer.Brand src="/assets/logo/Samsung_lettermark_black.png" />
    </Footer>
  );
};

export default DashboardFooter;
