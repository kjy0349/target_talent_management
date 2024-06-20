package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.Team;
import com.ssafy.s10p31s102be.member.infra.repository.AuthorityJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.TeamJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInitService {
    private final MemberJpaRepository memberRepository;

    private final AuthorityJpaRepository authorityRepository;

    private final DepartmentJpaRepository departmentRepository;

    private final ProfileJpaRepository profileRepository;

    private final TeamJpaRepository teamRepository;
    //
//    @PostConstruct
//    @Transactional
    public void insertMember(){
        // 권한 생성
        Authority authority = Authority.builder()
                .authName("admin")
                .authLevel(1)
                .build();
        authorityRepository.save(authority);

        // 발굴 담당자 생성
        Department department = new Department("DX" + 1, "테스트 DX" + 1 + " 부서입니다.");
        departmentRepository.save(department);
        Team team = new Team("people", "인재", null);
        teamRepository.save(team);
        Member member = Member.builder()
                .knoxId("wntjrdbs@gmail.com")
                .password("1q2w3e4r!")
                .name("테스트 발굴 담당자" + 1)
                .department(department)
                .team(team)
                .authority(authority)
                .build();

        memberRepository.save(member);
    }
}
