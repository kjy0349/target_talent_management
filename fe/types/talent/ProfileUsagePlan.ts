interface UsagePlanCreateRequest {
	mainDescription: string;
	detailDescription: string;
	targetEmployDate: Date;
	jobRank: string;
	targetDepartmentName: string;
}

interface UsagePlan {
	id: number;
	mainDescription: string;
	detailDescription: string;
	targetEmployDate: string;
	jobRank: string;
	targetDepartmentName: string;
	memberDepartment: string;
	memberName: string;
	isFavorite: boolean;
	createdAt: string;
}

export type { UsagePlanCreateRequest, UsagePlan };