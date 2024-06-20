package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.dto.request.*;
import com.ssafy.s10p31s102be.admin.dto.response.*;
import com.ssafy.s10p31s102be.admin.dto.response.PopupMessagePageDto;
import com.ssafy.s10p31s102be.admin.infra.entity.PopupMessage;
import com.ssafy.s10p31s102be.profile.exception.LabNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectListFullDto;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.infra.entity.*;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.project.dto.request.ProjectSearchConditionDto;
import com.ssafy.s10p31s102be.project.infra.entity.Project;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    /**
     Member
       * 공통사항 - CRUD
     */
    Long getMemberCount( UserDetailsImpl userDetails );
    Member createMember( UserDetailsImpl userDetails, MemberAdminCreateDto dto );
    MemberSearchResultDto getAllMembersByFilter(UserDetailsImpl userDetails, MemberAdminSearchConditionDto memberAdminSearchConditionDto, Pageable pageable);
    List<MemberAdminSummaryDto> getAllMembers(UserDetailsImpl userDetails, Pageable pageable);

    List<Member> updateMemberAuthorities(UserDetailsImpl userDetails, List<Integer> memberIds, Integer authorityId);

    Member updateMember(UserDetailsImpl userDetails, Integer targetId, MemberAdminUpdateDto dto );
    void deleteMember( UserDetailsImpl userDetails, Integer targetId );

    void deleteMembers(UserDetailsImpl userDetails, MemberAdminDeleteDto dto);

    /**
     * 추가사항 Authority, Team, Department, Role도 관리해야 된다.
     */
    //-- Authority --
    Authority createAuthority( UserDetailsImpl userDetails, AuthorityAdminCreateDto dto );
    List<Authority> getAllAuthorities(UserDetailsImpl userDetails );
    Authority updateAuthority( UserDetailsImpl userDetails, Integer authorityId, AuthorityAdminUpdateDto dto );
    void deleteAuthority( UserDetailsImpl userDetails, Integer authorityId );
    //-- Team --
    Team createTeam( UserDetailsImpl userDetails, TeamAdminCreateDto dto );
    List<Team> getAllTeams( UserDetailsImpl userDetails );
    Team updateTeam( UserDetailsImpl userDetails, Integer teamId,TeamAdminUpdateDto dto );
    void deleteTeam( UserDetailsImpl userDetails, Integer teamId );
    //-- Department --
    Department createDepartment( UserDetailsImpl userDetails, DepartmentAdminCreateDto dto );
    List<Department> getAllDepartments(UserDetailsImpl userDetails);

    Department updateDepartment(UserDetailsImpl userDetails,Integer departmentId, DepartmentAdminUpdateDto dto );
    void deleteDepartment( UserDetailsImpl userDetails,Integer departmentId );
    //-- Role --
    Role createRole( UserDetailsImpl userDetails,RoleAdminCreateDto dto );
    List<Role> getAllRoles(UserDetailsImpl userDetails);

    Role updateRole(UserDetailsImpl userDetails, Integer roleId, RoleAdminUpdateDto dto );
    void deleteRole( UserDetailsImpl userDetails, Integer roleId );

    List<JobRank> getAllJobRanks( UserDetailsImpl userDetails );

    List<ProfilePreviewAdminSummaryDto> getAllProfilePreviewByFilter(UserDetailsImpl userDetails, Pageable pageable, ProfileAdminFilterSearchDto profileFilterSearchDto);

    /**
     Profile
      * 공통사항 - C
     */

    List<ProfileAdminSummaryDto> getAllProfiles(UserDetailsImpl userDetails);
    Profile updateProfile( Integer profileId, UserDetailsImpl userDetails, ProfileUpdateDto dto );
    void deleteProfile( Integer profileId, UserDetailsImpl userDetails );
    /**
     Project
       * 공통사항 - RUD
       * 프로젝트의 생성은 기본적으로 멤버 권한. -> 담당자가 인재풀을 관리하는 개념
     */
    Long getProjectsCount( UserDetailsImpl userDetails );

    ProjectFullDto getProjectById(UserDetailsImpl userDetails, Integer projectId );
    ProjectListFullDto getAllProjectsWithFilter(UserDetailsImpl userDetails, ProjectSearchConditionDto searchConditionDto, Pageable pageable);
    Project updateProject( UserDetailsImpl userDetails,Integer projectId, ProjectAdminUpdateDto dto );
    void deleteProject( UserDetailsImpl userDetails, Integer projectId );

    Project updateProfiles(UserDetailsImpl userDetails, Integer projectId, ProjectAdminUpdateDto dto);

    void updateProfiles(Project project, ProjectAdminUpdateDto dto);

    Project updateMembers(UserDetailsImpl userDetails, Integer projectId, ProjectAdminUpdateDto dto);

    void updateMembers(Project project, ProjectAdminUpdateDto dto);

    void updateJobRanks(Project project, ProjectAdminUpdateDto dto);

    /**
     Networking
       * 공통사항 - CRUD
       *
     */
