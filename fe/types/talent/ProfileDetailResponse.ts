import {KeywordPreview} from "@/types/admin/Project";

export interface ProfileDetail {
  "profileId": number,
  "profileImage": string,
  "employStatus": string,
  "columnDatas": ProfileColumn[],
  "employmentHistories": EmploymentHistorySummary[],
  "memos": Memo[],
  "projectsPreview": Project[],
  "techmapsPreview": techmap[],
  "careersDetail": Career[],
  "educationsDetail": Education[],
  "keywordsPreview"?: string[],
  "jobRankDetail"?: {
    id: number;
    description: string;
  },
  "memberPreview": {
    "name": string
    "departmentName": string
  },
  "createdAt": string,
  "statusModifiedAt": string,
  "isPrivate": boolean,
  "isAllCompany": boolean
}

export interface EmploymentHistorySummary {
  "type": string,
  "step"?: string,
  "contents": string[],
  "datetime": string
}

export interface Project {
  "projectId": number,
  "title": string,
  "targetYear"?: string,
}

export interface techmap {
  "techmapId": number,
  "targetYear": number,
  "targetDepartmentName": string,
  "techDetailName": string,
  "techMainCategoryName": string,
  "techSubCategoryName": string,
}

export interface CareerCreateRequest {
  companyName: string;
  companyCountryName: string;
  companyRegion: string;
  jobRank: string;
  level: string;
  isManager: boolean;
  employType: string;
  startedAt: Date;
  endedAt?: Date;
  isCurrent: boolean;
  dept: string;
  role: string;
  description: string;
  keywords: string[];
}

export interface Career {
  "id": number,
  "companyName": string,
  "jobRank": string,
  "startedAt"?: string,
  "endedAt"?: string,
  "employType": string,
  "level"?: string,
  "isManager"?: boolean,
  "region"?: string,
  "isCurrent": boolean,
  "dept": string,
  "role": string,
  "description"?: string,
  "country"?: string,
  "careerPeriodMonth": number,
  "keywords": string[]
}

export interface EducationCreateRequest {
  degree: string;
  schoolCountry: string;
  schoolName: string;
  major: string;
  enteredAt?: Date;
  graduatedAt?: Date;
  labName?: string;
  graduateStatus: string;
  labResearchType: string;
  labResearchDescription: string;
  labResearchResult: string;
  labProfessor: string;
  keywords: string[];
}

export interface Education {
  "id": number,
  "degree": string,
  "major": string,
  "enteredAt"?: string,
  "graduatedAt"?: string,
  "graduateStatus": string,
  "school": {
    "id": number,
    "schoolName": string,
    "country": string
  },
  "labResearchType": string,
  "labResearchResult": string,
  "labResearchDescription": string,
  "lab"?: {
    labId: number,
    labName: string,
    labProfessor: string,
  },
  "keywords": string[]
}

export interface ProfileColumn {
  "name": string,
  "label": string,
  "value": string,
  "dataType": string
}

export interface Memo {
  id: number;
  memberId: number;
  memberDepartment: string;
  memberName: string;
  content: string;
  createdAt: string;
  isEditable: boolean;
}