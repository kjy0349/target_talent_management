interface Specialization {
	id: number;
	specialPoint: string;
	description: string;
	isFavorite: boolean;
	memberDepartment: string;
	memberName: string;
	createdAt: string;
}

interface SpecializationCreateRequest {
	specialPoint: string;
	description: string;
}

export type { Specialization, SpecializationCreateRequest };