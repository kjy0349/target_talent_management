import { useState, useEffect, useCallback } from 'react';
import { useLocalAxios } from '@/app/api/axios';
import { Meeting } from "@/types/talent/ProfileMeeting";
import CustomAddButton from "@/components/common/CustomAddButton";
import { Card } from "flowbite-react";
import { HiOutlineMinusCircle, HiOutlinePencil, HiOutlineStar, HiStar } from "react-icons/hi";
import SpecializationCreateModal from "@/components/profile/register/SpecializationCreateModal";
import MeetingCreateModal from "@/components/profile/register/MeetingCreateModal";

interface MeetingInfoDivProps {
  profileId: number;
}

const InterestTypes = [
  { label: "상", value: "UPPER" },
  { label: "중", value: "MIDDLE" },
  { label: "하", value: "LOWER" }
];

const MeetingInfoDiv = ({ profileId }: MeetingInfoDivProps) => {
  const localAxios = useLocalAxios();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [meetings, setMeetings] = useState<Meeting[]>([]);
  const [selectedMeeting, setSelectedMeeting] = useState<Meeting>();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const response = await localAxios.get<Meeting[]>(`/profile/${profileId}/meeting`)
    response.data.sort((a, b) => {
      if (a.isFavorite && !b.isFavorite) return -1;
      else if (!a.isFavorite && b.isFavorite) return 1;
      else return new Date(b.createdAt).valueOf() - new Date(a.createdAt).valueOf();
    });

    setMeetings(response.data);
  };

  const openModal = (meetingId?: number) => {
    if (meetingId) {
      setSelectedMeeting(meetings.find(x => x.id === meetingId));
    } else {
      setSelectedMeeting(undefined);
    }

    setIsModalOpen(true);
  }

  const closeModal = () => {
    setSelectedMeeting(undefined);
    setIsModalOpen(false);
  }

  const onSubmit = (meeting: Meeting) => {
    fetchData();
  }

  const updateFavorite = async (index: number) => {
    const response = await localAxios.put<Meeting>(`/profile/${profileId}/meeting/${meetings[index].id}/favorite`, !meetings[index].isFavorite);
    fetchData();
  }

  const deleteMeeting = async (index: number) => {
    await localAxios.delete(`/profile/${profileId}/meeting/${meetings[index].id}`);
    const newMeetings = [...meetings];
    meetings.splice(index, 1);
    setMeetings(newMeetings);
  }
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()}`;
  };

  const formatDateWithDay = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()} (${formatDay(date.getDay())})`;
  };

  const formatDay = (day: number) => {
    switch (day) {
      case 0:
        return "일";
      case 1:
        return "월";
      case 2:
        return "화";
      case 3:
        return "수";
      case 4:
        return "목";
      case 5:
        return "금";
      case 6:
        return "토";
      default:
        return "Invalid day";
    }
  };

  return (
    <div className="shadow-md p-10">
      <div className="flex flex-row justify-between items-center border-b-2 mb-6 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold whitespace-nowrap">면담</p>
        </div>

        <CustomAddButton onClick={() => {
          openModal();
        }} />
      </div>
      <div className="flex flex-col gap-4">
        {
          meetings.length <= 0 &&
          <div className="font-bold mx-auto pt-4 pb-2 text-lg">등록된 면담 기록이 존재하지 않습니다.</div>
        }
        {
          meetings.map((meeting, index) => (
            <Card key={meeting.id} className="text-lg">
              <div className="flex justify-between items-center mb-2">
                <button onClick={() => {
                  updateFavorite(index);
                }}>
                  {
                    meeting.isFavorite
                      ? <HiStar className="text-3xl text-yellow-300" />
                      : <HiOutlineStar className="text-3xl" />
                  }
                </button>

                <div className="flex items-center gap-2">
                  <button onClick={() => {
                    if (confirm("정말 삭제하시겠습니까?")) {
                      deleteMeeting(index);
                    }
                  }}>
                    <HiOutlineMinusCircle className="text-3xl text-red-500" />
                  </button>
                  <button onClick={() => {
                    openModal(meeting.id);
                  }}>
                    <HiOutlinePencil className="text-3xl" />
                  </button>
                </div>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="font-bold whitespace-nowrap">면담유형</span>
                <div>{meeting.meetingType === "CURRENT" ? "현업" : "채용"}</div>
                <span className="font-bold whitespace-nowrap">일시</span>
                <div>{formatDateWithDay(meeting.meetAt)}</div>
                <span className="font-bold whitespace-nowrap">방법</span>
                <div>{meeting.isFace ? "대면" : "비대면"}</div>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="font-bold whitespace-nowrap">면담담당</span>
                <div className="whitespace-pre-wrap">{meeting.participants}</div>
                <span className="font-bold whitespace-nowrap">국가/장소</span>
                <div>{meeting.country} / {meeting.place}</div>
              </div>
              {
                meeting.meetingType != "CURRENT" &&
                <>
                  <div className="px-10 flex gap-4 items-start">
                    <span className="font-bold whitespace-nowrap">현재업무</span>
                    <div>{meeting.currentTask}</div>
                  </div>
                  <div className="px-10 flex gap-4 items-start">
                    <span className="font-bold whitespace-nowrap">리 더 십</span>
                    <div>{meeting.leadershipDescription}</div>
                  </div>
                  <div className="px-10 flex gap-4 items-start">
                    <span className="font-bold whitespace-nowrap">당사관심도</span>
                    <div>{InterestTypes.find(x => x.value == meeting.interestType)?.label}</div>
                    <span className="font-bold whitespace-nowrap">관심사업부</span>
                    <div>{meeting.targetDepartment}</div>
                    <span className="font-bold whitespace-nowrap">관심분야</span>
                    <div>{meeting.interestTech}</div>
                    <span className="font-bold whitespace-nowrap">기대직급</span>
                    <div>{meeting.targetJobRank}</div>
                  </div>
                  <div className="px-10 flex gap-4 items-start">
                    <span className="font-bold whitespace-nowrap">질문사항</span>
                    <div>{meeting.question}</div>
                  </div>
                  <div className="px-10 flex gap-4 items-start">
                    <span className="font-bold whitespace-nowrap">기타사항</span>
                    <div>{meeting.etc}</div>
                  </div>
                </>
              }
              <div className="font-bold px-10 whitespace-nowrap">면담 상세내용</div>
              <div className="mx-10 -mt-1 border-2 rounded-md border-gray-400 p-4 whitespace-pre-wrap">
                {meeting.description}
              </div>
              <div className="w-full text-end mt-4 whitespace-nowrap">
                * 등록정보 : {meeting.memberDepartment} {meeting.memberName} 프로
                ({formatDate(meeting.createdAt)})
              </div>
            </Card>
          ))
        }
      </div>

      {isModalOpen &&
        <MeetingCreateModal profileId={profileId} selected={selectedMeeting} onSubmit={onSubmit}
          onClose={closeModal} />}
    </div>
  );
}

export default MeetingInfoDiv;