export interface techmapList {
  techmapId: number;
  targetYear: number;
  description?: string;
  info: string;
  dueDate: string;
  keywords: string[];
  departments: string[];
  createDate: string;
}

export interface techmapRequest {
  techmaps: techmapList[];
  targetYear?: targetYear[];
  totalPages: number;
  totalElements: number;
  currentPage: number;
  size: number;
}
export interface targetYear {
  targetYear: number;
  count: number;
}
export interface techmapProjectForm {
  targetYear: number;
  description: string;
  dueDate: Date;
  keywords: string[];
  departments: number[];
  isAlarmSend: boolean;
}
