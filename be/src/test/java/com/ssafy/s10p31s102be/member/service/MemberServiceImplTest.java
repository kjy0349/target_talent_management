package com.ssafy.s10p31s102be.member.service;

import com.ssafy.s10p31s102be.member.dto.request.MemberLoginDto;
import com.ssafy.s10p31s102be.member.exception.PasswordMismatchException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @Mock
    MemberJpaRepository memberRepository;

    @InjectMocks
    MemberServiceImpl memberService;

    private Member mockMember;

    String knoxId = "test_id";

    @BeforeEach
    void 등록된_멤버_만들기() {
        mockMember = Member.builder()
                .knoxId(knoxId)
                .password("correct_password")
                .build();
    }

    @Test
    void 로그인_성공() {
        //given
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .id(knoxId)
                .password("correct_password")
                .build();

        when(memberRepository.findByKnoxId(knoxId)).thenReturn(Optional.of(mockMember));
        //when
        Member result = memberService.login(memberLoginDto);

        //then
        assertThat(result.getPassword()).isEqualTo(mockMember.getPassword());
    }

    @Test
    void 비밀번호_오류로_인한_로그인_실패(){
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .id(knoxId)
                .password("incorrect_password")
                .build();

        when(memberRepository.findByKnoxId(knoxId)).thenReturn(Optional.of(mockMember));
        //when

        //then
        assertThrows(PasswordMismatchException.class, () -> {
            memberService.login(memberLoginDto);
        });
    }

    @Test
    void 로그인_할_때_마지막_로그인_시간이_업데이트된다(){
        //given
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .id(knoxId)
                .password("correct_password")
                .build();

        when(memberRepository.findByKnoxId(any())).thenReturn(Optional.of(mockMember));
        //when
        Member result = memberService.login(memberLoginDto);

        //then
        assertThat(result.getLastAccessDate()).isNotNull();
    }
}