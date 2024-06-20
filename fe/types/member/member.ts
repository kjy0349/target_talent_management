import { MemberAdminFull } from "../admin/Member";

interface MemberResponse extends MemberSummaryResponse {
  email: string;
  provider: string;
  address: string;
  phoneNumber: string;
}

interface MemberLoginResponse {
  accessToken: string;
  member: MemberSummaryResponse;
}

interface MemberSummaryResponse {
  id: number;
  name: string;
  departmentName: string;
  teamName: string;
  authority: string;
  isSecuritySigned: boolean;
  changePassword: boolean;
  authLevel: number;
  isCheckedPopup: boolean;
}

interface MemberUpdateRequest {
  nickname: string;
  profileImage: string;
  address: string;
  phoneNumber: string;
}

interface MemberSearchResult {
  memberAdminFullDtos: MemberAdminFull[];
  count: number;
  memberIds: number[];
}

export type {
  MemberResponse,
  MemberLoginResponse,
  MemberSummaryResponse,
  MemberUpdateRequest,
  MemberSearchResult,
};
