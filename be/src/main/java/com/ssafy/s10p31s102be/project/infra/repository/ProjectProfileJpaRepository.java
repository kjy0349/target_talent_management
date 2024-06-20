package com.ssafy.s10p31s102be.project.infra.repository;

import com.ssafy.s10p31s102be.project.infra.entity.Project;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectProfileJpaRepository extends JpaRepository<ProjectProfile,Integer> {
    @Modifying
    @Query("delete from ProjectProfile pp where pp.project = :project")
    void deleteByProject(@Param("project") Project project);
}
