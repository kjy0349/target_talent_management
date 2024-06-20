package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.dto.request.*;
import com.ssafy.s10p31s102be.admin.dto.request.networking.NetworkingAdminCreateDto;
import com.ssafy.s10p31s102be.admin.dto.response.*;
import com.ssafy.s10p31s102be.admin.dto.response.PopupMessagePageDto;
import com.ssafy.s10p31s102be.admin.exception.PopupMessageNotFoundException;
import com.ssafy.s10p31s102be.admin.infra.entity.PopupMessage;
import com.ssafy.s10p31s102be.admin.infra.repository.PopupMessageJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.exception.LabNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.SchoolNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.repository.LabJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.SchoolJpaRepository;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectListFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectSummaryDto;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import com.ssafy.s10p31s102be.admin.infra.repository.ProfileAdminSearchRepositoryCustom;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.*;
import com.ssafy.s10p31s102be.member.infra.entity.*;
import com.ssafy.s10p31s102be.member.infra.repository.*;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingProfileJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingRepositoryCustom;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.project.dto.request.ProjectSearchConditionDto;
import com.ssafy.s10p31s102be.project.exception.ProjectNotFoundException;
import com.ssafy.s10p31s102be.project.infra.entity.*;
import com.ssafy.s10p31s102be.project.infra.repository.ProjectJpaRepository;
import com.ssafy.s10p31s102be.project.infra.repository.ProjectRepositoryCustom;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final MemberJpaRepository memberRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final TeamJpaRepository teamRepository;
    private final AuthorityJpaRepository authorityRepository;
    private final RoleJpaRepository roleRepository;
    private final NetworkingJpaRepository networkingRepository;
    private final ProfileJpaRepository profileRepository;
    private final ProjectJpaRepository projectRepository;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final JobRankJpaRepository jobRankRepository;
    private final NetworkingRepositoryCustom networkingRepositoryCustom;
    private final ExecutiveJpaRepository executiveRepository;
    private final ProfileAdminSearchRepositoryCustom profileSearchRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final NetworkingProfileJpaRepository networkingProfileRepository;
    private final PopupMessageJpaRepository popupMessageRepository;
    private final MemberUsageStaticJpaRepository memberUsageStaticRepository;
    private final SchoolJpaRepository schoolRepository;
    private final LabJpaRepository labRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PasswordEncoder passwordEncoder;
    //TODO ValidUtil 만들기
    private void validAdmin(UserDetailsImpl userDetails) {
        if (!userDetails.getAuthorityLevel().equals(1)) {
            throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
        }
    }

    @Override
    public Long getMemberCount(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return memberRepository.selectCount();
    }

    @Override
    public Member createMember(UserDetailsImpl userDetails, MemberAdminCreateDto dto) {
        validAdmin(userDetails);

        Department department = dto.getDepartmentId() == 0 ? null : departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId(), this));
        Team team = dto.getTeamId() == 0 ? null : teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId(), this));
        Role role = dto.getRoleId() == 0 ? null : roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException(dto.getRoleId(), this));
        Authority authority = dto.getAuthorityId() == 0 ? null : authorityRepository.findById(dto.getAuthorityId())
                .orElseThrow(() -> new AuthorityNotFoundException(dto.getAuthorityId(), this));

        Optional<Member> existMember = memberRepository.findByKnoxId(dto.getKnoxId());

        if( existMember.isPresent() ){
            throw new DuplicateMemberException( this );
        }

        Member member = Member.builder()
                .name(dto.getName())
                .profileImage(dto.getProfileImage())
                .knoxId(dto.getKnoxId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .telephone(dto.getTelephone())
                .visitCount(0)
                .lastProfileUpdateDate(LocalDateTime.now())
                .isSecuritySigned(false)
                .mustChangePassword(false)
                .team(team)
                .department(department)
                .authority(authority)
                .role(role)
                .lastModifiedDate(LocalDateTime.now())
                .lastAccessDate(LocalDateTime.now())
                .build();
        member.setModifiedAt(LocalDateTime.now());
        memberRepository.save(member);
        return member;
    }

    @Override
    public MemberSearchResultDto getAllMembersByFilter(UserDetailsImpl userDetails, MemberAdminSearchConditionDto memberAdminSearchConditionDto, Pageable pageable) {
//        validAdmin(userDetails);

        Page<Member> members = memberRepositoryCustom.getAllMembersByFilter( memberAdminSearchConditionDto,pageable );
        System.out.println("searchedMember:" + members.toString());
        return MemberSearchResultDto.fromEntity(members);
    }

    //이 친구는 Service Layer에서 추가적으로 가공해서 반환하기에 Dto를 반환
    @Override
    public List<MemberAdminSummaryDto> getAllMembers(UserDetailsImpl userDetails, Pageable pageable) {
        validAdmin(userDetails);
        List<Member> result = memberRepository.findMembers(pageable); // 삭제회원도 한번에 보내자.
        return result.stream()
                .map(m -> {
                    Integer NetworkingDoneCount = networkingRepository.getCountByMemberIdAndNetworkingStatus(m.getId(), NetworkingStatus.DONE);
                    Integer personalPoolSize = profileRepository.countByManagerId(m.getId());
                    return MemberAdminSummaryDto.fromEntity(m, personalPoolSize, NetworkingDoneCount);
                }).toList();
    }

    @Override
    public List<Member> updateMemberAuthorities(UserDetailsImpl userDetails, List<Integer> memberIds, Integer authorityId) {
        validAdmin(userDetails);
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new AuthorityNotFoundException(authorityId, this));
        List<Member> result = memberIds.stream().map(id -> {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException(id, this));
            member.updateAuthority(authority);
            return member;
        }).toList();

        return result;
    }

    @Override
    public Member updateMember(UserDetailsImpl userDetails, Integer targetId, MemberAdminUpdateDto dto) {
        validAdmin(userDetails);
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId(), this));
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId(), this));
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException(dto.getRoleId(), this));
        Authority authority = authorityRepository.findById(dto.getAuthorityId())
                .orElseThrow(() -> new AuthorityNotFoundException(dto.getAuthorityId(), this));
        Member member = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberNotFoundException(targetId, this));
        member.update(dto, team, role, authority, department);

        memberRepository.save(member);
        return member;
    }

    @Override
    public void deleteMember(UserDetailsImpl userDetails, Integer targetId) {
        validAdmin(userDetails);
        Member member = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberNotFoundException(targetId, this));

        member.delete();
    }

    @Override
    public void deleteMembers(UserDetailsImpl userDetails, MemberAdminDeleteDto dto) {
        validAdmin(userDetails);
        List<Member> members = dto.getMemberIds().stream().map(id -> memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id, this))).toList();
        members.forEach(Member::delete);

    }

    @Override
    public Authority createAuthority(UserDetailsImpl userDetails, AuthorityAdminCreateDto dto) {
        validAdmin(userDetails);

        Optional<Authority> existAuthority = authorityRepository.findByName( dto.getAuthName() );
        if( existAuthority.isPresent() ){
            throw new DuplicateAuthorityException( this );
        }
        Authority authority = Authority.builder()
                .authName(dto.getAuthName())
                .authLevel(dto.getAuthLevel())
                .authDescription(dto.getAuthDescription())
                .build();
        authorityRepository.save(authority);
        return authority;
    }

    @Override
    public List<Authority> getAllAuthorities(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return authorityRepository.findAuthorities();
    }

    @Override
    public Authority updateAuthority(UserDetailsImpl userDetails, Integer authorityId, AuthorityAdminUpdateDto dto) {
        validAdmin(userDetails);
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new AuthorityNotFoundException(authorityId, this));
        authority.update(dto);
        return authority;
    }

    @Override
    public void deleteAuthority(UserDetailsImpl userDetails, Integer authorityId) {
        validAdmin(userDetails);
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new AuthorityNotFoundException(authorityId, this));
        authority.delete();
    }

    @Override
    public Team createTeam(UserDetailsImpl userDetails, TeamAdminCreateDto dto) {
        validAdmin(userDetails);
        Department nullableDepartment = departmentRepository.findById( dto.getDepartmentId() ).orElseGet( () -> null );


        Team team = Team.builder()
                .description(dto.getDescription())
                .name(dto.getName())
                .build();
        teamRepository.save(team);
        return team;
    }

    @Override
    public List<Team> getAllTeams(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return teamRepository.findTeams();
    }

    @Override
    public Team updateTeam(UserDetailsImpl userDetails, Integer teamId, TeamAdminUpdateDto dto) {
        validAdmin(userDetails);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId, this));
        team.update(dto);
