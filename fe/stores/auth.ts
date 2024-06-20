import { create } from "zustand";
import { persist } from "zustand/middleware";

import { MemberSummaryResponse } from "@/types/member/member";

interface AuthState {
  id?: number;
  name?: string;
  departmentName?: string;
  teamName?: string;
  authority?: string;
  isSecuritySigned?: boolean;
  changePassword?: boolean;
  email?: string;
  authLevel?: number;
  setAuth: (response: MemberSummaryResponse) => void;
  setEmail: (email: string) => void;
  clearAuth: () => void;
  isCheckedPopup: boolean,
  setIsCheckedPopup: (boolean: boolean) => void;
}

const useAuthStore = create<AuthState>()(
  // persist를 통해 localStorage에 로그인 정보 저장
  persist(
    (set) => ({
      setAuth: (response: MemberSummaryResponse) => {
        set({
          id: response.id,
          name: response.name,
          departmentName: response.departmentName,
          teamName: response.teamName,
          authority: response.authority,
          isSecuritySigned: response.isSecuritySigned,
          changePassword: response.changePassword,
          authLevel: response.authLevel,
        });
      },
      setEmail: (email: string) => {
        set({
          email: email,
        });
      },
      isCheckedPopup: false,
      setIsCheckedPopup: (boolean) => set({ isCheckedPopup: boolean }),
      clearAuth: () => {
        set({
          id: undefined,
          name: undefined,
          departmentName: undefined,
          teamName: undefined,
          authority: undefined,
          isSecuritySigned: undefined,
          changePassword: undefined,
          email: undefined,
          authLevel: undefined,
          isCheckedPopup: false,
        });
      },
    }),
    {
      name: "auth-storage",
    },
  ),
);

export { useAuthStore };
