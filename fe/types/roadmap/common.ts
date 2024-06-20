export interface CheckboxState {
  [key: string]: boolean;
}

export interface PageState {
  pageNumber: number;
  size: number;
  techmapId?: number;
  techCompanyRelativeLevel?: string;
  targetYear?: number;
  techStatus?: string;
  targetStatus?: boolean;
  departmentId?: number;
}

export interface Department {
  id: number;
  data: string;
}

export interface techmapMoveData {
  techmapId: number;
  projectId: number[];
  type: string;
  year?: number;
}
