interface EmploymentHistoryFullCreateRequest {
	type: string;
	step: string;
	description: string;

	// Interview
	meetDate?: Date;
	interviewType?: string;
	place?: string;
	interviewResults?: InterviewResultCreateRequest[];

	// EmploymentHistory
	result?: string;
	targetDepartmentName?: string;
	targetJobRankName?: string;
	executiveName?: string;
	targetEmployDate?: Date;
}

interface EmploymentHistoryCreateRequest {
	type: string;
	step: string;
	description: string;

	result?: string;
	targetDepartmentName?: string;
	targetJobRankName?: string;
	executiveName?: string;
	targetEmployDate?: Date;
}

interface InterviewCreateRequest {
	interviewDegree: string;
	description: string;

	meetDate?: Date;
	interviewType?: string;
	place?: string;
	interviewResults?: InterviewResultCreateRequest[];
}

interface EmploymentHistory {
	id: number;
	type: string;
	step: string;
	description: string;
	isFavorite: boolean;
	memberDepartment: string;
	memberName: string;
	createdAt: string;

	// Interview
	meetDate?: string;
	interviewType?: string;
	place?: string;
	interviewResults?: InterviewResult[];

	// EmploymentHistory
	result?: string;
	targetDepartmentName?: string;
	targetJobRankName?: string;
	executiveName?: string;
	targetEmployDate?: string;
}

interface InterviewResultCreateRequest {
	result: string;
	executiveName: string;
}

interface InterviewResult {
	id: number;
	interviewResultType: string;
	executiveName: string;
}

export type { EmploymentHistoryFullCreateRequest, EmploymentHistory, EmploymentHistoryCreateRequest, InterviewCreateRequest, InterviewResultCreateRequest, InterviewResult };