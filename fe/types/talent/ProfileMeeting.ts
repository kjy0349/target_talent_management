interface MeetingHeadCreateRequest {
    meetAt: Date;
    isFace: boolean;
    country: string;
    place: string;
    participants: string;
    description: string;
    isNetworking: boolean;
}

interface MeetingRecruitCreateRequest {
    meetAt: Date;
    isFace: boolean;
    country: string;
    place: string;
    isMemberDirected: boolean;
    participants: string;
    currentTask: string;
    leadershipDescription: string;
    interestType: string;
    interestTech: string;
    question: string;
    etc: string;
    description: string;
    targetDepartment: string;
    targetJobRank: string;
    isNetworking: boolean;
}

interface Meeting {
    id: number;
    meetingType: string;
    meetAt: string;
    isFace: boolean;
    country: string;
    place: string;
    participants: string;
    description: string;
    isMemberDirected?: boolean;
    currentTask?: string;
    leadershipDescription?: string;
    interestType?: string;
    interestTech?: string;
    question?: string;
    etc?: string;
    inChargeMemberName?: string;
    targetDepartment?: string;
    targetJobRank?: string;
    isFavorite: boolean;
    memberDepartment: string;
    memberName: string;
    createdAt: string;
    isNetworking: boolean;
}

export type { MeetingRecruitCreateRequest, MeetingHeadCreateRequest, Meeting };