//    Networking createNetworking(UserDetailsImpl userDetails,NetworkingAdminCreateDto dto );
//    List<Networking> getAllNetworkings(UserDetailsImpl userDetails);
//
//    NetworkingSearchResultDto getAllNetworkingsWithFilter(UserDetailsImpl userDetails, NetworkingSearchCondition searchConditionDto, Pageable pageable);
//
//    Networking updateNetworking(Integer networkingId, UserDetailsImpl userDetails, NetworkingAdminUpdateDto dto );
//
//    List<Networking> updateNetworking(UserDetailsImpl userDetails, List<NetworkingAdminUpdateDto> dtos );
//
//    Networking updateNetworking(UserDetailsImpl userDetails, Integer NetworkingId ,NetworkingAdminUpdateDto dto);
//    Networking updateNetworkingProfiles(UserDetailsImpl userDetails, Integer networkingId, NetworkingAdminUpdateDto dto);
//    Networking updateNetworkingProfiles(Integer networkingId, NetworkingAdminUpdateDto dto);
//    void deleteNetworking( UserDetailsImpl userDetails,Integer networkingId );
//    List<Executive> getAllExecutives(UserDetailsImpl userDetails);
//    ExecutiveSearchResultDto getAllExecutiveByFilter(UserDetailsImpl userDetails, ExecutiveAdminSearchConditionDto executiveAdminSearchConditionDto, Pageable pageable);
//    List<Profile> getAllProfilesNotNetworked(UserDetailsImpl userDetails);
    /**
     TODO DashBoard
       * 공통사항 - CRUD
     */

    /**
     TODO techmap
       * 공통사항 - CRUD
     */

    /**
     Alarms
     * 공통사항 - CRUD
        - board( 일단 공지사항 게시판 Article 먼저 )
        - TODO notification
        - TODO popup
        - TODO menu
     */
    Article createArticle(UserDetailsImpl userDetails,ArticleAdminCreateDto dto );
    List<Article> getAllArticles(UserDetailsImpl userDetails);

    Article updateArticle( UserDetailsImpl userDetails,Integer articleId, ArticleAdminUpdateDto dto );
    void deleteArticle( Integer articleId,UserDetailsImpl userDetails );







    Integer getProfileCount(UserDetailsImpl userDetails);

    ProfileAdminFilterGraphFullDto getProfileGrapPollSizeata(UserDetailsImpl userDetails);

    Profile updateProfileExecutive(UserDetailsImpl userDetails, ProfileAdminUpdateExecutiveDto dto);




//    void deleteNetworkings(UserDetailsImpl userDetails, List<Integer> networkIds);


    PopupMessage createPopupMessage(UserDetailsImpl userDetails, PopupAdminCreateDto popupAdminCreateDto);

    PopupMessageReadDto findPopupMessage(UserDetailsImpl userDetails, Integer popupMessageId);

    PopupMessagePageDto findPopupMessages(UserDetailsImpl userDetails, PopupMessageFindDto popupMessageFindDto);

    PopupMessage updatePopupMessage(UserDetailsImpl userDetails, Integer popupMessageId, PopupAdminCreateDto popupAdminCreateDto);

    void deletePopupMessage(UserDetailsImpl userDetails, Integer popupMessageId);

    PopupMessageReadDto showPopupMessages(UserDetailsImpl userDetails);

    MemberUsageChangeDto getUsageChanges(UserDetailsImpl userDetails);

    MemberAdminStaticDto getAdminMainPageStatics(UserDetailsImpl userDetails);

    School createSchool(UserDetailsImpl userDetails, SchoolCreateDto createDto);

    List<School> getAllSchools(UserDetailsImpl userDetails);

    School updateSchool(UserDetailsImpl userDetails, Integer schoolId, SchoolUpdateDto dto);

    void deleteSchool(UserDetailsImpl userDetails, Integer schoolId);

    JobRank createJobRank(UserDetailsImpl userDetails, JobRankCreateDto createDto);

    JobRank updateJobRank(UserDetailsImpl userDetails, Integer jobrankId, JobRankUpdateDto dto);

    void deleteJobRank(UserDetailsImpl userDetails, Integer jobrankId);

    Lab createLab(UserDetailsImpl userDetails, LabAdminCreateDto createDto);

    List<Lab> getAllLabsBySchoolId(UserDetailsImpl userDetails, Integer schoolId);

    Lab updateLab(UserDetailsImpl userDetails, Integer labId, LabAdminUpdateDto dto);

    Lab updateLabSchool(UserDetailsImpl userDetails, Integer labId, LabAdminUpdateDto dto);
    void deleteLab(UserDetailsImpl userDetails, Integer labId);


    List<Lab> getAllLabs(UserDetailsImpl userDetails);
}
