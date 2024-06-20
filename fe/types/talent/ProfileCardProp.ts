import { UsagePlan } from "./ProfileUsagePlan";

export default interface ProfileCardProp {
  profileId: number;
  profileImage: string;
  columnDatas: {
    [key: string]: string;
  };
  projectsPreview: [
    {
      projectId: number,
      title: string;
      targetYear?: string;
    },
  ];
  techmapsPreview: [
    {
      techmapId: number,
      targetYear: number,
      targetDepartmentName: string,
      techDetailName: string,
      techSubCategoryName: string,
      techMainCategoryName: string,
    }
  ];
  careersPreview: [
    {
      companyName: string;
      role: string;
      startedAt: string;
      endedAt: string;
    },
  ];
  educationsPreview: [
    {
      schoolName: string;
      major: string;
      degree: string;
      enteredAt: string;
      graduatedAt: string;
    },
  ];
  keywordsPreview: [
    {
      data: string;
    },
  ];
  memberPreview: {
    name: string;
    departmentName: string;
  };
  createdAt: string;
  employStatus: string;
  statusModifiedAt: string;
  networking?: [
    {
      category: string;
      executiveName: string;
    },
  ];
  specializationDetails?: [
    {
      specializationId: number;
      specialPoint: string;
      description: string;
    },
  ];

  usagePlans?: UsagePlan[];
  checked?: boolean;
  handleCheck?: () => void;

  handleOnDeleteProject?: (id: number) => void;
  handleOnAddingNetworking?: (id: number) => void;
  handleOnCopyOtherProject?: (id: number) => void;
  handleOnAddingtechmapProject?: () => void;
  handleOnUpdateDetail?: (oldStatus: string, newStatus: string) => void;
  handleReload?: () => void;
}

export interface ProjectProfileCardProp {
  profileId: number;
  profileImage: string;
  columnDatas: {
    [key: string]: string;
  };
  projectsPreview: [
    {
      title: string;
      targetYear?: string;
    },
  ];
  techmapsPreview: [
    {
      techmapId: number,
      targetYear: number,
      targetDepartmentName: string,
      techDetailName: string,
      techSubCategoryName: string,
      techMainCategoryName: string,
    }
  ];
  careersPreview: [
    {
      companyName: string;
      role: string;
      startedAt: string;
      endedAt: string;
    },
  ];
  educationsPreview: [
    {
      schoolName: string;
      major: string;
      degree: string;
      enteredAt: string;
      graduatedAt: string;
    },
  ];
  keywordsPreview: [
    {
      data: string;
    },
  ];
  memberPreview: {
    name: string;
    departmentName: string;
  };
  createdAt: string;
  employStatus: string;
  statusModifiedAt: string;
  networking?: [
    {
      category: string;
      executiveName: string;
    },
  ];
  specializationDetails?: [
    {
      specializationId: number;
      specialPoint: string;
      description: string;
    },
  ];
  usagePlans?: UsagePlan[];
  checked?: boolean;
  handleCheck?: () => void;
  handleReload?: () => void;
  handleOnDeleteProject?: (id: number) => void;
  handleOnAddingNetworking?: (id: number) => void;
  handleOnCopyOtherProject?: (id: number) => void;
  handleOnAddingtechmapProject?: () => void;
  handleOnUpdateDetail?: () => void;
}
