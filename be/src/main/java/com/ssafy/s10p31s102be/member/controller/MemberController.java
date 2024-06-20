package com.ssafy.s10p31s102be.member.controller;

import com.ssafy.s10p31s102be.admin.dto.request.MemberAdminSearchConditionDto;
import com.ssafy.s10p31s102be.admin.dto.response.MemberSearchResultDto;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.service.JwtService;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.dto.request.ChangePasswordDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberAuthorizationDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberForgotDto;
import com.ssafy.s10p31s102be.member.dto.request.MemberLoginDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberSummaryDto;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "멤버")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "멤버 로그인", description = "멤버 로그인 과정 확인")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> memberLogin(@RequestBody MemberLoginDto memberLoginDto) {
        Member member = memberService.login(memberLoginDto);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            member.getKnoxId(),
                            member.getPassword()
                    )
            );

            log.info("authentication 객체 생성 확인 = {}", authentication.toString());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            List<String> tokens = jwtService.generateToken(member.getKnoxId());
            Map<String, Object> map = new HashMap<>();
            map.put("member", MemberSummaryDto.fromEntity(member));
            map.put("accessToken", "Bearer " + tokens.get(0));

            log.info("authentication 객체 ContextHolder 저장 확인 = {}", SecurityContextHolder.getContext().getAuthentication().getName());
            return ResponseEntity.ok().body(map);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /*
        knox id 기준으로 비밀번호를 변경하기 위해 knox id를 받고 해당 email로 인증 코드 발송
     */
    @Operation(summary = "비밀번호 변경 email 인증 발송", description = "knox id 기준으로 비밀번호를 변경하기 위해 knox id를 받고 해당 email로 인증 코드 발송")
    @PostMapping("/forgot")
    public ResponseEntity<Void> forgotPassword(@RequestBody MemberForgotDto memberForgotDto) {
        memberService.forgotPassword(memberForgotDto);

        return ResponseEntity.ok().build();
    }

    /*
        이메일 확인 후 인증 코드를 발송하면 이메일 인증 진행
        인증 실패 시 자격 증명 실패 메시지 발송
     */
    @Operation(summary = "이메일 인증 진행", description = "이메일 확인 후 인증 코드를 발송하면 이메일 인증 진행\n" +
            "인증 실패 시 자격 증명 실패 메시지 발송")
    @GetMapping("{knoxId}/verify")
    public ResponseEntity<Void> verifyEmail(@PathVariable("knoxId") String knoxId, @RequestParam("authorizationCode") String authorizationCode, HttpSession session) {
        Member member = memberService.verifyEmail(knoxId, authorizationCode);
        session.setAttribute("memberId", member.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 변경", description = "새로운 비밀번호 변경")
    @PutMapping("/change-password")
    public ResponseEntity<Void> chagePassword(@RequestBody ChangePasswordDto changePasswordDto, HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("memberId");
        if (memberId == null) {
            session.invalidate();
            throw new InvalidAuthorizationException(null, this);
        }

        // service 처리 중 예외가 발생하면 잡아서 다시 던지기
        try {
            memberService.changePassword(memberId, changePasswordDto);
        } catch(Exception e){
            session.invalidate();
            throw e;
        }
        // 세션 무효화
        session.invalidate();

        return ResponseEntity.ok().build();
    }

    /*
        담당자 변경을 위한 같은 사업부에 속한 인원 검색 기능
     */
    @Operation(summary = "신규 기술 분야 내 담당자 변경을 위한 검색", description = "담당자 변경을 위한 같은 사업부에 속한 인원 검색 기능")
    @GetMapping("/search/{techmapProjectId}")
    public ResponseEntity<List<MemberSummaryDto>> searchMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable("techmapProjectId") Integer techmapProjectId,
                                                               @RequestParam(value = "word", required = false) String word) {
        return ResponseEntity.ok().body(memberService.searchMembers(userDetails, techmapProjectId, word));
    }
    @Operation(summary = "멤버 검색", description = "사용자를 필터 기반으로 검색하는 로직")
    @PostMapping("/search")
    public ResponseEntity<MemberSearchResultDto> getMembersWithFilters(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody( required = false ) MemberAdminSearchConditionDto memberAdminSearchConditionDto, @RequestParam( defaultValue = "0",name="page" ) Integer page, @RequestParam( defaultValue = "4", name="size" ) Integer size ){
        Pageable pageable = PageRequest.of( page, size );
        MemberSearchResultDto dtoList= memberService.getAllMembersByFilter( userDetails, memberAdminSearchConditionDto, pageable );
        return ResponseEntity.ok( dtoList );
    }
}