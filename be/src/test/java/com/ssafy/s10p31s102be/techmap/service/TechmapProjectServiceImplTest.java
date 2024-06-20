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
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectDuplicateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectProfileUpdateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.techmapProjectCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.techmapProjectDuplicateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.techmapProjectFindDto;
import com.ssafy.s10p31s102be.techmap.dto.request.techmapProjectProfileUpdateDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectPageDto;
import com.ssafy.s10p31s102be.techmap.dto.response.techmapProjectPageDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.techmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.techmapProjectProfile;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import com.ssafy.s10p31s102be.techmap.infra.enums.MoveStatus;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechMainCategoryJpaRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechmapProjectServiceImplTest {
    @Mock
    TechmapProjectJpaRepository techmapProjectRepository;

    @Mock
    TechmapJpaRepository techmapRepository;

    @Mock
    MemberJpaRepository memberRepository;

    @Mock
    TechMainCategoryJpaRepository techMainCategoryRepository;

    @Mock
    KeywordJpaRepository keywordRepository;

    @Mock
    DepartmentJpaRepository departmentRepository;

    @Mock
    ProfileJpaRepository profileRepository;

    @InjectMocks
    TechmapProjectServiceImpl techmapProjectService;

    private TechmapProject mocktechmapProject;
    private Techmap mocktechmap;
    private Techmap mockNewtechmap;
    private Member mockMember;
    private Member mockManager;
    private Profile mockProfile;
    private TechMainCategory mockTechMainCategory;
    private Department mockDepartment;
    private Keyword mockKeyword;
    private final KeywordType KEYWORD_IDENTIFIER = KeywordType.TECH_SKILL;
    private Pageable pageable;

    @BeforeEach
    void 초기_인재Pool_프로젝트_연관객체_등록하기() throws NoSuchFieldException, IllegalAccessException {
        mocktechmap = Techmap.builder()
                .targetYear(2024)
                .build();
        mockNewtechmap = Techmap.builder()
                .targetYear(2025)
                .build();
        // 채용담당자
        mockMember = Member.builder()
                .name("이싸피")
                .authority(Authority.builder()
                        .authLevel(3)
                        .authName("채용부서장")
                        .build())
                .team(new Team())
                .department(new Department())
                .build();

        Field id = mockMember.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(mockMember, 1);

        // 관리자
        mockManager = Member.builder()
                .name("김싸피")
                .authority(Authority.builder()
                        .authLevel(1)
                        .authName("관리자")
                        .build())
                .team(new Team())
                .department(new Department())
                .build();
        mocktechmapProject = TechmapProject.builder()
                .manager(mockMember)
                .build();

        mockProfile = new Profile();

        mockTechMainCategory = new TechMainCategory();
        mockDepartment = new Department();
        mockKeyword = new Keyword();
        pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "techmapProjectId"));
    }

    @Test
    void 인재Pool_프로젝트를_생성할_수_있다() {
        //given
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "채용담당자",
                1, 4, 1, 1));

        Integer techmapId = 1;

        TechmapProjectCreateDto techmapProjectCreateDto = TechmapProjectCreateDto.builder()
                .techmapId(techmapId)
                .mainCategoryId(1)
                .subCategory("Vision")
                .keyword("6G")
                .techStatus(TechStatus.NEW)
                .description("test")
                .relativeLevel(TechCompanyRelativeLevel.NORMAL)
                .relativeLevelReason("열세인 이유")
                .targetStatus(true)
                .targetMemberCount(15)
                .build();

        when(memberRepository.findById(any())).thenReturn(Optional.of(mockMember));
        when(techmapRepository.findById(any())).thenReturn(Optional.of(mocktechmap));
        when(techMainCategoryRepository.findById(any())).thenReturn(Optional.of(mockTechMainCategory));
        when(keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, "6G")).thenReturn(Optional.of(mockKeyword));
        //when
        TechmapProject result = techmapProjectService.create(userDetails, techmapProjectCreateDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTechCompanyRelativeLevel()).isEqualTo(TechCompanyRelativeLevel.NORMAL);
        assertThat(result.getTechmapProjectMembers().get(0).getMember().getName()).isEqualTo(userDetails.getUsername());
        assertThat(result.getTechmapProjectMembers().get(0).getMember().getName()).isEqualTo(result.getManager().getName());

        verify(techmapProjectRepository).save(any(TechmapProject.class));
        verify(techMainCategoryRepository).findById(any());
    }

    @Test
    void 관리자는_전체_인재Pool_프로젝트를_조회할_수_있다() {
        //given
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("김싸피", "1q2w3e4r", "관리자",
                1, 1, 1, 1));
        TechmapProjectFindDto techmapProjectFindDto = TechmapProjectFindDto.builder()
                .pageNumber(0)
                .size(1)
                .build();
        Pageable pageable = PageRequest.of(techmapProjectFindDto.getPageNumber(), techmapProjectFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));

        Page<TechmapProject> expectedProjects = Mockito.mock(Page.class);
        when(techmapProjectRepository.findtechmapProjects(pageable, null, null,null, null, null, null)).thenReturn(expectedProjects);

        //when
        TechmapProjectPageDto result = techmapProjectService.findTechmapProjects(userDetails, techmapProjectFindDto);

        //then
        assertThat(result).isNotNull();
        verify(techmapProjectRepository).findtechmapProjects(any(),any(),any(), any(), any(), any(), any());
    }

    @Test
    void 채용담당자는_자신이_속한_부서의_인재Pool_프로젝트만_조회할_수_있다() {
        //given
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "채용담당자",
                1, 1, 1, 1));
        TechmapProjectFindDto techmapProjectFindDto = TechmapProjectFindDto.builder()
                .pageNumber(0)
                .size(1)
                .departmentId(1)
                .build();
        Pageable pageable = PageRequest.of(techmapProjectFindDto.getPageNumber(), techmapProjectFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));

        Page<TechmapProject> expectedProjects = Mockito.mock(Page.class);

        when(techmapProjectRepository.findtechmapProjects(pageable, null, null, null, null, null, 1)).thenReturn(expectedProjects);
        //when
        TechmapProjectPageDto result = techmapProjectService.findtechmapProjects(userDetails, techmapProjectFindDto);

        //then
        assertThat(result).isNotNull();
        verify(techmapProjectRepository).findtechmapProjects(pageable, null, null, null, null, null, 1);
    }

    @Test
    void 인재Pool_프로젝트를_업데이트할_수_있다() {
        //given
        Integer techmapProjectId = 1;
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "담당자", 1, 1, 1, 1));

        Integer techmapId = 1;

        TechmapProjectCreateDto techmapProjectCreateDto = TechmapProjectCreateDto.builder()
                .techmapId(techmapId)
                .mainCategoryId(1)
                .subCategory("Vision")
                .keyword("6G")
                .techStatus(TechStatus.NEW)
                .description("test")
                .relativeLevel(TechCompanyRelativeLevel.NORMAL)
                .relativeLevelReason("열세인 이유")
                .targetStatus(true)
                .targetMemberCount(15)
                .build();

        when(techmapProjectRepository.findById(techmapProjectId)).thenReturn(Optional.of(mockTechmapProject));
        when(techMainCategoryRepository.findById(any())).thenReturn(Optional.of(mockTechMainCategory));
        when(keywordRepository.findByTypeAndData(KEYWORD_IDENTIFIER, "6G")).thenReturn(Optional.of(mockKeyword));
        //when
        TechmapProject result = techmapProjectService.update(userDetails, techmapProjectId, techmapProjectCreateDto);

        //then
        assertThat(result).isNotNull();
        verify(techmapProjectRepository).save(any(TechmapProject.class));
    }

    @Test
    void 인재Pool_프로젝트를_삭제할_수_있다() {
        //given
        Integer techmapProjectId = 1;
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "관리자", 1, 1, 1, 1));

        when(techmapProjectRepository.findById(any())).thenReturn(Optional.of(mockTechmapProject));
        doNothing().when(techmapProjectRepository).deleteById(any());

        //when
        techmapProjectService.delete(userDetails, List.of(1));

        //then
        verify(techmapProjectRepository).deleteById(any());
    }

    @Test
    void 인재Pool_프로젝트에_관리되는_인재들을_조회_할_수_있다() {
        //given
        Integer techmapProjectId = 1;

        when(techmapProjectRepository.findById(any())).thenReturn(Optional.of(mockTechmapProject));
        //when
        List<Profile> result = techmapProjectService.findProfiles(techmapProjectId);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    void 인재Pool_프로젝트에_관리할_인재를_추가_할_수_있다(){
        //given
        Integer profileId = 1;
        Integer techmapProjectId = 1;
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "관리자", 1, 1, 1, 1));

        when(techmapProjectRepository.findById(any())).thenReturn(Optional.of(mockTechmapProject));
        when(profileRepository.findById(any())).thenReturn(Optional.of(mockProfile));
        //when
        TechmapProject result = techmapProjectService.updateProfiles(userDetails, techmapProjectId, new TechmapProjectProfileUpdateDto(List.of(profileId)));

        //then
        verify(techmapProjectRepository).save(any(TechmapProject.class));
    }

    @Test
    void 인재Pool_프로젝트를_새로운_인재Pool에_복사할_수_있다(){
        //given
        UserDetailsImpl userDetails = new UserDetailsImpl(new AccountDto("이싸피", "1q2w3e4r", "관리자", 1, 1, 1, 1));

        TechmapProjectDuplicateDto techmapProjectDuplicateDto = new TechmapProjectDuplicateDto(mockNewtechmap.getId(), MoveStatus.DUPLICATE, List.of(1));

        when(techmapRepository.findById(any())).thenReturn(Optional.of(mockNewtechmap));
        when(techmapProjectRepository.findById(any())).thenReturn(Optional.of(mockTechmapProject));

        //when
        techmapProjectService.duplicatetechmapProject(userDetails, TechmapProjectDuplicateDto);

        //then
//        assertThat(result).isNotNull();
//        assertThat(result.getTargetYear()).isEqualTo(mockNewtechmap.getTargetYear());
//        assertThat(result.getManager().getName()).isEqualTo(userDetails.getUsername());

        verify(techmapProjectRepository).save(any(TechmapProject.class));
    }
}