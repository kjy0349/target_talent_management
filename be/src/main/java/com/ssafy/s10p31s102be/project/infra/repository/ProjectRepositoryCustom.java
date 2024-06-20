package com.ssafy.s10p31s102be.project.infra.repository;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.project.dto.request.ProjectSearchConditionDto;
import com.ssafy.s10p31s102be.project.infra.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRepositoryCustom {
    Page<Project> findAllWithFilter(UserDetailsImpl userDetails, ProjectSearchConditionDto dto, Pageable pageable);

    Page<Project> findAllWithFilterByAdmin(Integer memberId, ProjectSearchConditionDto dto, Pageable pageable);

    List<Project> findAllForFilterValue( UserDetailsImpl userDetails );

    List<Project> findAllForFilterValueAdmin(Integer memberId);
}
