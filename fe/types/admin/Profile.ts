import { NetworkingExecutivePreview } from "../networking/Networking";
import ProfileCardProp from "../talent/ProfileCardProp";
import { JobRankAdminSummary } from "./Member";
import {
  CareerPreview,
  EducationPreview,
  KeywordPreview,
  MemberPreview,
  ProjectPreview,
  ProjectProfilePreview,
} from "./Project";

export interface ProfileAdminFilterSearch {
  names: string;
  column2: string[];
  jobRanks: string[];
  companyNames: string[];
  careerStartedAt: Date | null;
  careerEndedAt: Date | null;
  degrees: string[];
  schoolNames: string[];
  majors: string[];
  keywords: string[];
  graduatedAt: Date | null;
  employStatuses: string[];
  foundDeptNames: string[];
  founderNames: string[];
  networkingResponsibleMemberNames: string[];
}
export interface ProfilePreviewAdminSummary {
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

export enum Degree {
  pPollSize = "pPollSize",
  MASTER = "MASTER",
  BACHELOR = "BACHELOR",
}

export enum EmployStatus {
  FOUND = "FOUND", // 발굴 완료
  USAGE_REVIEW = "USAGE_REVIEW", // 활용도 검토
  CONTACT = "CONTACT", // 접촉
  SHORT_TERM_MANAGE = "SHORT_TERM_MANAGE", // 단기관리
  MID_LONG_TERM_MANAGE = "MID_LONG_TERM_MANAGE", // 중/장기 관리
  인터뷰2 = "인터뷰2", //  인터뷰
  인터뷰3 = "인터뷰3", // interview3
  for_interview = "for_interview", // 인터뷰 4
  CHECK_B = "CHECK_B", // CHECK_B
  APPROVE_E = "APPROVE_E", // APPROVE_E
  NEGOTIATION = "NEGOTIATION", // 처우 협의
}
export interface ProfileAdminFilterGraphFull {
  grapPollSizeata: {
    [key: string]: number;
  };
}

export interface ProfileExternalSearchCondition {
  searchString: string;
}

export interface ProfileExternalSearchFull {
  profileList: ProfileCardProp[];
  maxCount: number;
  maxPage: number;
}

export interface ProfilePreview {
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
