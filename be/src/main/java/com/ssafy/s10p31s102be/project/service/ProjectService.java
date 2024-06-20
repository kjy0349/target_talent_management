package com.ssafy.s10p31s102be.project.service;


import com.ssafy.s10p31s102be.admin.dto.request.ProfileAdminUpdateExecutiveDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.project.dto.request.*;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFilterFullDto;
import com.ssafy.s10p31s102be.project.dto.response.ProjectFullDto;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.project.dto.response.ProjectListFullDto;
import com.ssafy.s10p31s102be.project.infra.entity.Project;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ProjectService {
    Project createProject(UserDetailsImpl UserDetailsImpl, ProjectCreateDto dto);

    Project copyProject(UserDetailsImpl UserDetailsImpl, Integer projectId, ProjectCopyDto projectCopyDto);

    ProjectListFullDto getAllProjectsWithFilter(UserDetailsImpl UserDetailsImpl, ProjectSearchConditionDto searchConditionDto,Pageable pageable);

    ProjectFullDto findByIdWithProfiles(UserDetailsImpl UserDetailsImpl, Integer projectId);

    Project updateProject(UserDetailsImpl UserDetailsImpl, Integer projectId, ProjectUpdateDto dto);

    Project updateProfiles(UserDetailsImpl UserDetailsImpl, Integer projectId, ProjectUpdateDto dto);

    void updateProfiles(Project project, ProjectUpdateDto dto);

    Project updateMembers(UserDetailsImpl UserDetailsImpl, Integer projectId, ProjectUpdateDto dto);

    void updateMembers(Project project, ProjectUpdateDto dto);

    void updateJobRanks(Project project, ProjectUpdateDto dto);

    void deleteProject(UserDetailsImpl UserDetailsImpl, Integer projectId);
//    Profile updateProfileExecutive(UserDetailsImpl userDetails, ProjectProfileUpdateExecutiveDto dto);


    void updateProjectBookMark(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateBookmarkDto dto);

    ProjectFilterFullDto getAllFilterValue( UserDetailsImpl userDetails);

    void updateTitle(UserDetailsImpl userDetails, Integer projectId, ProjectUpdateDto dto);

    ByteArrayResource excelDownload(UserDetailsImpl userDetails, ProjectExcelDto projectExcelDto) throws IOException;
}
