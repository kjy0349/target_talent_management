package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.dto.request.AccountDto;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.infra.repository.KeywordJpaRepository;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.Team;
import com.ssafy.s10p31s102be.member.infra.repository.AuthorityJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.TeamJpaRepository;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapUpdateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.techmapCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.techmapUpdateDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapDepartment;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapKeyword;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapDepartmentJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapKeywordJpaRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TechmapServiceImplTest {
    @Nested
    @SpringBootTest
    @Transactional
    public class techmapServiceImplSpringBootTest {
        @Autowired
        MemberJpaRepository memberRepository;

        @Autowired
        DepartmentJpaRepository departmentRepository;

        @Autowired
        AuthorityJpaRepository authorityRepository;

        @Autowired
        TeamJpaRepository teamRepository;

        @Autowired
        TechmapJpaRepository techmapRepository;

        @Autowired
        TechmapDepartmentJpaRepository techmapDepartmentRepository;

        @Autowired
        TechmapKeywordJpaRepository techmapKeywordRepository;

        @Autowired
        TechmapServiceImpl techmapService;

        private TechmapDepartment mocktechmapDepartment;
        private Member mockMember;
        private TechmapKeyword mocktechmapKeyword;
        private Techmap mocktechmap;
        Integer techmapId;
        Integer memberId;
        Integer departmentId;

        @BeforeEach
        void 초기_인재Pool_연관객체_등록하기(){
            Integer memberId = 1;

            Authority authority = new Authority("채용담당자", 1);
            authorityRepository.save(authority);

            Department department = new Department();
            departmentRepository.save( department );
            departmentId = department.getId();

            Team team = new Team();
            teamRepository.save(team);

            Member member = Member.builder()
                    .name("test")
                    .knoxId("test")
                    .password("test")
                    .department(department)
                    .team(team)
                    .authority(authority)
                    .build();

            memberRepository.save( member );
            memberId = member.getId();


            UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "채용담당자",
                    memberId, 1, departmentId, 1));

            TechmapCreateDto techmapCreateDto = TechmapCreateDto.builder()
                    .departments(List.of(departmentId))
                    .keywords(List.of("생성형 AI", "6G"))
                    .build();

            mocktechmap = techmapService.create(userDetails, techmapCreateDto);

            techmapId = mocktechmap.getId();
        }

        @Test
        void 인재Pool을_등록했을_때_매핑테이블에_정보가_추가된다(){
            assertThat(techmapRepository.findAll().size()).isEqualTo(1);
            assertThat(techmapDepartmentRepository.findAll().size()).isEqualTo(1);
            assertThat(techmapKeywordRepository.findAll().size()).isEqualTo(2);
        }

        @Test
        void 인재Pool을_삭제하면_연관된_매핑테이블에서_정보도_삭제한다(){
            //given
            UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "채용담당자",
                    1, 1, 1, 1));
            Integer memberId = 1;

            //when
            techmapService.delete(userDetails, List.of(techmapId));

            //then
            assertThat(techmapRepository.findById(techmapId).isEmpty()).isEqualTo(true);
            assertThat(techmapDepartmentRepository.findAll().size()).isEqualTo(0);
            assertThat(techmapKeywordRepository.findAll().size()).isEqualTo(0);
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    public class techmapServiceImplMockTest {
        @Mock
        TechmapJpaRepository techmapRepository;
        @Mock
        MemberJpaRepository memberRepository;
        @Mock
        DepartmentJpaRepository departmentRepository;
        @Mock
        KeywordJpaRepository keywordRepository;

        @InjectMocks
        TechmapServiceImpl techmapService;

        private Member mockMember;
        private Department mockDepartment;
        private TechmapDepartment mocktechmapDepartment;
        private TechmapKeyword mocktechmapKeyword;

        @BeforeEach
        void 초기_인재Pool_연관객체_등록하기() {
            mockMember = new Member();
            mockDepartment = new Department();
            mocktechmapDepartment = new TechmapDepartment();
            mocktechmapKeyword = new TechmapKeyword();
        }

        @Test
        void 운영진은_인재Pool을_등록할_수_있다() {
            //given

            UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "채용담당자",
                    1, 1, 1, 1));
            Integer memberId = 1;

            TechmapCreateDto techmapCreateDto = TechmapCreateDto.builder()
                    .targetYear(2024)
                    .departments(List.of(1))
                    .description("Test techmap입니다.")
                    .keywords(List.of("생성형 AI", "6G"))
                    .dueDate(LocalDateTime.now())
                    .isAlarmSend(true)
                    .build();

            when(keywordRepository.findByTypeAndData(any(), any())).thenReturn(Optional.of(new Keyword(KeywordType.techmap, "생성형 AI")));
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
            when(departmentRepository.findById(any())).thenReturn(Optional.of(mockDepartment));

            //when
            Techmap result = techmapService.create(userDetails, techmapCreateDto);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getMember()).isEqualTo(mockMember);
            assertThat(result.getDescription()).isEqualTo("Test techmap입니다.");
            assertThat(result.getTargetYear()).isEqualTo(2024);
            assertThat(result.getIsAlarmSend()).isTrue();
            assertThat(result.getTechmapDepartments().size()).isEqualTo(1);
            assertThat(result.getTechmapKeywords().size()).isEqualTo(2);

            verify(techmapRepository).save(any(Techmap.class));
            verify(memberRepository).findById(memberId);
            verify(departmentRepository).findById(any());
            verify(keywordRepository, times(2)).findByTypeAndData(any(), any());
        }

        @Test
        void 운영진은_자신이_등록한_인재Pool_리스트를_볼_수_있다() {
            //given
            Integer memberId = 1;

            List<Techmap> mocktechmaps = Arrays.asList(new Techmap(), new Techmap());

            when(techmapRepository.findtechmapsByMemberId(memberId)).thenReturn(mocktechmaps);
            //when
            List<Techmap> result = techmapRepository.findtechmapsByMemberId(1);

            //then
            assertThat(result.size()).isEqualTo(2);
            verify(techmapRepository).findtechmapsByMemberId(memberId);
        }

        @Test
        void 운영진은_자신이_등록한_인재Pool을_수정할_수_있다() {
            //given
            UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "채용담당자",
                    1, 1, 1, 1));
            Integer memberId = 1;
            Integer techmapId = 1;

            Techmap mocktechmap = Techmap.builder()
                    .build();

            TechmapUpdateDto techmapUpdateDto = TechmapUpdateDto.builder()
                    .targetYear(2024)
                    .departments(List.of(1))
                    .description("Test Upadte techmap입니다.")
                    .keywords(List.of("Network", "6G"))
                    .dueDate(LocalDateTime.now())
                    .build();

            when(keywordRepository.findByTypeAndData(any(), any())).thenReturn(Optional.of(new Keyword(KeywordType.techmap, "생성형 AI")));
            when(techmapRepository.findById(techmapId)).thenReturn(Optional.of(mocktechmap));
            when(departmentRepository.findById(any())).thenReturn(Optional.of(mockDepartment));
            //when
            Techmap result = techmapService.update(userDetails, techmapId, techmapUpdateDto);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getDescription()).isEqualTo("Test Upadte techmap입니다.");
            assertThat(result.getTargetYear()).isEqualTo(2024);
            assertThat(result.getTechmapDepartments().size()).isEqualTo(1);
            assertThat(result.getTechmapKeywords().size()).isEqualTo(2);

            verify(techmapRepository).findById(techmapId);
            verify(departmentRepository).findById(any());
            verify(keywordRepository, times(2)).findByTypeAndData(any(), any());
        }

        @Test
        void 운영진은_자신이_등록한_인재Pool을_삭제할_수_있다() {
            //given
            Integer techmapId = 1;
            UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "admin",
                    1, 1, 1, 1));

            Techmap mocktechmap = Techmap.builder()
                    .build();

            doNothing().when(techmapRepository).deleteById(any());
            //when
            techmapService.delete(userDetails, List.of(techmapId));

            //then
            verify(techmapRepository).deleteById(any());
        }
    }
}
