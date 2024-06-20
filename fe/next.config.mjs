/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false,

  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:8080/:path*",
      },
    ];
  },

  swcMinify: true,
  experimental: {
    optimizePackageImports: ["react-icons/*"],
  },
  images: {
    remotePatterns: [
      // 내부 테스트용
      {
        protocol: "http",
        hostname: "localhost",
      },
      {
        protocol: "http",
        hostname: "k10s102.p.ssafy.io",
      },
    ],
    // sizes: "250px",
  },

  typescript: {
    // !! WARN !!
    // Dangerously allow production builds to successfully complete even if
    // your project has type errors.
    // !! WARN !!
    ignoreBuildErrors: true,
  },
};

export default nextConfig;
