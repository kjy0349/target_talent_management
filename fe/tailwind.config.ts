import flowbite from "flowbite-react/tailwind";
import type { Config } from "tailwindcss";
const colors = require("tailwindcss/colors");

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    flowbite.content(),
  ],
  theme: {
    fontFamily: {
      sans: [
        "Samsung One",
        "Samsung One Light",
        "Samsung One Medium",
        "Samsung One Bold",
        "Samsung One ExtraBold",
        "Samsung One Heavy",
      ],
    },

    extend: {
      colors: {
        primary: "#1428A0",
        black: "#000000",
        white: "#FFFFFF",
        // blue: colors.lightBlue,
        cyan: colors.blue,
        // cyan: {
        //   100: "#CED7FA",
        //   200: "#9FAFF5",
        //   300: "#6B80E2",
        //   400: "#4458C5",
        //   500: "#1428A0",
        //   600: "#0E1E89",
        //   700: "#0A1573",
        //   800: "#060E5C",
        //   900: "#03094C",
        // },
        info: {
          100: "#D1E4FC",
          200: "#A4C8FA",
          300: "#74A5F2",
          400: "#5185E6",
          500: "#1D58D6",
          600: "#1543B8",
          700: "#0E319A",
          800: "#09227C",
          900: "#051766",
        },
        // primary: {
        //   100: "#CED7FA",
        //   200: "#9FAFF5",
        //   300: "#6B80E2",
        //   400: "#4458C5",
        //   500: "#1428A0",
        //   600: "#0E1E89",
        //   700: "#0A1573",
        //   800: "#060E5C",
        //   900: "#03094C",
        // },
        // secondary: colors.yellow,
        // neutral: colors.gray,
      },
    },
  },
  plugins: [flowbite.plugin(), require("tailwind-scrollbar-hide")],
};

export default config;