//        if( dto.getDepartmentId() != null ){
//            Department department = departmentRepository.findById(dto.getDepartmentId())
//                    .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId(), this));
//            team.updateDepartment( department );
//        }
        return team;
    }

    @Override
    public void deleteTeam(UserDetailsImpl userDetails, Integer teamId) {
        validAdmin(userDetails);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId, this));
        team.delete();

    }

    @Override
    public Department createDepartment(UserDetailsImpl userDetails, DepartmentAdminCreateDto dto) {
        validAdmin(userDetails);

        Optional<Department> existDepartment = departmentRepository.findDepartmentByName( dto.getName() );

        if( existDepartment.isPresent() ){
            throw new DuplicateDepartmentException( this );
        }


        Member manager = dto.getManagerId() == null ?
                null :
                memberRepository.findById(dto.getManagerId())
                        .orElseThrow(() -> new MemberNotFoundException(dto.getManagerId(), this));
        Department department = Department
                .builder()
                .manager(manager)
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        departmentRepository.save(department);
        return department;
    }

    @Override
    public List<Department> getAllDepartments(UserDetailsImpl userDetails) {
//        validAdmin( userDetails );
        return departmentRepository.findDepartments();
    }

    @Override
    public Department updateDepartment(UserDetailsImpl userDetails, Integer departmentId, DepartmentAdminUpdateDto dto) {
        validAdmin(userDetails);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId, this));
        List<Member> members = dto.getMemberIds() != null ? dto.getMemberIds().stream().map(id -> {
            return memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException(id, this));
        }).toList() : null;
        Member manager = dto.getManagerId() == null || dto.getManagerId() == 0 ?
                null :
                memberRepository.findById(dto.getManagerId())
                        .orElseThrow(() -> new MemberNotFoundException(dto.getManagerId(), this));
        department.update(dto, members, manager);
        return department;
    }

    @Override
    public void deleteDepartment(UserDetailsImpl userDetails, Integer departmentId) {
        validAdmin(userDetails);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId, this));
        department.delete();
    }

    @Override
    public Role createRole(UserDetailsImpl userDetails, RoleAdminCreateDto dto) {
        validAdmin(userDetails);
        Role role = Role.builder()
                .description(dto.getDescription())
                .build();

        roleRepository.save(role);
        return role;
    }

    @Override
    public List<Role> getAllRoles(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return roleRepository.findRoles();
    }

    @Override
    public Role updateRole(UserDetailsImpl userDetails, Integer roleId, RoleAdminUpdateDto dto) {
        validAdmin(userDetails);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId, this));
        role.update(dto);
        return null;
    }

    @Override
    public void deleteRole(UserDetailsImpl userDetails, Integer roleId) {
        validAdmin(userDetails);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId, this));
        role.delete();
    }

    @Override
    public List<JobRank> getAllJobRanks(UserDetailsImpl userDetails){
//        validAdmin( userDetails );
        return jobRankRepository.findAll();
    }


    //-----------------프로필---------------------


    @Override
    public List<ProfilePreviewAdminSummaryDto> getAllProfilePreviewByFilter(UserDetailsImpl userDetails, Pageable pageable, ProfileAdminFilterSearchDto profileFilterSearchDto) {
        validAdmin(userDetails);
        List<Profile> searchedProfiles = profileSearchRepository.getAllProfilePreviewByFilter(pageable, profileFilterSearchDto);

        List<ProfilePreviewAdminSummaryDto> searchedDtos = searchedProfiles.stream().map(ProfilePreviewAdminSummaryDto::fromEntity).toList();

        return searchedDtos;
    }

    @Override
    public Integer getProfileCount(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return (int) (profileRepository.count());
    }

    @Override
    public ProfileAdminFilterGraphFullDto getProfileGrapPollSizeata(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        Map<String, Integer> grapPollSizeata = new TreeMap<>();
        List<Profile> profiles = profileRepository.findAll();
        profiles.stream().forEach(profile -> {
            if (profile.getCareers() != null) {
                if (!profile.getCareers().isEmpty()) {
                    log.info(profile.getId() + "::" + String.valueOf(profile.getCareers().get(profile.getCareers().size() - 1).getStartedAt()));
                    LocalDateTime startedAt = profile.getCareers().get(profile.getCareers().size() - 1).getStartedAt();
                    if (startedAt != null) {
                        YearMonth biasDate = YearMonth.from(startedAt);

                        LocalDateTime endedAt = profile.getCareers().get(profile.getCareers().size() - 1).getEndedAt();
                        YearMonth endPoint = endedAt == null ? YearMonth.from(LocalDateTime.now()) : YearMonth.from(endedAt);
                        endPoint = endPoint.plusMonths(1);
                        log.info(biasDate.toString() + "//to:" + endPoint.toString());
                        while (biasDate.isBefore(endPoint)) {
                            String key = biasDate.getYear() + "-" + biasDate.getMonthValue();


                            grapPollSizeata.put(key, grapPollSizeata.getOrDefault(key, 0) + 1);
                            biasDate = biasDate.plusMonths(1);
                        }

                    }
                }

            }
        });
        log.info(grapPollSizeata.toString());
        return ProfileAdminFilterGraphFullDto.builder()
                .grapPollSizeata(grapPollSizeata)
                .build();
    }

    @Override
    public Profile updateProfileExecutive(UserDetailsImpl userDetails, ProfileAdminUpdateExecutiveDto dto) {
        validAdmin(userDetails);

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new ProfileNotFoundException(dto.getProfileId(), this));


        NetworkingAdminCreateDto createDto = NetworkingAdminCreateDto
                .builder()
                .memberId(userDetails.getMemberId())
                .executiveId(dto.getExecutiveId())
                .category("프로젝트 내부 네트워킹 생성")
                .build();
        Executive executive = executiveRepository.findById(dto.getExecutiveId())
                .orElseThrow(() -> new ExecutiveNotFoundException(dto.getExecutiveId(), this));
        Member member = memberRepository.findById(createDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(createDto.getMemberId(), this));
        Networking networking = Networking.builder()
                .networkingStatus(NetworkingStatus.READY_NETWORKING)
                .category(createDto.getCategory())
                .member(member)
                .executive(executive)

                .build();

        networkingRepository.save(networking);
        NetworkingProfile networkingProfile = new NetworkingProfile(networking, profile);
        networking.getNetworkingProfiles().add(networkingProfile);
        return profile;
    }

    @Override
    public List<ProfileAdminSummaryDto> getAllProfiles(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return null;
    }

    @Override
    public Profile updateProfile(Integer profileId, UserDetailsImpl userDetails, ProfileUpdateDto dto) {
        validAdmin(userDetails);
        return null;
    }

    @Override
    public void deleteProfile(Integer profileId, UserDetailsImpl userDetails) {
        validAdmin(userDetails);

    }

    //-------------------- 프로젝트 -----------------------
    @Override
    public Long getProjectsCount(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return projectRepository.count();
    }


    public ProjectFullDto getProjectById(UserDetailsImpl userDetails, Integer projectId ){
        validAdmin( userDetails );
        Project project = projectRepository.findById( projectId ).orElseThrow(() -> new ProjectNotFoundException( projectId,this));
        List<ProfilePreviewDto> pps = project.getProjectProfiles().stream().map(pp -> {
            List<NetworkingProfile> networkings = networkingProfileRepository.findAllByProfile( pp.getProfile() );
            return ProfilePreviewDto.fromEntity( pp.getProfile());
        }).toList();
        return ProjectFullDto.fromEntity(project, pps);
    }

    @Override
    public ProjectListFullDto getAllProjectsWithFilter(UserDetailsImpl userDetails, ProjectSearchConditionDto searchConditionDto, Pageable pageable) {
        //TODO refactoring 필요
        validAdmin(userDetails);
        Page<Project> findResult = projectRepositoryCustom
                .findAllWithFilterByAdmin(
                        userDetails.getMemberId(),
                        searchConditionDto,
                        pageable
                );
        List<Department> departments = departmentRepository.findDepartments();
        List<Integer> targetYears = projectRepository.findAll().stream().map(p -> {
            return p.getTargetYear();
        }).collect(Collectors.toSet()).stream().toList();
        ProjectListFullDto projectListFullDto = ProjectListFullDto.fromEntity(findResult.stream().map(
                ProjectSummaryDto::fromEntity).toList());
        projectListFullDto.setTotalCount(findResult.getTotalElements());
        projectListFullDto.setTotalPages(findResult.getTotalPages());
        return projectListFullDto;
    }

    @Override
    public Project updateProject(UserDetailsImpl userDetails, Integer projectId, ProjectAdminUpdateDto dto) {
        validAdmin(userDetails);
        if (dto.getIsPrivate()) {
            dto.setProjectMembers(Arrays.stream(new Integer[]{dto.getResponsibleMemberId()}).toList());
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));

        project.update(dto);

        // ProjectMember 업데이트
        updateMembers(project, dto);

        //ProjectProfiles 업데이트
        updateProfiles(project, dto);


        updateJobRanks(project, dto);


        // Department 전사면 없다.
        if (dto.getProjectType() != null && dto.getProjectType().equals(ProjectType.IN_DEPT)) {
            Department targetDepartment = departmentRepository
                    .findById(dto.getTargetDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(dto.getTargetDepartmentId(), this));
            project.updateTargetDepartment(targetDepartment);
        }

        return project;
    }

    @Override
    public void deleteProject(UserDetailsImpl userDetails, Integer projectId) {
        validAdmin(userDetails);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));

        // Project 엔티티 삭제
        projectRepository.delete(project);
    }


    @Override
    public Project updateProfiles(UserDetailsImpl userDetails, Integer projectId, ProjectAdminUpdateDto dto) {
        validAdmin(userDetails);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        updateProfiles(project, dto);
        return project;
    }

    @Override
    public void updateProfiles(Project project, ProjectAdminUpdateDto dto) {

        // ProjectProfile 업데이트
        List<Integer> updatedProfileIds = dto.getProjectProfiles();
        if (updatedProfileIds == null) return;
        List<ProjectProfile> existingProjectProfiles = project.getProjectProfiles();

        // 기존 ProjectProfile 중 업데이트되지 않은 항목 제거
        existingProjectProfiles.removeIf(pp -> !updatedProfileIds.contains(pp.getProfile().getId()));

        // 새로운 ProjectProfile 추가
        if (updatedProfileIds != null) {
            updatedProfileIds.forEach(profileId -> {
                if (existingProjectProfiles.stream().noneMatch(pp -> pp.getProfile().getId().equals(profileId))) {
                    Profile profile = profileRepository.findById(profileId)
                            .orElseThrow(() -> new ProfileNotFoundException(profileId, this));
                    ProjectProfile projectProfile = new ProjectProfile(project, profile);
                    project.getProjectProfiles().add(projectProfile);
                }
            });


        }


    }

    @Override
    public Project updateMembers(UserDetailsImpl userDetails, Integer projectId, ProjectAdminUpdateDto dto) {
        validAdmin(userDetails);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        updateMembers(project, dto);
        return project;
    }

    @Override
    public void updateMembers(Project project, ProjectAdminUpdateDto dto) {

//05-03 버그 픽스
        if (dto.getProjectMembers() != null && !dto.getProjectMembers().contains(dto.getResponsibleMemberId())) {
            dto.getProjectMembers().add(dto.getResponsibleMemberId());
        }
        // ProjectMember 업데이트
        List<Integer> updatedMemberIds = dto.getProjectMembers();
        if (updatedMemberIds == null) return;
        List<ProjectMember> existingProjectMembers = project.getProjectMembers();

        // 기존 ProjectMember 중 업데이트되지 않은 항목 제거
        existingProjectMembers.removeIf(pm -> !updatedMemberIds.contains(pm.getMember().getId()));

        // 새로운 ProjectMember 추가
        if (updatedMemberIds != null) {
            updatedMemberIds.forEach(memberId -> {
                if (existingProjectMembers.stream().noneMatch(pm -> pm.getMember().getId().equals(memberId))) {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new MemberNotFoundException(memberId, this));
                    ProjectMember projectMember = new ProjectMember(project, member);
                    project.getProjectMembers().add(projectMember);
                }
            });
        }

        //1명 이상이면 비공개 프로젝트가 아님.
        if (project.getProjectMembers().size() > 1) {
            project.getProjectMembers().forEach(pm -> {
                log.info(pm.getMember().getId() + "");
            });
            project.updateIsPrivate(false);
        }

    }

    @Override
    public void updateJobRanks(Project project, ProjectAdminUpdateDto dto) {
        // ProjectJobRank 업데이트
        List<Integer> updatedJobRankIds = dto.getTargetJobRanks();
        if (updatedJobRankIds == null) return;
        List<ProjectJobRank> existingProjectJobRanks = project.getTargetJobRanks();

        // 기존 ProjectJobRank 중 업데이트되지 않은 항목 제거
        existingProjectJobRanks.removeIf(pjr -> !updatedJobRankIds.contains(pjr.getJobRank().getId()));

        // 새로운 ProjectJobRank 추가
        if (updatedJobRankIds != null) {
            updatedJobRankIds.forEach(jobRankId -> {
                if (existingProjectJobRanks.stream().noneMatch(pjr -> pjr.getJobRank().getId().equals(jobRankId))) {
                    JobRank jobRank = jobRankRepository.findById(jobRankId)
                            .orElseThrow(() -> new JobRankNotFoundException(jobRankId, this));
                    ProjectJobRank projectJobRank = new ProjectJobRank(project, jobRank);
                    project.getTargetJobRanks().add(projectJobRank);
                }
            });
        }

    }

    //-----------------------네트워킹-------------------


    @Override
    public Article createArticle(UserDetailsImpl userDetails, ArticleAdminCreateDto dto) {
        validAdmin(userDetails);
        return null;
    }

    @Override
    public List<Article> getAllArticles(UserDetailsImpl userDetails) {
        validAdmin(userDetails);
        return null;
    }

    @Override
    public Article updateArticle(UserDetailsImpl userDetails, Integer articleId, ArticleAdminUpdateDto dto) {
        validAdmin(userDetails);
        return null;
    }

    @Override
    public void deleteArticle(Integer articleId, UserDetailsImpl userDetails) {
        validAdmin(userDetails);
    }

    //----------------------팝업 메시지--------------------------
    /*
        관리자 팝업 메시지 등록 기능
     */
    @Override
    public PopupMessage createPopupMessage(UserDetailsImpl userDetails, PopupAdminCreateDto popupAdminCreateDto) {
        validAdmin(userDetails);

        Authority authority = authorityRepository.findById(popupAdminCreateDto.getAuthId())
                .orElseThrow(() -> new AuthorityNotFoundException(popupAdminCreateDto.getAuthId(), this));



        return popupMessageRepository.save(PopupMessage.builder()
                .content(popupAdminCreateDto.getContent())
                .isUsed(popupAdminCreateDto.getIsUsed())
                .startDate(popupAdminCreateDto.getStartDate())
                .endDate(popupAdminCreateDto.getEndDate())
                .viewAuthority(authority)
                .build());
    }

    /*
        ToDo: 관리자 팝업메시지 수정 & 삭제 기능 미구현
        팝업 메시지 수정을 위한 팝업메시지 상세보기
     */
    @Override
    public PopupMessageReadDto findPopupMessage(UserDetailsImpl userDetails, Integer popupMessageId) {
        validAdmin(userDetails);
        PopupMessage popupMessage = popupMessageRepository.findById(popupMessageId)
                .orElseThrow(() -> new PopupMessageNotFoundException(popupMessageId, this));

        return PopupMessageReadDto.fromEntity(popupMessage);
    }

    /*
        이전에 작성한 팝업메시지들은 모두 볼 수 있는 기능
     */
    @Override
    public PopupMessagePageDto findPopupMessages(UserDetailsImpl userDetails, PopupMessageFindDto popupMessageFindDto) {
        Pageable pageable = PageRequest.of(popupMessageFindDto.getPageNumber(), popupMessageFindDto.getSize(), Sort.by(Sort.Direction.DESC, "id"));

        Page<PopupMessage> pages = popupMessageRepository.findAll(pageable);
        List<PopupMessageReadDto> popupMessages = pages.getContent().stream()
                .map(PopupMessageReadDto::fromEntity).toList();

        return new PopupMessagePageDto(popupMessages, pages.getTotalPages(), pages.getTotalElements(), pages.getNumber(), pages.getSize());
    }

    @Override
    public PopupMessage updatePopupMessage(UserDetailsImpl userDetails, Integer popupMessageId, PopupAdminCreateDto popupAdminCreateDto) {
        validAdmin(userDetails);

        PopupMessage popupMessage = popupMessageRepository.findById(popupMessageId)
                .orElseThrow(() -> new PopupMessageNotFoundException(popupMessageId, this));
        Authority authority = authorityRepository.findById(popupAdminCreateDto.getAuthId())
                .orElseThrow(() -> new AuthorityNotFoundException(popupAdminCreateDto.getAuthId(), this));

        popupMessage.updatePopupMessage(popupAdminCreateDto.getContent(), popupAdminCreateDto.getIsUsed(), popupAdminCreateDto.getStartDate(), popupAdminCreateDto.getEndDate(), authority);

        popupMessageRepository.save(popupMessage);

        return popupMessage;
    }

    @Override
    public void deletePopupMessage(UserDetailsImpl userDetails, Integer popupMessageId) {
        validAdmin(userDetails);

        popupMessageRepository.deleteById(popupMessageId);
    }

    /*
        일반 유저들이 접속했을 때 팝업메시지를 보여주는 기능(최신 1개의 팝업만 보여준다는 가정)
        자신의 권한에 맞는 팝업 중 가장 최신의 팝업메시지를 보여주도록 구현
    */
    @Override
    public PopupMessageReadDto showPopupMessages(UserDetailsImpl userDetails) {
        Instant now = Instant.now();
        List<PopupMessage> popupMessages = popupMessageRepository.findAll();

        Integer authLevel = userDetails.getAuthorityLevel();
        int size = popupMessages.size()-1;
        for (int i = size; i >= 0; i--) {
            PopupMessage popupMessage = popupMessages.get(i);
            if (popupMessage.getEndDate() == null) {
                if (popupMessage.getIsUsed() && popupMessage.getViewAuthority().getAuthLevel() >= authLevel) {
                    return PopupMessageReadDto.fromEntity(popupMessages.get(i));
                } else{
                    continue;
                }
            }
            Instant popupEndDate = popupMessage.getEndDate().toInstant(ZoneOffset.UTC);
            log.info("현재시간={}", now);
            log.info("마감시간={}", popupEndDate);
            if (popupMessage.getIsUsed() && popupEndDate.isAfter(now) && popupMessage.getViewAuthority().getAuthLevel() >= authLevel){
                return PopupMessageReadDto.fromEntity(popupMessages.get(i));
            }
        }

        return null;
    }

    @Override
    public MemberUsageChangeDto getUsageChanges(UserDetailsImpl userDetails) {
        if( userDetails.getAuthorityLevel() > 1 ){
            throw new InvalidAuthorizationException( userDetails.getMemberId(), this );
        }
        Long memberCount = memberRepository.selectCount();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastWeek = today.minusWeeks(1);
        LocalDate lastMonth = today.minusMonths(1);

        Integer todayId = Integer.valueOf(today.format(formatter));
        Integer yesterdayId = Integer.valueOf(yesterday.format(formatter));
        Integer lastWeekId = Integer.valueOf(lastWeek.format(formatter));
        Integer lastMonthId = Integer.valueOf(lastMonth.format(formatter));

        MemberUsageStatic todayUsage = memberUsageStaticRepository.findById(todayId).orElse(new MemberUsageStatic());
        MemberUsageStatic yesterdayUsage = memberUsageStaticRepository.findById(yesterdayId).orElse(new MemberUsageStatic());
        MemberUsageStatic lastWeekUsage = memberUsageStaticRepository.findById(lastWeekId).orElse(new MemberUsageStatic());
        MemberUsageStatic lastMonthUsage = memberUsageStaticRepository.findById(lastMonthId).orElse(new MemberUsageStatic());

        return new MemberUsageChangeDto(
                todayUsage.getCount() - yesterdayUsage.getCount(),
                todayUsage.getVisitorCounts() - yesterdayUsage.getVisitorCounts(),
                todayUsage.getCount() - lastWeekUsage.getCount(),
                todayUsage.getVisitorCounts() - lastWeekUsage.getVisitorCounts(),
                todayUsage.getCount() - lastMonthUsage.getCount(),
                todayUsage.getVisitorCounts() - lastMonthUsage.getVisitorCounts(),
                todayUsage.getCount(),
                todayUsage.getVisitorCounts(),
                memberCount
        );
    }

    @Override
    public MemberAdminStaticDto getAdminMainPageStatics(UserDetailsImpl userDetails) {
        Long totalMembers = memberRepository.selectCount();
        LocalDate today = LocalDate.now();
        List<String> categories = new ArrayList<>();
        List<Double> revenueData = new ArrayList<>();
        List<Double> revenuePreviousData = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Integer dateId = Integer.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE));
