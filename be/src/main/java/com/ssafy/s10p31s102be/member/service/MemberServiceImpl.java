package com.ssafy.s10p31s102be.member.service;

import com.ssafy.s10p31s102be.admin.dto.request.MemberAdminSearchConditionDto;
import com.ssafy.s10p31s102be.admin.dto.response.MemberSearchResultDto;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.infra.repository.OptionsRepositoryImpl;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.dto.request.ChangePasswordDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberForgotDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberLoginDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberSummaryDto;
import com.ssafy.s10p31s102be.member.exception.AuthorizationCodeNotMatchedException;
import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.exception.PasswordMismatchException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.MemberUsageStatic;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberRepositoryCustomImpl;
import com.ssafy.s10p31s102be.member.infra.repository.MemberUsageStaticJpaRepository;
import com.ssafy.s10p31s102be.techmap.exception.TechmapProjectNotFoundException;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectJpaRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberJpaRepository memberRepository;
    private final TechmapProjectJpaRepository techmapProjectRepository;
    private final OptionsRepositoryImpl optionsRepository;
    private final JavaMailSender mailSender;
    private final MemberRepositoryCustomImpl memberRepositoryCustom;
    private final DepartmentJpaRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUsageStaticJpaRepository memberUsageStaticRepository;

    /*
        멤버 로그인 기능
     */
    @Override
    public Member login(MemberLoginDto memberLoginDto) {
        String knoxId = memberLoginDto.getId();
        String password = memberLoginDto.getPassword();

        Member member = memberRepository.findByKnoxId(knoxId)
                .orElseThrow(() -> new MemberNotFoundException(knoxId, this));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException(this);
        }


        member.updateVisitCount();

        LocalDateTime now = LocalDateTime.now();
        Integer TodayKeyValue = Integer.parseInt(now.getYear()+""+ ( now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue() ) +""+ now.getDayOfMonth());

        MemberUsageStatic memberUsageStatic = memberUsageStaticRepository.findById( TodayKeyValue ).orElseGet( () -> new MemberUsageStatic());
        memberUsageStatic.updateCount();

        Integer nowAccessYear = now.getYear();
        Integer nowAccessMonth = now.getMonthValue();
        Integer nowAccessDay = now.getDayOfMonth();

        Integer lastAccessYear = member.getLastAccessDate().getYear();
        Integer lastAccessMonth = member.getLastAccessDate().getMonthValue();
        Integer lastAccessDay = member.getLastAccessDate().getDayOfMonth();

        if( !( lastAccessYear.equals(nowAccessYear) && lastAccessMonth.equals(nowAccessMonth) && lastAccessDay.equals(nowAccessDay) ) ){
            memberUsageStatic.updateVisitors();
        }

        if( memberUsageStatic.getId() == null ){
            memberUsageStatic.setId( TodayKeyValue );
            memberUsageStaticRepository.save( memberUsageStatic );
        }
        member.updateLastAccessDate(LocalDateTime.now());
        memberRepository.save(member);

        return member;
    }

    /*
        비밀번호를 까먹을 시 knoxId를 이용해 인증 코드를 생성하고 메일을 발송
     */
    @Override
    public Member forgotPassword(MemberForgotDto memberForgotDto) {
        String knoxId = memberForgotDto.getKnoxId();

        Member member = memberRepository.findByKnoxId(knoxId)
                .orElseThrow(() -> new MemberNotFoundException(knoxId, this));

        member.updateAuthorizationCode(generateAuthorizationCode());

        memberRepository.save(member);

        sendCertificationEmail(knoxId, member.getName(), member.getAuthorizationCode());

        return member;
    }

    /*
        이메일 인증 기능
        인증 코드를 기반으로 이메일 인증 진행(인증 코드는 1회만 사용 가능하다.)
     */
    @Override
    public Member verifyEmail(String id, String authorizationCode) {
        Member member = memberRepository.findByKnoxId(id)
                .orElseThrow(() -> new MemberNotFoundException(id, this));

        if (!authorizationCode.equals(member.getAuthorizationCode())) {
            throw new AuthorizationCodeNotMatchedException(authorizationCode, this);
        }

        member.updateAuthorizationCode(null);

        return member;
    }

    /*
        담당자 변경을 위해 같은 부서의 사람들만
     */
    @Override
    public List<MemberSummaryDto> searchMembers(UserDetailsImpl userDetails, Integer techmapProjectId, String word) {
        int limitSize = 5;

        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        Member manager = techmapProject.getManager();

        // 운영진보다 아래 권한을 가지고 있는데 변경을 원하는 프로젝트의 부서와 다른 부서의 사람이 접근했다면 처리하지 않는다.
        if (userDetails.getAuthorityLevel() > 2 && !userDetails.getDepartmentId().equals(manager.getDepartment().getId())) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }

        List<Member> members = optionsRepository.findMembersByDepartmentId(word, manager.getDepartment().getId(), limitSize);

        return members.stream().map(MemberSummaryDto::fromEntity).toList();
    }

    /*
        비밀번호 변경 reset 기능
     */
    @Override
    public Member changePassword(Integer memberId, ChangePasswordDto changePasswordDto) {
        String password = changePasswordDto.getPassword();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId, this));

        member.updatePassword(passwordEncoder.encode(password), false);
        memberRepository.save(member);

        return member;
    }

    private String generateAuthorizationCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder authorizationCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = secureRandom.nextInt(10);
            authorizationCode.append(digit);
        }

        return authorizationCode.toString();
    }

    private void sendCertificationEmail(String email, String name, String authorizationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Please certify your email address");
        message.setText(String.format(
                "Dear %S,%n%n" +
                        "We received a request to reset your password. To complete the process, please use the verification code provided below:%n%n" +
                        "Your Verification Code: %s%n%n" +
                        "Please enter this code on the password reset page to proceed. %n%n" +
                        "If you did not request a password reset, please ignore this email or contact our support team for assistance.%n%n" +
                        "Thank you for using our services!%n%n" +
                        "Best regards,%n" +
                        "Samsung Software Academy for Youth Support Team", name, authorizationCode));
        mailSender.send(message);
    }
    @Override
    public MemberSearchResultDto getAllMembersByFilter(UserDetailsImpl userDetails, MemberAdminSearchConditionDto memberAdminSearchConditionDto, Pageable pageable) {
//        validAdmin(userDetails);
        System.out.println(userDetails.getUsername() + " : " + userDetails.getAuthorityLevel() );
        if( userDetails.getAuthorityLevel() > 2 ){
            Department department = departmentRepository.findById( userDetails.getDepartmentId() )
                    .orElseThrow( () -> new DepartmentNotFoundException(userDetails.getDepartmentId(), this));
            memberAdminSearchConditionDto.setDepartmentName( department.getName() );
        }
        Page<Member> members = memberRepositoryCustom.getAllMembersByFilter( memberAdminSearchConditionDto,pageable );
        System.out.println("searchedMember:" + members.toString());
        return MemberSearchResultDto.fromEntity(members);
    }
}
