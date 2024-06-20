//package com.ssafy.s10p31s102be.project.service;
//
//import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
//import com.ssafy.s10p31s102be.member.exception.JobRankNotFoundException;
//import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
//import com.ssafy.s10p31s102be.member.infra.entity.Authority;
//import com.ssafy.s10p31s102be.member.infra.entity.Department;
//import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
//import com.ssafy.s10p31s102be.member.infra.entity.Member;
//import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
//import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
//import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
//import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
//import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
//import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
//import com.ssafy.s10p31s102be.project.dto.request.ProjectCreateDto;
//import com.ssafy.s10p31s102be.project.dto.request.ProjectSearchConditionDto;
//import com.ssafy.s10p31s102be.project.dto.request.ProjectUpdateDto;
//import com.ssafy.s10p31s102be.project.exception.ProjectNotFoundException;
//import com.ssafy.s10p31s102be.project.infra.entity.*;
//import com.ssafy.s10p31s102be.project.infra.repository.*;
//import org.apache.kafka.common.quota.ClientQuotaAlteration;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ProjectServiceImplTest {
//
//    @Nested
//    @SpringBootTest
//    @Transactional
//    public class ProjectServiceImplSpringBootTest{
//        @Autowired
//        ProjectServiceImpl projectService;
//
//        @Autowired
//        ProjectJpaRepository projectRepository;
//
//        @Autowired
//        ProjectMemberJpaRepository projectMemberRepository;
//
//        @Autowired
//        ProjectProfileJpaRepository projectProfileRepository;
//
//        @Autowired
//        ProjectJobRankJpaRepository projectJobRankRepository;
//
//        @Autowired
//        DepartmentJpaRepository departmentRepository;
//
//        @Autowired
//        MemberJpaRepository memberRepository;
//
//        @Autowired
//        ProfileJpaRepository profileRepository;
//
//        @Autowired
//        JobRankJpaRepository jobRankRepository;
//
//        Integer projectId;
//        Integer departmentId;
//        @BeforeEach
//        public void TestSetting(){
//            //given
//            Department department = new Department();
//            departmentRepository.save(department);
//
//            Project project = Project.builder()
//                    .title("Test Project")
//                    .targetDepartment(department)
//                    .responsibleMemberId(1)
//                    .build();
//            projectRepository.save(project);
//            projectId = project.getId();
//            departmentId = department.getId();
//        }
//
//        @Test
//        public void Project를_삭제하면_매핑테이블과_Project만_지워진다(){
//
//            Project project = projectRepository.findById(projectId).orElseThrow( () -> new RuntimeException("테스트간 에러 발생"));
//            Department department = departmentRepository.findById(departmentId).orElseThrow( () -> new RuntimeException("테스트간 에러 발생"));
//            Member member1 = new Member();
//            Member member2 = new Member();
//            memberRepository.saveAll(Arrays.asList(member1,member2));
//            ProjectMember projectMember1 = new ProjectMember(project, member1);
//            ProjectMember projectMember2 = new ProjectMember(project, member2);
//            project.getProjectMembers().addAll(Arrays.asList(projectMember1, projectMember2));
//            projectMemberRepository.saveAll(Arrays.asList(projectMember1, projectMember2));
//
//            Profile profile1 = new Profile( member1, "hi");
//            Profile profile2 = new Profile( member2, "hello");
//            profileRepository.saveAll(Arrays.asList(profile1,profile2));
//            ProjectProfile projectProfile1 = new ProjectProfile(project, profile1);
//            ProjectProfile projectProfile2 = new ProjectProfile(project, profile2);
//            project.getProjectProfiles().addAll(Arrays.asList(projectProfile1, projectProfile2));
//            projectProfileRepository.saveAll(Arrays.asList(projectProfile1, projectProfile2));
//
//            JobRank jobRank1 = new JobRank();
//            JobRank jobRank2 = new JobRank();
//            jobRankRepository.saveAll(Arrays.asList(jobRank1,jobRank2));
//            ProjectJobRank projectJobRank1 = new ProjectJobRank(project, jobRank1);
//            ProjectJobRank projectJobRank2 = new ProjectJobRank(project, jobRank2);
//            project.getTargetJobRanks().addAll(Arrays.asList(projectJobRank1, projectJobRank2));
//            projectJobRankRepository.saveAll(Arrays.asList(projectJobRank1, projectJobRank2));
//
//            // when
//            projectService.delete(1, project.getId());
//
//            // then
//            assertThat(projectMemberRepository.findAll()).isEmpty();
//            assertThat(projectProfileRepository.findAll()).isEmpty();
//            assertThat(projectJobRankRepository.findAll()).isEmpty();
//            assertThat(projectRepository.findById(project.getId())).isEmpty();
//            assertThat(departmentRepository.findById(department.getId())).isPresent();
//            assertThat(profileRepository.findById(profile1.getId())).isPresent();
//            assertThat(jobRankRepository.findById(jobRank1.getId())).isPresent();
//            assertThat(memberRepository.findById(member1.getId())).isPresent();
//        }
//    }
//    @Nested
//    @ExtendWith(MockitoExtension.class)
//    public class ProjectServiceImplMockTest{
//
//        @InjectMocks @Spy ProjectServiceImpl projectService;
//        @Mock ProjectJpaRepository projectRepository;
//        @Mock MemberJpaRepository memberRepository;
//        @Mock ProfileJpaRepository profileRepository;
//        @Mock ProjectMemberJpaRepository projectMemberRepository;
//        @Mock JobRankJpaRepository jobRankRepository;
//        @Mock DepartmentJpaRepository departmentRepository;
//        @Mock ProjectProfileJpaRepository projectProfileRepository;
//        @Mock ProjectJobRankJpaRepository projectJobRankRepository;
//        @Mock ProjectRepositoryCustom projectRepositoryCustom;
//
//        @Test
//        public void create시_projectRepository의_save가_실행된다(){
//            //given
//            Integer memberId = 1;
//            ProjectCreateDto dto = ProjectCreateDto.builder()
//                    .title("테스트")
//                    .targetYear(2023)
//                    .targetMemberCount(5)
//                    .targetCountry("대한민국")
//                    .isPrivate(false)
//                    .projectType(ProjectType.IN_DEPT)
//                    .responsibleMemberId(2)
//                    .projectMembers(List.of(1, 2, 3))
//                    .projectProfiles(List.of(1, 2))
//                    .targetJobRanks(List.of(1, 2))
//                    .targetDepartmentId(1)
//                    .build();
//
//            given(memberRepository.findById(any())).willReturn(Optional.of(new Member()));
//            given(profileRepository.findById(any())).willReturn(Optional.of(new Profile()));
//            given(jobRankRepository.findById(any())).willReturn(Optional.of(new JobRank()));
//            given(departmentRepository.findById(any())).willReturn(Optional.of(new Department()));
//            //when
//            projectService.create(memberId, dto);
//            //then
//            InOrder inOrder = inOrder( memberRepository, profileRepository, jobRankRepository, departmentRepository, projectRepository);
//            inOrder.verify(memberRepository, times(dto.getProjectMembers().size())).findById(any());
//            inOrder.verify(profileRepository, times(dto.getProjectProfiles().size())).findById(any());
//            inOrder.verify(jobRankRepository, times(dto.getTargetJobRanks().size())).findById(any());
//            inOrder.verify(departmentRepository).findById(any());
//            inOrder.verify(projectRepository).save(any());
//        }
//        @Test
//        public void create시_member가_없으면_MemberNotFoundException이_터진다(){
//            //given
//            Integer memberId = 1;
//            ProjectCreateDto dto = ProjectCreateDto.builder()
//                    .title("테스트")
//                    .targetYear(2023)
//                    .targetMemberCount(5)
//                    .targetCountry("대한민국")
//                    .isPrivate(false)
//                    .projectType(ProjectType.IN_DEPT)
//                    .responsibleMemberId(2)
//                    .projectMembers(List.of(1, 2, 3))
//                    .projectProfiles(List.of(1, 2))
//                    .targetJobRanks(List.of(1, 2))
//                    .targetDepartmentId(1)
//                    .build();
//
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.empty());
//            lenient().when(profileRepository.findById(any())).thenReturn(Optional.of(new Profile()));
//            lenient().when(jobRankRepository.findById(any())).thenReturn(Optional.of(new JobRank()));
//            lenient().when(departmentRepository.findById(any())).thenReturn(Optional.of(new Department()));
//
//            //when
//            //then
//            assertThatThrownBy(() -> projectService.create(memberId, dto)).isInstanceOf(MemberNotFoundException.class);
//        }
//        @Test
//        public void create시_profile이_없으면_ProfileNotFoundException이_터진다(){
////given
//            //given
//            Integer memberId = 1;
//            ProjectCreateDto dto = ProjectCreateDto.builder()
//                    .title("테스트")
//                    .targetYear(2023)
//                    .targetMemberCount(5)
//                    .targetCountry("대한민국")
//                    .isPrivate(false)
//                    .projectType(ProjectType.IN_DEPT)
//                    .responsibleMemberId(2)
//                    .projectMembers(List.of(1, 2, 3))
//                    .projectProfiles(List.of(1, 2))
//                    .targetJobRanks(List.of(1, 2))
//                    .targetDepartmentId(1)
//                    .build();
//
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
//            lenient().when(profileRepository.findById(any())).thenReturn(Optional.empty());
//            lenient().when(jobRankRepository.findById(any())).thenReturn(Optional.of(new JobRank()));
//            lenient().when(departmentRepository.findById(any())).thenReturn(Optional.of(new Department()));
//            //when
//            //then
//            assertThatThrownBy(() -> projectService.create(memberId, dto)).isInstanceOf(ProfileNotFoundException.class);
//
//        }
//        @Test
//        public void create시_jobRank가_없으면_JobRankNotFoundException이_터진다(){
////given
//            //given
//            Integer memberId = 1;
//            ProjectCreateDto dto = ProjectCreateDto.builder()
//                    .title("테스트")
//                    .targetYear(2023)
//                    .targetMemberCount(5)
//                    .targetCountry("대한민국")
//                    .isPrivate(false)
//                    .projectType(ProjectType.IN_DEPT)
//                    .responsibleMemberId(2)
//                    .projectMembers(List.of(1, 2, 3))
//                    .projectProfiles(List.of(1, 2))
//                    .targetJobRanks(List.of(1, 2))
//                    .targetDepartmentId(1)
//                    .build();
//
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
//            lenient().when(profileRepository.findById(any())).thenReturn(Optional.of(new Profile()));
//            lenient().when(jobRankRepository.findById(any())).thenReturn(Optional.empty());
//            lenient().when(departmentRepository.findById(any())).thenReturn(Optional.of(new Department()));
//
//            //when
//            //then
//            assertThatThrownBy(() -> projectService.create(memberId, dto) ).isInstanceOf(JobRankNotFoundException.class);
//        }
//        @Test
//        public void create시_Department가_없으면_DepartmentNotFoundException이_터진다(){
////given
//            //given
//            Integer memberId = 1;
//            ProjectCreateDto dto = ProjectCreateDto.builder()
//                    .title("테스트")
//                    .targetYear(2023)
//                    .targetMemberCount(5)
//                    .targetCountry("대한민국")
//                    .isPrivate(false)
//                    .projectType(ProjectType.IN_DEPT)
//                    .responsibleMemberId(2)
//                    .projectMembers(List.of(1, 2, 3))
//                    .projectProfiles(List.of(1, 2))
//                    .targetJobRanks(List.of(1, 2))
//                    .targetDepartmentId(1)
//                    .build();
//
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
//            lenient().when(profileRepository.findById(any())).thenReturn(Optional.of(new Profile()));
//            lenient().when(jobRankRepository.findById(any())).thenReturn(Optional.of(new JobRank()));
//            lenient().when(departmentRepository.findById(any())).thenReturn(Optional.empty());
//            //when
//            //then
//            assertThatThrownBy(() -> projectService.create(memberId, dto) ).isInstanceOf(DepartmentNotFoundException.class);
//
//        }
//        @Test
//        public void hasProjectModifyAuthorization는_admin일시에_true다(){
//            //given
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(1);
//
//            //when
//            boolean result = projectService.hasProjectModifyAuthorization( member, null );
//            //then
//            assertThat( result ).isTrue();
//        }
//        @Test
//        public void hasProjectModifyAuthorization는_프로젝트_담당자_ID와_제공_사용자의_ID가_같으면_true다(){
//            //given
//            Member member = mock(Member.class);
//            given(member.getId()).willReturn(1);
//            Authority authority = mock(Authority.class);
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(3);
//            //when
//            boolean result = projectService.hasProjectModifyAuthorization( member, 1 );
//            //then
//            assertThat( result ).isTrue();
//        }
//
//        @Test
//        public void hasProjectModifyAuthorization는_어드민도_아니고_id도_다르다면_false다(){
//            //given
//            Member member = mock(Member.class);
//            given(member.getId()).willReturn(1);
//            Authority authority = mock(Authority.class);
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(3);
//            //when
//            boolean result = projectService.hasProjectModifyAuthorization( member, 100 );
//            //then
//            assertThat( result ).isFalse();
//        }
//
//        @Test
//        public void copy시_save가_실행되고_원래_객체와_같다(){
//            //given
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            Project project = Project.builder()
//                    .responsibleMemberId(1)
//                    .poolSize(2)
//                    .title("테스트")
//                    .targetCountry("대한민국")
//                    .build();
//
//            given(member.getId()).willReturn(1);
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(3);
//            given(projectRepository.findById(any())).willReturn(Optional.of(project));
//            given(memberRepository.findById(any())).willReturn(Optional.of(member));
//
//            //when
//            Project copyObject = projectService.copy(1, 1);
//            //then
//            assertThat( copyObject.getTitle() ).isEqualTo(project.getTitle() +"의 복사본");
//            assertThat( copyObject.getTargetCountry()).isEqualTo(project.getTargetCountry());
//            assertThat( copyObject.getPoolSize()).isEqualTo(project.getPoolSize());
//
//        }
//        @Test
//        public void copy시_Project가_삭제되어_있으면_ProjectNotFoundException이_터진다(){
//            //given
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            Project project = Project.builder()
//                    .responsibleMemberId(1)
//                    .poolSize(2)
//                    .title("테스트")
//                    .targetCountry("대한민국")
//                    .build();
//
//            lenient().when(member.getId()).thenReturn(1);
//            lenient().when(member.getAuthority()).thenReturn(authority);
//            lenient().when(authority.getAuthLevel()).thenReturn(3);
//            lenient().when(projectRepository.findById(any())).thenReturn(Optional.empty());
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(member));
//
//
//            //when
//            //then
//            assertThatThrownBy(()->projectService.copy(1, 1)).isInstanceOf(ProjectNotFoundException.class);
//
//
//
//        }
//        @Test
//        public void copy시_Member가_삭제되어_있으면_MemberNotFoundException이_터진다(){
//            //given
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            Project project = Project.builder()
//                    .responsibleMemberId(1)
//                    .poolSize(2)
//                    .title("테스트")
//                    .targetCountry("대한민국")
//                    .build();
//
//            lenient().when(member.getId()).thenReturn(1);
//            lenient().when(member.getAuthority()).thenReturn(authority);
//            lenient().when(authority.getAuthLevel()).thenReturn(3);
//            lenient().when(projectRepository.findById(any())).thenReturn(Optional.of(project));
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.empty());
//
//            //when
//            //then
//            assertThatThrownBy(()->projectService.copy(1, 1)).isInstanceOf(MemberNotFoundException.class);
//
//        }
//        @Test
//        public void 조회시_어드민이면_findAll을_호출해서_전부_가져온다(){
//            //given
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            List<Project> projectsAdmin = new ArrayList<>();
//
//            given(memberRepository.findById(any())).willReturn(Optional.of(member));
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(1);
//            given(projectRepository.findAll()).willReturn(projectsAdmin);
//            //when
//
//            List<Project> result = projectService.findAllWithMemberIdWithFilter(1,new ProjectSearchConditionDto());
//            //then
//            assertThat(result).isEqualTo( projectsAdmin );
//            InOrder inOrder = inOrder( memberRepository, projectRepository,projectRepositoryCustom );
//            inOrder.verify( memberRepository ).findById(any());
//            inOrder.verify( projectRepository ).findAll();
//            inOrder.verify( projectRepositoryCustom, times(0)).findAllWithFilter(any(),any(),any(),any(),any());
//        }
//
//        @Test
//        public void 조회시_유저면_findAllWithFilter을_호출해서_전부_가져온다(){
//            //given
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            List<Project> projectsUser = new ArrayList<>();
//
//            given(memberRepository.findById(any())).willReturn(Optional.of(member));
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(3);
//            given(projectRepositoryCustom.findAllWithFilter(any(),any(),any(),any(),any())).willReturn(projectsUser);
//            //when
//
//            List<Project> result = projectService.findAllWithMemberIdWithFilter(1,new ProjectSearchConditionDto());
//            //then
//            assertThat(result).isEqualTo( projectsUser );
//            InOrder inOrder = inOrder( memberRepository, projectRepository,projectRepositoryCustom );
//            inOrder.verify( memberRepository ).findById(any());
//            inOrder.verify( projectRepository, times(0) ).findAll();
//            inOrder.verify( projectRepositoryCustom, times(1)).findAllWithFilter(any(),any(),any(),any(),any());
//        }
//
//        @Test
//        public void findByIdWithProfiles는_정상_조회시_Project를_findByIdWithProfiles를_통해_반환한다(){
//            //given
//            Member member = mock(Member.class);
//            Project project = Project.builder()
//                    .responsibleMemberId(1)
//                    .build();
//            Authority authority = mock(Authority.class);
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(1);
//            given(memberRepository.findById(any())).willReturn(Optional.of(member));
//            given(projectRepository.findByIdWithProfiles(any())).willReturn(Optional.of(project));
//            //when
//            Project result = projectService.findByIdWithProfiles(1,1);
//            //then
//            assertThat( result ).isEqualTo(project);
//            InOrder inOrder = inOrder( memberRepository, projectRepository );
//            inOrder.verify(memberRepository,times(1)).findById(any());
//            inOrder.verify(projectRepository,times(1)).findByIdWithProfiles(any());
//            inOrder.verify(projectRepository,times(0)).findById(any());
//        }
//
//        @Test
//        public void findByIdWithProfiles는_Member가_없으면_MemberNotFoundException이_발생한다(){
//            //given
//            Member member = mock(Member.class);
//            Project project = Project.builder()
//                    .responsibleMemberId(1)
//                    .build();
//            Authority authority = mock(Authority.class);
//            lenient().when(member.getAuthority()).thenReturn(authority);
//            lenient().when(authority.getAuthLevel()).thenReturn(1);
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.empty());
//            lenient().when(projectRepository.findByIdWithProfiles(any())).thenReturn(Optional.of(project));
//            //when
//            //then
//            assertThatThrownBy(() -> projectService.findByIdWithProfiles(1,1)).isInstanceOf(MemberNotFoundException.class);
//
//            }
//        @Test
//        public void findByIdWithProfiles는_Project가_없으면_ProjectNotFoundException이_발생한다(){
//            //given
//            //given
//            Member member = mock(Member.class);
//            Project project = Project.builder()
//                    .responsibleMemberId(1)
//                    .build();
//            Authority authority = mock(Authority.class);
//            lenient().when(member.getAuthority()).thenReturn(authority);
//            lenient().when(authority.getAuthLevel()).thenReturn(1);
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(member));
//            lenient().when(projectRepository.findByIdWithProfiles(any())).thenReturn(Optional.empty());
//            //when
//            //then
//            assertThatThrownBy( () -> projectService.findByIdWithProfiles(1,1)).isInstanceOf(ProjectNotFoundException.class);
//
//        }
//
//        @Test
//        public void updateProfiles는_성공시_profile이_변경된다(){
//            //given
//            Profile profile1 = mock(Profile.class);
//            Profile profile2 = mock(Profile.class);
//            Profile profile3 = mock(Profile.class);
//            Project project = Project
//                    .builder()
//                    .build();
//            ProjectUpdateDto dto = ProjectUpdateDto
//                    .builder()
//                    .projectProfiles(Arrays.stream(new Integer[]{1,3}).toList())
//                    .build();
//            ProjectProfile pp1 = new ProjectProfile(project,profile1);
//            ProjectProfile pp2 = new ProjectProfile(project,profile2);
//            project.getProjectProfiles().add(pp1);
//            project.getProjectProfiles().add(pp2);
//            given(profile1.getId()).willReturn(1);
//            given(profile2.getId()).willReturn(2);
//            given(profileRepository.findById(3)).willReturn(Optional.of(profile3));
//            //when
//            projectService.updateProfiles(project,dto);
//            //then
//
//            List<ProjectProfile> updatedProfiles = project.getProjectProfiles();
//            assertThat(updatedProfiles).hasSize(2);
//
//            List<Profile> profiles = updatedProfiles.stream()
//                    .map(ProjectProfile::getProfile)
//                    .collect(Collectors.toList());
//            assertThat(profiles).containsExactly(profile1, profile3);
//
//            assertThat(project.getPoolSize()).isEqualTo(2);
//        }
//
//        //TODO UpdateProfiles도 고려해야 한다.
//        @Test
//        void updateMembers는_성공하면_Member내용을_수정한다() {
//
//            // given
//            Integer memberId = 1;
//            Integer projectId = 1;
//            ProjectUpdateDto dto = ProjectUpdateDto.builder().build();
//            Project project = Project.builder().build();
//            Member member = mock(Member.class);
//            Authority authority = mock(Authority.class);
//            given(member.getAuthority()).willReturn(authority);
//            given(authority.getAuthLevel()).willReturn(1);
//            given(projectRepository.findById(any())).willReturn(Optional.of(project));
//            given(memberRepository.findById(any())).willReturn(Optional.of(member));
//
//            // when
//            projectService.updateMembers(memberId, projectId, dto);
//
//            // then
//            then(projectRepository).should(times(1)).findById(projectId);
//            then(memberRepository).should(times(1)).findById(memberId);
//            then(projectService).should(times(1)).updateMembers(project, dto);
//        }
//
//        @Test
//        public void updateMembers_변경로직은_성공_시_소속ProfileMember가_바뀐다(){
//            //            updateMembers와 updateJobRanks는 로직 상 같기에 가볍게 흐름만 체크
//            // given
//            Project project = new Project();
//            ProjectUpdateDto dto = new ProjectUpdateDto();
//            dto.setProjectMembers(Arrays.asList(1, 2, 3));
//
//            Member member1 = mock(Member.class);
//            Member member2 = mock(Member.class);
//            Member member3 = mock(Member.class);
//
//            ProjectMember projectMember1 = new ProjectMember(project, member1);
//            ProjectMember projectMember2 = new ProjectMember(project, member2);
//
//            project.getProjectMembers().addAll(Arrays.asList(projectMember1, projectMember2));
//
//            given(memberRepository.findById(1)).willReturn(Optional.of(member1));
//            given(memberRepository.findById(2)).willReturn(Optional.of(member2));
//            given(memberRepository.findById(3)).willReturn(Optional.of(member3));
//
//            // when
//            projectService.updateMembers(project, dto);
//
//            // then
//            assertThat(project.getProjectMembers()).hasSize(3);
//            assertThat(project.getProjectMembers()).extracting("member").containsExactly(member1, member2, member3);
//            assertThat(project.getIsPrivate()).isFalse();
//        }
//
//        @Test
//        public void update_성공시_반환된_project의_필드값은_dto와_같다(){
//
//            //given
//            ProjectUpdateDto dto = ProjectUpdateDto
//                    .builder()
//                    .title("테스트")
//                    .targetYear(1)
//                    .projectMembers(Arrays.stream(new Integer[]{1}).toList())
//                    .isPrivate(false)
//                    .projectType(ProjectType.IN_DEPT)
//                    .responsibleMemberId(1)
//                    .targetCountry("test")
//                    .targetDepartmentId(1)
//                    .targetMemberCount(10)
//                    .build();
//            Project project = Project
//                    .builder()
//                    .title("다른 제목의project")
//                    .build();
//            Member member = mock( Member.class );
//            Authority authority = mock( Authority.class);
//            given(member.getAuthority()).willReturn(authority);
//            given( authority.getAuthLevel()).willReturn(1);
//
//            given(projectRepository.findById(any())).willReturn(Optional.of(project));
//            given(memberRepository.findById(1)).willReturn(Optional.of(member));
//            given(departmentRepository.findById(dto.getTargetDepartmentId())).willReturn(Optional.of(new Department()));
//            //when
//            Project result = projectService.update( 1, 1, dto );
//            //then
//            InOrder inOrder = inOrder(projectRepository, memberRepository, departmentRepository, projectService);
//            Integer memberRepositorySize = 1 + dto.getProjectMembers().size();
//            inOrder.verify(projectRepository).findById(any());
//            inOrder.verify(memberRepository, times( memberRepositorySize)).findById(any());
//            inOrder.verify(departmentRepository).findById(dto.getTargetDepartmentId());
//
//            assertThat(dto.getTitle()).isEqualTo(result.getTitle());
//        }
//
//        @Test
//        public void updateJobRanks는_성공시_Jobranks를_변경한다(){
//            // given
//            Project project = new Project();
//            ProjectUpdateDto dto = new ProjectUpdateDto();
//            dto.setTargetJobRanks(Arrays.asList(1, 2, 3));
//
//            JobRank jobRank1 = mock(JobRank.class);
//            JobRank jobRank2 = mock(JobRank.class);
//            JobRank jobRank3 = mock(JobRank.class);
//
//            ProjectJobRank projectJobRank1 = new ProjectJobRank(project, jobRank1);
//            ProjectJobRank projectJobRank2 = new ProjectJobRank(project, jobRank2);
//
//            project.getTargetJobRanks().addAll(Arrays.asList(projectJobRank1, projectJobRank2));
//
//            given(jobRankRepository.findById(1)).willReturn(Optional.of(jobRank1));
//            given(jobRankRepository.findById(2)).willReturn(Optional.of(jobRank2));
//            given(jobRankRepository.findById(3)).willReturn(Optional.of(jobRank3));
//
//            // when
//            projectService.updateJobRanks(project, dto);
//
//            // then
//            assertThat(project.getTargetJobRanks()).hasSize(3);
//            assertThat(project.getTargetJobRanks()).extracting("jobRank").containsExactly(jobRank1, jobRank2, jobRank3);
//        }
//
//
//    }
//
//
//}