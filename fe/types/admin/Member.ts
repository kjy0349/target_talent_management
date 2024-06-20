export interface DepartmentAdminSummary {
  departmentId: number;
  name: string;
  description: string;
  managerName: string;
}
export interface DepartmentAdminFull {
  departmentId: number;
  name: string;
  description: string;
  managerName: string;
  managerId: number;
}

export interface DepartmentSummary {
  departmentId: number;
  name: string;
  description: string;
  managerName: string;
}
export interface AuthorityAdminSummary {
  id: number;
  authName: string;
  authDescription: string;
  authLevel: number;
}

export interface TeamAdminSummary {
  teamId: number;
  name: string;
  description: string;
}

export interface TeamAdminFull {
  teamId: number;
  name: string;
  description: string;
  departmentName: string;
  departmentId: number;
}
export interface RoleAdminSummary {
  roleId: number;
  description: string;
}

export interface MemberAdminSummary {
  id: number;

  name: string;

  departmentName: string;

  profileImage: string;

  knoxId: string;

  telephone: string;

  visitCount: number;

  lastAccessDate: Date;

  createdAt: Date;

  isSecuritySigned: boolean;

  managePoolSize: number;

  completedNetworks: number;
}

export interface MemberAdminCreate {
  name: string;

  password: string;

  departmentName: string;

  profileImage: string;

  knoxId: string;

  telephone: string;

  departmentId: number;

  teamId: number;

  roleId: number;

  authorityId: number;
}

export interface JobRankAdminSummary {
  id: number;
  description: string;
}

export interface ExecutiveAdminSummary {
  executiveId: number;

  name: string;

  department: string;

  jobRank: string;

  email: string;
}
export interface MemberAdminSearchCondition {
  keyword: string;
  departmentName: string;
  withDelete: boolean;
}
export interface MemberAdminFull {
  id: number;

  name: string;

  profileImage: string;

  departmentName: string;

  teamName: string;

  roleName: string;

  knoxId: string;

  authority: string;

  createdAt: Date;

  visitCount: number;

  lastAccessDate: Date;
  isSecuritySigned: boolean;

  lastDeletedAt: Date;
}

export interface UpdateMemberAuthorityDto {
  memberIds: number[];
  authorityId: number;
}

export interface ExecutiveSearchResult {
  executiveAdminSummaryDtos: ExecutiveAdminSummary[];
  count: number;
}

export interface MemberUsageChange {
  countChangeYesterday: number;
  visitorChangeYesterday: number;
  countChangeLastWeek: number;
  visitorChangeLastWeek: number;
  countChangeLastMonth: number;
  visitorChangeLastMonth: number;
  totalCount: number;
  totalVisitorCount: number;
  totalMembers: number;
}

export interface SchoolFull {
  id: number;
  schoolName: string;
  country: string;
}
