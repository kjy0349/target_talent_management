import {
  DepartmentAdminSummary,
  JobRankAdminSummary,
  MemberAdminSummary,
} from "./Member";
import { NetworkingExecutivePreview } from "../networking/Networking";
import ProfileCardProp from "../talent/ProfileCardProp";

export interface ProjectSummary {
  id: number;
  title: string;
  poolSize: number;
  targetJobRanks: string[];
  targetMemberCount: number;
  responsibleMemberName: string;
  targetYear: number;
  projectType: string;
  isBookMarked: boolean;
  departmentName: string;
  createAt: Date;
  isPrivate: boolean;
}
export interface ProjectSearchCondition {
  countryName: string | undefined;
  departmentId: number | undefined;
  targetYear: number | undefined;
  orderBy: string | undefined;
  keyword: string | undefined;
  memberId: number | undefined;
  isPrivate: Boolean | undefined;
  projectType: string | undefined;
  targetCountry: string | undefined;
  targetJobRankId: number | undefined;
}

export interface ProjectMemberListSummary {
  memberId: number;
  name: string;
}
export interface ProjectFilterFull {
  memberList: ProjectMemberListSummary[];
  departmentAdminSummaryDtos: DepartmentAdminSummary[];
  jobRankAdminSummaryDtos: JobRankAdminSummary[];
  targetCountries: string[];
  targetYears: number[];
}
export interface ProjectListFull {
  projectSummaryDtos: ProjectSummary[];
  targetYears: number[];
  departmentAdminSummaryDtos: DepartmentAdminSummary[];
  totalCount: number;
  totalPages: number;

  targetCountries: string[];
  projectStaticsFullDto: ProjectStaticsFull;
}
export interface ProjectCreate {
  title: string;
  description: string;
  targetMemberCount: number;
  targetYear: number;
  isPrivate: boolean;
  projectType: string;
  projectProfiles: number[];
  targetCountry: string;
  responsibleMemberId: number;
  projectMembers: ProjectMemberSummary[];
  targetJobRanks: number[];
  targetDepartmentId: number;
}

export interface ProjectMemberSummary {
  memberId: number;
  name: string;
  profileImage: string;
}

export interface ProjectDepartmentSummary {
  departmentId: number;
  name: string;
}
export interface ProjectUpdate {
  title?: string;
  descripton?: string;
  targetMemberCount?: number;
  targetYear?: number;
  isPrivate?: boolean;
  projectType?: string;
  targetCountry?: string;
  responsibleMemberId?: number;
  projectMembers?: number[];
  projectProfiles?: number[];
  targetJobRanks?: number[];
  targetDepartmentId?: number;
}

export interface ProjectFull {
  title: string;
  description?: string;
  targetMemberCount: number;
  targetYear: number;
  isPrivate: boolean;
  projectType: string;
  targetCountry: string;
  responsibleMemberId: number;
  projectMembers: ProjectMemberSummary[];
  targetJobRanks: string[];
  targetJobRanksIds: number[];
  projectDepartment: ProjectDepartmentSummary;
  projectProfilesPreviewDtos: ProfileCardProp[];
  poolSize: number;
  filteredArray: number[];
}

export interface ProjectSearchResult {
  projectAdminFullDtos: ProjectFull[];
  count: number;
}

export interface ProjectProfilePreview {
  profileId: number;
  profileImage: string;
  // columnDatas: { key: { value: string } }[];
  columnDatas: { name: string; column1: string; column1: string };
  projectsPreview: ProjectPreview[];
  careersPreview: CareerPreview[];
  employstatus: string;
  educationsPreview: EducationPreview[];
  keywordsPreview: KeywordPreview[];
  memberPreview: MemberPreview | null;
  networkingExecutivePreview: NetworkingExecutivePreview;
  createdAt: Date;
}

export interface ProjectPreview {
  title: string;
  targetYear: number;
}

export interface CareerPreview {
  companyName: string;
  role: string;
  startedAt: Date;
  endedAt: Date;
}

export interface EducationPreview {
  schoolName: string;
  major: string;
  degree: Degree;
  enteredAt: Date;
  graduatedAt: Date;
}

export interface KeywordPreview {
  data: string;
}

export interface MemberPreview {
  name: string;
  departmentName: string;
}

export enum Degree {
  pPollSize,
  MASTER,
  BACHELOR,
}

export interface ProjectProfileFilterSearch {
  column2: string[];
  jobRanks: string[];
  companyNames: string[];
  degrees: string[];
  schoolNames: string[];
  departmentNames: string[];
  founderNames: string[];
}

export interface ProjectUpdateBookmark {
  isBookMarked: Boolean;
}

export interface ProjectStaticsFull {
  projectMemberStatics: Record<number, number>;
  isPrivateStatics: Record<string, number>;
  targetYearStatics: Record<number, number>;
  targetDepartmentStatics: Record<number, number>;
  targetJobRanksStatics: Record<number, number>;
  projectTypeStatics: Record<string, number>;
  targetCountryStatics: Record<string, number>;
}
