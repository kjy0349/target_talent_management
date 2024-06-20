package com.ssafy.s10p31s102be.member.service;

import com.ssafy.s10p31s102be.admin.dto.request.MemberAdminSearchConditionDto;
import com.ssafy.s10p31s102be.admin.dto.response.MemberSearchResultDto;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.dto.request.ChangePasswordDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberAuthorizationDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberForgotDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberLoginDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberSummaryDto;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    Member login(MemberLoginDto memberLoginDto);

    Member forgotPassword(MemberForgotDto memberForgotDto);

    Member verifyEmail(String id, String authorizationCode);

    List<MemberSummaryDto> searchMembers(UserDetailsImpl userDetails, Integer techmapProjectId, String word);

    Member changePassword(Integer memberId, ChangePasswordDto changePasswordDto);

    MemberSearchResultDto getAllMembersByFilter(UserDetailsImpl userDetails, MemberAdminSearchConditionDto memberAdminSearchConditionDto, Pageable pageable);
}
