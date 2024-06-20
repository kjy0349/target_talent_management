package com.ssafy.s10p31s102be.profile.controller;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingCreateRequestDto;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingUpdateRequestDto;
import com.ssafy.s10p31s102be.profile.dto.response.MeetingCombinedDetailDto;
import com.ssafy.s10p31s102be.profile.dto.response.MeetingHeadDetailDto;
import com.ssafy.s10p31s102be.profile.dto.response.MeetingRecruitDetailDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import com.ssafy.s10p31s102be.profile.service.MeetingService;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/{profileId}/meeting")
public class ProfileMeetingController {
    private final MeetingService meetingService;

    @PostMapping
    @Operation(summary = "면담 내용을 생성한다.", description = "면담을 생성한다.\n" +
            "AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 면담만 생성할 수 있다.\n" +
            "채용 담당자일 경우에는, 뷰와 같이 모든 컬럼에 대해서 데이터를 입력하게 되지만\n" +
            "현업/채용부서장일 경우 {meetingType, meetAt, isFace, country, place, description, isNetworking, participants} 컬럼들만 사용한다.")
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId, @RequestBody MeetingCreateRequestDto dto) {
        Meeting meeting = meetingService.create(userDetails, profileId, dto);

        if (meeting.getMeetingType() == MeetingType.RECRUITER) {
            return ResponseEntity.ok().body(MeetingRecruitDetailDto.fromEntity(meeting));
        } else {
            return ResponseEntity.ok().body(MeetingHeadDetailDto.fromEntity(meeting));
        }
    }

    @GetMapping
    @Operation(summary = "면담 내용을 읽어온다.", description = "면담들을 읽어온다.\n" +
            "AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 면담만 읽어온다.")
    public ResponseEntity<List<Object>> getAllMeetingsDetailByProfileId(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "profileId") Integer profileId) {
        List<Meeting> meetings = meetingService.readMeetings(userDetails, profileId);

        List<Object> combinedDetails = new ArrayList<>();

        meetings.forEach(meeting -> {
                    if (meeting.getMeetingType() == MeetingType.RECRUITER) {
                        combinedDetails.add(MeetingRecruitDetailDto.fromEntity(meeting));
                    } else {
                        combinedDetails.add(MeetingHeadDetailDto.fromEntity(meeting));
                    }
                }
        );
        return ResponseEntity.ok().body(combinedDetails);
    }

    @PutMapping("/{meetingId}")
    @Operation(summary = "면담 내용을 수정한다.", description = "면담을 수정한다.\n" +
            "AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 면담만 수정할 수 있다.")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "meetingId") Integer meetingId, @RequestBody MeetingCreateRequestDto dto) {
        Meeting meeting = meetingService.update(userDetails, meetingId, dto);
        if (meeting.getMeetingType() == MeetingType.RECRUITER) {
            return ResponseEntity.ok().body(MeetingRecruitDetailDto.fromEntity(meeting));
        } else {
            return ResponseEntity.ok().body(MeetingHeadDetailDto.fromEntity(meeting));
        }
    }

    @PutMapping("/{meetingId}/favorite")
    @Operation(summary = "면담 즐겨찾기 상태를 업데이트한다.", description = "면담 즐겨찾기 상태를 업데이트한다.\n" +
            "AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 면담 즐겨찾기 상태만 업데이트 가능하다.")
    public ResponseEntity<?> updateFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "meetingId") Integer meetingId, @RequestBody Boolean isFavorite) {
        Meeting meeting = meetingService.updateFavorite(userDetails, meetingId, isFavorite);
        if (meeting.getMeetingType() == MeetingType.RECRUITER) {
            return ResponseEntity.ok().body(MeetingRecruitDetailDto.fromEntity(meeting));
        } else {
            return ResponseEntity.ok().body(MeetingHeadDetailDto.fromEntity(meeting));
        }
    }

    @DeleteMapping("/{meetingId}")
    @Operation(summary = "면담을 삭제한다.", description = "면담을 삭제한다.\n" +
            "AuthorityLevel이 최소 5는 되어야 수행 가능하다. 권한 레벨이 2 이상인경우(Admin, 운영진이 아닌 경우) 자신이 속한 부서 프로필의 면담만 삭제할 수 있다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(name = "meetingId") Integer meetingId) {
        meetingService.delete(userDetails, meetingId);
        return ResponseEntity.ok().build();
    }
}