//            System.out.println(dateId);
            MemberUsageStatic usage = memberUsageStaticRepository.findById(dateId).orElse(new MemberUsageStatic());

            categories.add(date.format(formatter));
            revenueData.add(((double) usage.getVisitorCounts() / totalMembers) * 100);
            revenuePreviousData.add(((double) usage.getCount() / totalMembers) * 100);
        }

        int total = revenueData.stream().mapToInt(Double::intValue).sum();
        double percentage = total / 100.0;

        List<MemberAdminStaticSeriesDto> series = List.of(
                new MemberAdminStaticSeriesDto("사용률(당일 방문자수 / 당일 총 회원수) * 100", revenueData, "#1A56DB"),
                new MemberAdminStaticSeriesDto("사용 빈도(당일 접속 횟수 / 당일 총 방문자 수) * 100", revenuePreviousData, "#FDBA8C")
        );

        MemberAdminStaticDataDto dto = new MemberAdminStaticDataDto(total, percentage, categories, series);
        return new MemberAdminStaticDto( dto );
    }

    @Override
    public School createSchool(UserDetailsImpl userDetails, SchoolCreateDto createDto) {
        validAdmin(userDetails);

        Optional<School> existSchool = schoolRepository.findBySchoolName( createDto.getSchoolName() );

        if( existSchool.isPresent() ){
            throw new DuplicateSchoolException( this );
        }

        School school = School.builder()
                .schoolName(createDto.getSchoolName() )
                .country(createDto.getCountry())
                .build();


        return schoolRepository.save( school );
    }

    @Override
    public List<School> getAllSchools(UserDetailsImpl userDetails) {
        validAdmin(userDetails);

        return schoolRepository.findAllNotDeleted();
    }

    @Override
    public School updateSchool(UserDetailsImpl userDetails, Integer schoolId, SchoolUpdateDto dto) {
        validAdmin(userDetails);
        School school = schoolRepository.findById( schoolId )
                .orElseThrow(() -> new SchoolNotFoundException(schoolId, this));

        school.update( dto );
        return school;
    }

    @Override
    public void deleteSchool(UserDetailsImpl userDetails, Integer schoolId) {
        validAdmin(userDetails);
        School school = schoolRepository.findById( schoolId )
                .orElseThrow(() -> new SchoolNotFoundException(schoolId, this));
        school.delete();

        for(Lab lab: school.getLabs() ){
            lab.delete();
        }
    }

    @Override
    public JobRank createJobRank(UserDetailsImpl userDetails, JobRankCreateDto createDto) {

        Optional<JobRank> existJobRank = jobRankRepository.findByDescription( createDto.getDescription() );

        if( existJobRank.isPresent() ){
            throw new DuplicateJobRankException( this );
        }


        JobRank jobRank = JobRank.builder()
                .description( createDto.getDescription())
                .build();
        return jobRankRepository.save( jobRank );
    }

    @Override
    public JobRank updateJobRank(UserDetailsImpl userDetails, Integer jobrankId, JobRankUpdateDto dto) {
        JobRank jobRank = jobRankRepository.findById( jobrankId )
                .orElseThrow( () -> new JobRankNotFoundException(jobrankId, this));
        jobRank.update( dto );
        return jobRank;
    }

    @Override
    public void deleteJobRank(UserDetailsImpl userDetails, Integer jobrankId) {
        JobRank jobRank = jobRankRepository.findById( jobrankId )
                .orElseThrow( () -> new JobRankNotFoundException(jobrankId, this));

        jobRankRepository.deleteById(jobrankId);
    }

    @Override
    public Lab createLab(UserDetailsImpl userDetails, LabAdminCreateDto createDto) {
        School school = schoolRepository.findById( createDto.getSchoolId() )
                .orElseThrow(() -> new SchoolNotFoundException(createDto.getSchoolId(), this));

        Optional<Lab> existLab = labRepository.findLabsByLabNameAndSchoolId( createDto.getLabName(), createDto.getSchoolId() );
        if( existLab.isPresent() ){
            throw new DuplicateLabException( this );
        }

        Lab lab = Lab.builder()
                .labName( createDto.getLabName() )
                .labProfessor( createDto.getLabProfessor() )
                .researchDescription( createDto.getResearchDescription() )
                .researchResult( createDto.getResearchResult() )
                .researchType( createDto.getResearchType() )
                .major( createDto.getMajor() )
                .school(school)
                .build();

        return labRepository.save( lab );
    }


    @Override
    public List<Lab> getAllLabsBySchoolId(UserDetailsImpl userDetails, Integer schoolId) {
        return labRepository.findAllBySchoolIdNotDeleted( schoolId );
    }

    @Override
    public Lab updateLab(UserDetailsImpl userDetails, Integer labId, LabAdminUpdateDto dto) {
        Lab lab = labRepository.findById( labId )
                .orElseThrow(() -> new LabNotFoundException( labId, this) );
        School school = schoolRepository.findById( dto.getSchoolId() )
                .orElseThrow(() -> new SchoolNotFoundException(dto.getSchoolId(), this));
        lab.update( dto , school );
        return lab;
    }

    @Override
    public Lab updateLabSchool(UserDetailsImpl userDetails, Integer labId, LabAdminUpdateDto dto) {
        Lab lab = labRepository.findById( labId )
                .orElseThrow(() -> new LabNotFoundException( labId, this) );
        School school = schoolRepository.findById( dto.getSchoolId() )
                .orElseThrow(() -> new SchoolNotFoundException(dto.getSchoolId(), this));

        lab.updateSchool( school );
        return lab;
    }

    @Override
    public void deleteLab(UserDetailsImpl userDetails, Integer labId) {
        Lab lab = labRepository.findById( labId )
                .orElseThrow(() -> new LabNotFoundException( labId, this) );
        lab.delete();
    }

    @Override
    public List<Lab> getAllLabs(UserDetailsImpl userDetails) {

        return labRepository.findAllNotDeleted();
    }


}
