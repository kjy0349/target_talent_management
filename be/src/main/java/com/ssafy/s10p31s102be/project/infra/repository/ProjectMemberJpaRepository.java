package com.ssafy.s10p31s102be.project.infra.repository;

import com.ssafy.s10p31s102be.project.infra.entity.Project;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectMemberJpaRepository extends JpaRepository<ProjectMember, Integer> {
    @Modifying
    @Query("delete from ProjectMember pm where pm.project = :project")
    void deleteByProject(@Param("project")Project project);
}
