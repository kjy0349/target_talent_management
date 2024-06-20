package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.networking.dto.request.ExecutiveSearchConditionDto;
import com.ssafy.s10p31s102be.admin.dto.request.MemberAdminSearchConditionDto;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> getAllMembersByFilter(MemberAdminSearchConditionDto memberAdminSearchConditionDto, Pageable pageable);

    Page<Executive> getAllExecutiveByFilter(ExecutiveSearchConditionDto executiveSearchConditionDto, Pageable pageable);
}
