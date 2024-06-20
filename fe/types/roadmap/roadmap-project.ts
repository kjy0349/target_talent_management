export interface techmapProjects {
  techmapProjectId: number;
  mainCategory: string; // 대분류
  subCategory: string; // 기술분야
  techDetail: string; // 세부기술
  description: string; // 개술개요 및 필요 역량
  techStatus: string; // 기술 구분
  techCompanyRelativeLevel: string; // 당사수준
  relativeLevelReason: string; // 판단근거
  targetStatus: boolean; // target여부
  targetMemberCount: number; // 확보 목표
  profileSize: number; // 인재 Pool
  techLabSize: number; // techLab
  techCompanySize: number; // techCompany
  managerName: string; // 담당자
}

export interface techmapProjectRequest {
  techmapProjects: techmapProjects[];
  targetYear?: targetYear[];
  totalPages: number;
  totalElements: number;
  currentPage: number;
  size: number;
}
export interface targetYear {
  targetYear: number;
  count: number;
  ids: number[];
}

export interface RequestNewTech {
  techmapId: number;
  mainCategoryId: number; // 대분류
  subCategory: string; // 기술분야
  keyword: string; // 세부기술
  techStatus: string; // 기술 구분
  description: string; // 개술개요 및 필요 역량
  relativeLevel: string; // 당사수준
  relativeLevelReason: string; // 판단근거
  targetStatus: boolean; // target여부
  targetMemberCount: number; // 확보 목표
}

export interface EditNewTech extends RequestNewTech {}

export interface RequestCategories {
  mainCategoryId: number;
  mainCategory: string;
}

export interface MovetechmapProject {
  techmapId: number;
  techMainCategoryId: number;
  moveStatus: string;
}
