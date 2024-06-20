import { create } from "zustand";
import { persist } from 'zustand/middleware'

interface TokenState {
  accessToken?: string;
  setAccessToken: (accessToken: string) => void;
  clearAccessToken: () => void;
}

const useTokenStore = create<TokenState>()(
  persist(
    (set) => ({
      setAccessToken: (accessToken: string) => {
        set({
          accessToken
        });
      },
      clearAccessToken: () => {
        set({
          accessToken: undefined
        });
      }
    }),
    {
      name: 'access-token'
    }
  )
);

export { useTokenStore };
