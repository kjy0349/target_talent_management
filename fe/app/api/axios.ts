import { useState } from "react";
import axios, {
  AxiosInstance,
  InternalAxiosRequestConfig,
  AxiosResponse,
  AxiosError,
} from "axios";
// import {useAuthStore} from "@/stores/auth.ts";
import { useTokenStore } from "@/stores/token";
import { useRouter } from "next/navigation";

export const useLocalAxios = (isAuthenticated: boolean = true) => {
  // const authStore = useAuthStore();
  const tokenStore = useTokenStore();
  const router = useRouter();
  // const [isRefreshing, setIsRefreshing] = useState(false);
  const [refreshSubscribers, setRefreshSubscribers] = useState<
    ((newAccessToken: string) => void)[]
  >([]);

  // const addRefreshSubscriber = (callback: (newAccessToken: string) => void) => {
  //     setRefreshSubscribers((prev) => [...prev, callback]);
  // };

  // const onRefreshed = (newAccessToken: string) => {
  //     refreshSubscribers.forEach((callback) => callback(newAccessToken));
  //     setRefreshSubscribers([]);
  //     setIsRefreshing(false);
  // };

  // const refreshAccessToken = async () => {
  //     try {
  //         const response = await axios.post<{ accessToken: string }>(`${import.meta.env.VITE_API_BASE_URL}/refresh`);
  //         const newAccessToken = response.data.accessToken;
  //         tokenStore.setAccessToken(newAccessToken);
  //         // onRefreshed(newAccessToken);
  //     } catch (error) {
  //         tokenStore.clearAccessToken();
  //         authStore.clearAuth();
  //     }
  // };

  const axiosInstance: AxiosInstance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BASE_URL,
    headers: {
      "Content-Type": "application/json",
    },
    withCredentials: true,
  });

  axiosInstance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      // 새로고침 시 기존 local storage 내부의 access token 확인
      // access token의 존재 유무만 확인하기 인증확인 로직 추가 예정
      const token =
        typeof window !== "undefined"
          ? sessionStorage.getItem("access-token")
          : null;
      if (isAuthenticated && token) {
        config.headers!.Authorization = JSON.parse(token);
      } else if (isAuthenticated && !token) {
        router.push("/login");
      }
      return config;
    },
    (error) => {
      const errorResponse = error as AxiosError;
      console.error(errorResponse);
      return Promise.reject(error);
    },
  );

  axiosInstance.interceptors.response.use(
    (response: AxiosResponse) => response,
    async (error: AxiosError) => {
      if (error.response?.status == 400) {
        alert("잘못된 요청입니다.");
      }
      else if (error.response?.status == 403) {
        alert("권한이 없는 요청입니다.");
        router.push("/");
      }
      else if (error.response?.status == 415) {
        alert("잘못된 파일 형식입니다.");
      }

      return Promise.reject(error);
    }
  );
  // axiosInstance.interceptors.response.use(
  //     (response: AxiosResponse) => response,
  //     async (error) => {
  //         const originalRequest = error.config;
  //         if (error.response?.status === 401 && !originalRequest?._retry) {
  //           // 401이나 그런것일 경우

  //             if (isRefreshing) {
  //                 try {
  //                     const newAccessToken = await new Promise<string>((resolve) => {
  //                         addRefreshSubscriber((newAccessToken) => {
  //                             resolve(newAccessToken);
  //                         });
  //                     });
  //                     originalRequest.headers!.Authorization = newAccessToken;
  //                     return axiosInstance(originalRequest);
  //                 } catch (error) {
  //                     return Promise.reject(error);
  //                 }
  //             }

  //             originalRequest._retry = true;
  //             setIsRefreshing(true);
  //             try {
  //                 await refreshAccessToken();
  //             } catch (error) {
  //                 return Promise.reject(error);
  //             }

  //             originalRequest.headers!.Authorization = tokenStore.accessToken;
  //             return axiosInstance(originalRequest);
  //         }

  //         return Promise.reject(error);
  //     }
  // );

  return axiosInstance;
};
