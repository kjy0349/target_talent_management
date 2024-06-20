package com.ssafy.s10p31s102be.project.service;

import com.ssafy.s10p31s102be.admin.dto.response.JobRankAdminSummaryDto;
import com.ssafy.s10p31s102be.admin.dto.response.member.DepartmentAdminSummaryDto;
import com.ssafy.s10p31s102be.common.exception.InternalServerException;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.util.ExcelUtil;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.project.dto.request.*;
import com.ssafy.s10p31s102be.project.dto.response.*;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
import com.ssafy.s10p31s102be.member.exception.JobRankNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;

import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.project.exception.ProjectNotFoundException;
import com.ssafy.s10p31s102be.project.infra.entity.*;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.project.infra.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectJpaRepository projectRepository;
    private final MemberJpaRepository memberRepository;
    private final ProfileJpaRepository profileRepository;
    private final ProjectMemberJpaRepository projectMemberRepository;
    private final JobRankJpaRepository jobRankRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final ProjectProfileJpaRepository projectProfileRepository;
    private final ProjectJobRankJpaRepository projectJobRankRepository;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final NetworkingProfileJpaRepository networkingProfileRepository;
    private final ExecutiveJpaRepository executiveRepository;
    private final NetworkingJpaRepository networkingRepository;
    private final ProjectBookrmarkJpaRepository projectBookrmarkRepository;
    private final ExcelUtil excelUtil;

    //TODO update를 제네릭을 활용해서 공통 메서드로 뺄 수 있을까?
    @Override
    public Project createProject(UserDetailsImpl userDetails, ProjectCreateDto dto) {

        Project project = Project.builder()
                .title(dto.getTitle())
                .targetYear(dto.getTargetYear())
                .targetMemberCount(dto.getTargetMemberCount())
                .targetCountry(dto.getTargetCountry())
                .isPrivate(dto.getIsPrivate())
                .projectType(dto.getProjectType())
                .description(dto.getDescription())
                //사업부 프로젝트( 개인 포함 )은 null일 수 없고 전사는 null일 수 있다.
                .responsibleMemberId(
                        userDetails.getMemberId())
                .build();
        if( !dto.getProjectMembers().contains( userDetails.getMemberId() ) ){
            dto.getProjectMembers().add( userDetails.getMemberId());
        }

        dto.getProjectMembers().stream().forEach( pmemberId -> {
            Member member = memberRepository.findById(pmemberId)
                    .orElseThrow( () -> new MemberNotFoundException(pmemberId, this));
            ProjectMember projectMember = new ProjectMember(project,member);
            project.getProjectMembers().add(projectMember);
        });
//
//        dto.getProjectProfiles().stream().forEach( profileId -> {
//            Profile profile = profileRepository.findById( profileId )
//                    .orElseThrow( () -> new ProfileNotFoundException(profileId,this));
//            ProjectProfile projectProfile = new ProjectProfile( project, profile );
//            project.getProjectProfiles().add(projectProfile);
//        });

        // pool 사이즈 변경
        System.out.println( dto );
        dto.getTargetJobRanks().forEach(jobRankId -> {
            JobRank jobRank = jobRankRepository.findById( jobRankId ).orElseThrow( () ->
                    new JobRankNotFoundException(jobRankId,this));
            ProjectJobRank projectJobRank = new ProjectJobRank( project, jobRank );
            project.getTargetJobRanks().add( projectJobRank );
        });
        //부서는 없을 수 있다. 전사 프로젝트라면 전체를 다 포함.


        Department targetDepartment = departmentRepository
                .findById( dto.getTargetDepartmentId() ).orElseThrow( () ->
                        new DepartmentNotFoundException(dto.getTargetDepartmentId(),this));

        project.updateTargetDepartment(targetDepartment);



        project.setModifiedAt(LocalDateTime.now());
        projectRepository.save(project);
        return project;
    }

    @Override
    public Project copyProject(UserDetailsImpl userDetails, Integer projectId, ProjectCopyDto projectCopyDto) {

        Project project = projectRepository.findById( projectCopyDto.getTargetProjectId() )
                .orElseThrow( () -> new ProjectNotFoundException(projectId, this));


        List<Integer> updatedProfileIds = projectCopyDto.getProfileIds();
        List<ProjectProfile> existingProjectProfiles = project.getProjectProfiles();


        // 새로운 ProjectProfile 추가
        if( updatedProfileIds != null ){
            updatedProfileIds.forEach(profileId -> {
                if (existingProjectProfiles.stream().noneMatch(pp -> pp.getProfile().getId().equals(profileId))) {
                    Profile profile = profileRepository.findById(profileId)
                            .orElseThrow(() -> new ProfileNotFoundException(profileId, this));
                    ProjectProfile projectProfile = new ProjectProfile(project, profile);
                    project.getProjectProfiles().add(projectProfile);
                }
            });

        }
        project.setModifiedAt(LocalDateTime.now());

        return project;
    }
    //TODO 추가 예정 QUERYDSL 듣고 진행 04-22 완
    @Override
    public ProjectListFullDto getAllProjectsWithFilter(UserDetailsImpl userDetails, ProjectSearchConditionDto searchConditionDto, Pageable pageable) {

        System.out.println(userDetails.getAuthorityLevel() + "님께서 프로젝트 리스트 조회 page" + pageable.getPageNumber() + " size:" + pageable.getPageSize() );
        if( userDetails.getAuthorityLevel().equals(1)){
            Page<Project> findResult = projectRepositoryCustom
                    .findAllWithFilterByAdmin(
                            userDetails.getMemberId(),
                            searchConditionDto,
                            pageable
                    );
            List<Department> departments = departmentRepository.findDepartments();
            List<Integer> targetYears = projectRepository.findAll().stream().map( p -> {
                return p.getTargetYear();
            }).collect(Collectors.toSet()).stream().toList();
            ProjectListFullDto projectListFullDto= ProjectListFullDto.fromEntity( findResult.stream().map(
                        p -> {
                            ProjectSummaryDto dto = ProjectSummaryDto.fromEntity( p );
                            dto.setIsBookMarked( p.getProjectBookMarks().stream().anyMatch( pb -> pb.getMember().getId().equals( userDetails.getMemberId()) ));
                            return dto;
                        }).toList());

            System.out.println("조회결과" + findResult.toString());
            projectListFullDto.setTotalPages( findResult.getTotalPages() );
            projectListFullDto.setTotalCount( findResult.getTotalElements());






            return projectListFullDto;
        }else if( userDetails.getAuthorityLevel().equals(2)){
            searchConditionDto.setIsPrivate(false);
            Page<Project> findResult = projectRepositoryCustom
                    .findAllWithFilterByAdmin(
                            userDetails.getMemberId(),
                            searchConditionDto,
                            pageable
                    );
            List<Department> departments = departmentRepository.findDepartments();
            List<Integer> targetYears = projectRepository.findAll().stream().map( p -> {
                return p.getTargetYear();
            }).collect(Collectors.toSet()).stream().toList();
            ProjectListFullDto projectListFullDto= ProjectListFullDto.fromEntity( findResult.stream().map(
                    p -> {
                        ProjectSummaryDto dto = ProjectSummaryDto.fromEntity( p );
                        dto.setIsBookMarked( p.getProjectBookMarks().stream().anyMatch( pb -> pb.getMember().getId().equals( userDetails.getMemberId()) ));
                        return dto;
                    }).toList());


            projectListFullDto.setTotalPages( findResult.getTotalPages() );
            projectListFullDto.setTotalCount( findResult.getTotalElements());







            return projectListFullDto;
        }

        Page<Project> findResult = projectRepositoryCustom
                .findAllWithFilter(
                        userDetails,
                        searchConditionDto,
                        pageable
                );





        ProjectListFullDto projectListFullDto = ProjectListFullDto.fromEntity( findResult.stream().map(
                p -> {
                    ProjectSummaryDto dto = ProjectSummaryDto.fromEntity( p );
                    dto.setIsBookMarked( p.getProjectBookMarks().stream().anyMatch( pb -> pb.getMember().getId().equals( userDetails.getMemberId()) ));
                    return dto;
                }).toList() );
        projectListFullDto.setTotalPages( findResult.getTotalPages() );
        projectListFullDto.setTotalCount( findResult.getTotalElements() );





        return projectListFullDto;
    }




    @Override
    public ProjectFullDto findByIdWithProfiles(UserDetailsImpl userDetails, Integer projectId) {


        Project project = projectRepository.findById( projectId )
                .orElseThrow( () -> new ProjectNotFoundException(projectId,this));;

//        Project project = projectRepository.findByIdWithProfiles( projectId )
//                .orElseThrow( () -> new ProjectNotFoundException(projectId,this));
        List<ProfilePreviewDto> pps = project.getProjectProfiles().stream().map(pp -> {
//            List<NetworkingProfile> networkings = networkingProfileRepository.findAllByProfile( pp.getProfile() );
            return ProfilePreviewDto.fromEntity( pp.getProfile() );
        }).toList();
        return ProjectFullDto.fromEntity( project, pps );
    }
    @Override
    public Project updateProject(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateDto dto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        validUser(userDetails, project);
        if( dto.getIsPrivate() ){
            dto.setProjectMembers( Arrays.stream(new Integer[]{ dto.getResponsibleMemberId() }).toList() );
        }
        project.update(dto);
        project.setModifiedAt(LocalDateTime.now());
        updateJobRanks( project, dto );

        updateMembers( userDetails, projectId, dto) ;

            Department targetDepartment = departmentRepository
                    .findById(dto.getTargetDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(dto.getTargetDepartmentId(), this));
            project.updateTargetDepartment(targetDepartment);



        return project;
    }

    @Override
    public Project updateProfiles(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateDto dto){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        System.out.println("권한 체크");
        validUser(userDetails, project);
        System.out.println("권한 체크 성공");
        updateProfiles( project, dto );

        return project;
    }
    @Override
    public void updateProfiles(Project project, ProjectUpdateDto dto){
        // ProjectProfile 업데이트
        List<Integer> updatedProfileIds = dto.getProjectProfiles();
        List<ProjectProfile> existingProjectProfiles = project.getProjectProfiles();

        // 기존 ProjectProfile 중 업데이트되지 않은 항목 제거
        existingProjectProfiles.removeIf(pp -> !updatedProfileIds.contains(pp.getProfile().getId()));

        // 새로운 ProjectProfile 추가
        if( updatedProfileIds != null ){
            updatedProfileIds.forEach(profileId -> {
                if (existingProjectProfiles.stream().noneMatch(pp -> pp.getProfile().getId().equals(profileId))) {
                    Profile profile = profileRepository.findById(profileId)
                            .orElseThrow(() -> new ProfileNotFoundException(profileId, this));
                    ProjectProfile projectProfile = new ProjectProfile(project, profile);
                    project.getProjectProfiles().add(projectProfile);
                }
            });

            project.setModifiedAt( LocalDateTime.now() );
        }


    }

    @Override
    public Project updateMembers(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateDto dto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        validUser(userDetails, project);
        updateMembers( project, dto );
        return project;
    }

    @Override
    public void updateMembers( Project project, ProjectUpdateDto dto) {
        if( dto.getIsPrivate() != null ){
            project.updateIsPrivate( dto.getIsPrivate() );
        }
        // ProjectMember 업데이트
        List<Integer> updatedMemberIds = dto.getProjectMembers();
        System.out.println("1." + updatedMemberIds);
        //없으면 추가
        if( !updatedMemberIds.contains( project.getResponsibleMemberId() )){
            updatedMemberIds.add( project.getResponsibleMemberId() );
        }
        System.out.println(updatedMemberIds);
        List<ProjectMember> existingProjectMembers = project.getProjectMembers();
        // 기존 ProjectMember 중 업데이트되지 않은 항목 제거
        existingProjectMembers.removeIf(pm -> !updatedMemberIds.contains(pm.getMember().getId()));
        System.out.println(existingProjectMembers);
        // 새로운 ProjectMember 추가
        if(updatedMemberIds != null ){
            updatedMemberIds.forEach(memberId -> {
                if (existingProjectMembers.stream().noneMatch(pm -> pm.getMember().getId().equals(memberId))) {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new MemberNotFoundException(memberId, this));
                    ProjectMember projectMember = new ProjectMember(project, member);
                    project.getProjectMembers().add(projectMember);
                }
            });
        }
        project.setModifiedAt( LocalDateTime.now() );
        //1명 이상이면 비공개 프로젝트가 아님.
        if( project.getProjectMembers().size() > 1 ){
            project.updateIsPrivate( false );
        }

    }


    @Override
    public void updateJobRanks(Project project, ProjectUpdateDto dto){
        // ProjectJobRank 업데이트
        List<Integer> updatedJobRankIds = dto.getTargetJobRanks();
        List<ProjectJobRank> existingProjectJobRanks = project.getTargetJobRanks();

        // 기존 ProjectJobRank 중 업데이트되지 않은 항목 제거
        existingProjectJobRanks.removeIf(pjr -> !updatedJobRankIds.contains(pjr.getJobRank().getId()));

        // 새로운 ProjectJobRank 추가
        if( updatedJobRankIds != null ){
            updatedJobRankIds.forEach(jobRankId -> {
                if (existingProjectJobRanks.stream().noneMatch(pjr -> pjr.getJobRank().getId().equals(jobRankId))) {
                    JobRank jobRank = jobRankRepository.findById(jobRankId)
                            .orElseThrow(() -> new JobRankNotFoundException(jobRankId, this));
                    ProjectJobRank projectJobRank = new ProjectJobRank(project, jobRank);
                    project.getTargetJobRanks().add(projectJobRank);
                }
            });
        }
        project.setModifiedAt( LocalDateTime.now() );


    }

    @Override
    public void deleteProject( UserDetailsImpl userDetails, Integer projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        validUser(userDetails, project);

//        // ProjectMember 매핑 테이블 행 삭제
//        projectMemberRepository.deleteByProject(project);
//
//        // ProjectProfile 매핑 테이블 행 삭제
//        projectProfileRepository.deleteByProject(project);
//
//        // ProjectJobRank 매핑 테이블 행 삭제
//        projectJobRankRepository.deleteByProject(project);

        // Project 엔티티 삭제
        projectRepository.delete(project);
        project.setModifiedAt(LocalDateTime.now());
    }



    @Override
    public void updateProjectBookMark(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateBookmarkDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, this));
        Member member = memberRepository.findById( userDetails.getMemberId() )
                .orElseThrow( () -> new MemberNotFoundException(userDetails.getMemberId(), this ));
        ProjectBookmark projectBookmark = new ProjectBookmark( project, member );
        project.setModifiedAt( LocalDateTime.now() );
        if( dto.getIsBookMarked() ){
            if( project.getProjectBookMarks().stream().noneMatch( pb -> pb.getMember().getId().equals( userDetails.getMemberId()) )){
                projectBookrmarkRepository.save( projectBookmark);
            }else{
                throw new InternalServerException("북마크 생성간 에러 발생 projectId:" + projectId + "member: " + userDetails.getMemberId() + "::" + userDetails.getUsername(), this );
            }
        }else{
            projectBookrmarkRepository.deleteBookmarkByMemberIdAndProjectId( projectId, userDetails.getMemberId());
        }
    }

    @Override
    public ProjectFilterFullDto getAllFilterValue( UserDetailsImpl userDetails) {
        List<Project> findResult;
        if( userDetails.getAuthorityLevel() < 3 ){
            findResult = projectRepositoryCustom.findAllForFilterValueAdmin( userDetails.getAuthorityLevel() );
        }else{
            findResult = projectRepositoryCustom.findAllForFilterValue( userDetails );
        }

        List<ProjectMemberListSummaryDto> memberList;
        List<DepartmentAdminSummaryDto> departmentAdminSummaryDtos;
        List<JobRankAdminSummaryDto> jobRankAdminSummaryDtos = new ArrayList<>();
        List<String> targetCountries;
        List<Integer> targetYears;

//    요구사항은 누가 만들었냐임
        List<Integer> memberIds = findResult.stream().flatMap( p ->
                p.getProjectMembers().stream().map(pm -> {
                    return pm.getMember().getId();
                })
        ).distinct().toList();
        memberList = memberIds.stream().map( mid -> memberRepository.findById( mid ).orElseThrow( () -> new MemberNotFoundException( mid, this)))
                .map( ProjectMemberListSummaryDto::fromEntity)
                .toList();



        //타겟 직급 세팅

//        List<Integer> jobRankIds = findResult.stream().flatMap( p ->
//                p.getTargetJobRanks().stream().map(pj -> {
//                    return pj.getJobRank().getId();
//                })
//        ).distinct().toList();
//        jobRankAdminSummaryDtos = jobRankIds.stream().map(jid -> jobRankRepository.findById( jid ).orElseThrow( () -> new JobRankNotFoundException( jid, this)))
//                .map( JobRankAdminSummaryDto::fromEntity)
//                .toList();
        
        jobRankAdminSummaryDtos.add( new JobRankAdminSummaryDto( 1, "isE"));
        jobRankAdminSummaryDtos.add( new JobRankAdminSummaryDto( 2, "실무급"));

        targetCountries = findResult.stream().map( p -> p.getTargetCountry() ).collect(Collectors.toSet()).stream().toList();

        targetYears = findResult.stream().map( p -> p.getTargetYear() ).collect(Collectors.toSet()).stream().toList();

        departmentAdminSummaryDtos = findResult.stream().map( p -> {

            return p.getTargetDepartment() != null ? p.getTargetDepartment().getId() : null;
        } ).collect(Collectors.toSet()).stream().map( id ->{

                    if(id==null || id == -1){
                        return null;
                    }
                    return DepartmentAdminSummaryDto.fromEntity(departmentRepository
                            .findById( id ).orElseThrow( () ->
                                    new DepartmentNotFoundException( id ,this) ) );
                }
        ).collect(Collectors.toList());


        //통계 세팅

        //TODO 일단 flatMap으로 빠르게 구현하고 추후에 n번 도는 로직으로 변경
        // 요구사항은 생성자임.
        //멤버 세팅
        ProjectStaticsFullDto staticsFullDto = ProjectStaticsFullDto.builder().build();;
        Map< Integer, Integer > memberStatics = new HashMap<>();
//        findResult.stream().forEach( p ->
//                p.getProjectMembers().stream().forEach( pm ->
//                        memberStatics.put( pm.getMember().getId(), memberStatics.getOrDefault( pm.getMember().getId(), 0 ) + 1 )
//                )
//        );
        findResult.forEach( p -> {
            memberStatics.put( p.getResponsibleMemberId(), memberStatics.getOrDefault( p.getResponsibleMemberId(), 0 ) + 1 );
        });
        staticsFullDto.setProjectMemberStatics(memberStatics);


        //타겟 직급 세팅
        Map< Integer, Integer > jobrankStaics = new HashMap<>();

        Optional<JobRank> executiveLineJobRank = jobRankRepository.findByDescription("직급4");
        if( executiveLineJobRank.isEmpty() ){
            executiveLineJobRank = jobRankRepository.findByDescription("부직급1↑");
        }

        Integer executiveLineNumber = executiveLineJobRank.get().getId();


        findResult.stream().forEach( p ->{

            Integer targetId = p.getTargetJobRanks().stream().map( pj -> pj.getJobRank().getId() ).anyMatch( jid -> jid <= executiveLineNumber ) ? 1 : 2;
                    log.info("target : jr " + p.getId() + "::" + targetId +":" + p.getTargetJobRanks().toString());
                    jobrankStaics.put( targetId, jobrankStaics.getOrDefault( targetId, 0 ) + 1 );
                }
//                p.getTargetJobRanks().stream().forEach(pj -> {
//                    Integer targetId = pj.getJobRank().getId() > 4 ? 1 : 2;
////                    jobrankStaics.put( pj.getJobRank().getId(), jobrankStaics.getOrDefault( pj.getJobRank().getId(), 0 ) + 1 );
//                    jobrankStaics.put( targetId, jobrankStaics.getOrDefault( targetId, 0 ) + 1 );
//                })
        );
        staticsFullDto.setTargetJobRanksStatics(jobrankStaics);

        //1:n 끝났으니 나머지는 n번으로 해결
        Map<Boolean, Integer> isPrivateStatics = new HashMap<>();
        Map<Integer,Integer> targetYearStatics = new HashMap<>();
        Map<Integer,Integer> targetDepartmentStatics = new HashMap<>();
        Map<ProjectType, Integer> projectTypeStatics = new HashMap<>();
        Map<String,Integer> targetCountryStatics = new HashMap<>();

        for( Project p : findResult ){
            isPrivateStatics.put( p.getIsPrivate(), isPrivateStatics.getOrDefault( p.getIsPrivate(), 0 ) + 1 );
            targetYearStatics.put( p.getTargetYear(), targetYearStatics.getOrDefault( p.getTargetYear(), 0 ) + 1 );
            targetDepartmentStatics.put( p.getTargetDepartment() != null ? p.getTargetDepartment().getId(): -1 , targetDepartmentStatics.getOrDefault(p.getTargetDepartment() != null ? p.getTargetDepartment().getId(): -1, 0 ) + 1 );
            projectTypeStatics.put( p.getProjectType(), projectTypeStatics.getOrDefault( p.getProjectType(), 0 ) + 1 );
            targetCountryStatics.put( p.getTargetCountry() == null ? "없음" : p.getTargetCountry(), targetCountryStatics.getOrDefault( p.getTargetCountry(), 0 ) + 1 );
        }


        staticsFullDto.setIsPrivateStatics(isPrivateStatics);
        staticsFullDto.setProjectTypeStatics(projectTypeStatics);
        staticsFullDto.setTargetYearStatics( targetYearStatics ) ;
        staticsFullDto.setTargetDepartmentStatics(targetDepartmentStatics);
        staticsFullDto.setTargetCountryStatics(targetCountryStatics);


        return ProjectFilterFullDto
                .builder()
                .departmentAdminSummaryDtos(departmentAdminSummaryDtos)
                .targetYears(targetYears)
                .targetCountries(targetCountries)
                .memberList(memberList)
                .projectStaticsFullDto(staticsFullDto)
                .jobRankAdminSummaryDtos(jobRankAdminSummaryDtos)
                .build();
    }

    @Override
    public void updateTitle(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateDto dto) {
        Project project = projectRepository.findById( projectId )
                .orElseThrow( () -> new ProjectNotFoundException(projectId, this));
        validUser(userDetails, project);
        project.updateTitle( dto.getTitle() );
        project.setModifiedAt(LocalDateTime.now());

    }

    public void validUser( UserDetailsImpl userDetails,Project project){
        if( !userDetails.getMemberId().equals(project.getResponsibleMemberId() ) ){
            if( !project.getProjectMembers().stream().map( p -> p.getMember().getId()).collect(Collectors.toList()).contains(userDetails.getMemberId()) ){
                if( userDetails.getAuthorityLevel() > 2 ){
                    throw new InvalidAuthorizationException(userDetails.getMemberId(),this);
                }
            }
        }
    }

    //TODO Map< Column명, 한국명 > 을 활용해서 로직을 단순화 할 수 있다.
    @Override
    public ByteArrayResource excelDownload(UserDetailsImpl userDetails, ProjectExcelDto projectExcelDto) throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Profile> profiles = projectExcelDto.getProfileIds().stream().map( profileId -> profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId, this)) ).toList();
        Project project = projectRepository.findById( projectExcelDto.getProjectId() )
                .orElseThrow( () -> new ProjectNotFoundException( projectExcelDto.getProjectId(),this));


            for( Profile profile : profiles ){

                Map<String, String> map = new LinkedHashMap<>();
                map.put("프로젝트 명", project.getTitle() );
                map.put("한글 이름", profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("name")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("name")).findFirst().get().getContent() : "");
                map.put("영어 이름", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("nameEng")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("nameEng")).findFirst().get().getContent() : "");
                map.put("column1", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("column1")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("column1")).findFirst().get().getContent() : "");
                map.put("column1", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("column1")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("column1")).findFirst().get().getContent() : "");
                map.put("지역", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("foundRegion")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("foundRegion")).findFirst().get().getContent() : "");
                map.put("비자 유형", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("visa")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("visa")).findFirst().get().getContent() : "");
                map.put("한국어", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("korean")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("korean")).findFirst().get().getContent() : "");
                map.put("Relocation", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("relocation")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("relocation")).findFirst().get().getContent() : "");
                map.put("발굴 경로", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("foundPath")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("foundPath")).findFirst().get().getContent() : "");
                map.put("추천인", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("recommenderInfo")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("recommenderInfo")).findFirst().get().getContent() : "");
                map.put("직무 구분", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("skillMainCategory")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("skillMainCategory")).findFirst().get().getContent() : "");
                map.put("기술 분야", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("skillSubCategory")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("skillSubCategory")).findFirst().get().getContent() : "");
                map.put("상세 분야", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("skillDetail")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("skillDetail")).findFirst().get().getContent() : "");
                map.put("전화번호", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("phone")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("phone")).findFirst().get().getContent() : "");
                map.put("이메일",profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("email")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("email")).findFirst().get().getContent() : "");
                map.put("link", profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("url")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("url")).findFirst().get().getContent() : "");
                map.put("개인홈페이지",profile.getProfileColumnDatas().stream()
                        .filter( pc -> pc.getProfileColumn().getName().equals("homepageUrl")).findFirst().isPresent() ?
                        profile.getProfileColumnDatas().stream()
                                .filter( pc -> pc.getProfileColumn().getName().equals("homepageUrl")).findFirst().get().getContent() : "");
                int index = 1;
                for(Education education: profile.getEducations() ){
                    map.put("학위"+index, education.getDegree() != null ? education.getDegree().toString() : "");
                    map.put("학교국가"+index, education.getSchool() != null ? education.getSchool().getCountry() : "");
                    map.put("학교명"+index, education.getSchool() != null ? education.getSchool().getSchoolName() : "" );
                    map.put("전공명"+index, education.getMajor() );
                    map.put("입학년월"+index, education.getEnteredAt() != null ? education.getEnteredAt().toString() : "");
                    map.put("졸업년월" + index, education.getGraduatedAt() != null ? education.getGraduatedAt().toString() : "");
                    map.put("졸업여부"+index, education.getGraduateStatus() != null ? education.getGraduateStatus().toString() : "");

                    index++;
                }
                int index2 = 1;
                for(Career career : profile.getCareers()){
                    map.put("회사명"+index2, career.getCompany() != null ? career.getCompany().getName():"");
                    map.put("직급"+index2, career.getJobRank() );
                    map.put("고용형태"+index2, career.getEmployType() != null ? career.getEmployType().toString() : "");
                    map.put("경력국가"+index2, career.getCountry());
                    map.put("경력시작년월"+index2, career.getStartedAt() != null ? career.getStartedAt().toString() : "");
                    map.put("경력종료년월"+index2, career.getEndedAt() != null ? career.getEndedAt().toString() : "");
                    map.put("근무부서" +index2, career.getDept() );
                    map.put("담당업무"+index2, career.getDescription());
                    index2++;
                }

                dataList.add(map);
            }


        return new ByteArrayResource(excelUtil.mapToExcel(dataList));
    }


}
