export interface AutoCompleteInputProps {
  // identifier: "AUTHORITY" | "DEPARTMENT" | "EXECUTIVE" | "JOBRANK" | "MEMBER" | "ROLE" | "TEAM" | "COMPANY" | "LAB" | "SCHOOL" | "COUNTRY" | "TECH_MAIN_CATEGORY";
  identifier: string;
  className?: string;
  onChange?: (item?: AutoCompleteResponse) => void;
  value?: AutoCompleteResponse;
  required?: boolean;
  placeholder?: string;
  isKeyword?: boolean;
  disabled?: boolean;
}

export interface AutoCompleteResponse {
  id: number;
  data: string;
}
