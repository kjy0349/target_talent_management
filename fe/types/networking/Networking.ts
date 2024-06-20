import { DepartmentAdminSummary, DepartmentSummary } from "../admin/Member";
import { ProfilePreviewAdminSummary } from "../admin/Profile";

export interface NetworkingFilterFull {
  departmentSummaryDtos: DepartmentSummary[];
  categories: string[];
  maxCount: number;
  executiveCount: number;
  profileCount: number;
  categoryCount: number;
}

export interface NetworkingSummary {
  networkingId: number;
  category: string;
  executiveId: number;
  memberId: number;
  responsibleMemberName: string;
  executiveName: string;
  networkingProfilePreviewSummaryDtos: NetworkingProfilePreviewSummary[];
  status: string;
}
export interface NetworkingProfilePreviewSummary {
  profileId: number;
  name: string; //DC.
  column1: string; // DC.
  profileImage: string;
  companyName: string; //Career.company.name
  jobRank: string; // Career.jobrank
  careerStartedAt: Date; // Career.startedAt
  careerEndedAt: Date; // Career.endedAt
  schoolName: string; // School.name
  schoolMajor: string; // School.major
  schoolDegree: string; // School.degree enum
  techDetailName: string; // TechDetail.techDetailName
}
export interface NetworkingSearchCondition {
  targetYear: number;
  departmentId: number;
  category: string | null;
}

export interface NetworkingUpdate {
  networkingId: number;
  category: string | null | undefined;
  memberId: number;
  responsibleMemberName: string;
  executiveId: number;
  executiveName: string;
  networkingStatus: string | null;
  networkingProfileIds: number[];
}

export interface NetworkingCreate {
  networkingId: number;
  executiveId: number;
  executiveName: string;
  category: string;
  memberId: number;
  memberName: string;
}
export interface NetworkingExecutivePreview {
  executiveId: number;
  executiveName: string;
  NetworkingCount: number;
}

export interface NetworkingSearchResult {
  networkingSummaryDtos: NetworkingSummary[];
  count: number;
}
