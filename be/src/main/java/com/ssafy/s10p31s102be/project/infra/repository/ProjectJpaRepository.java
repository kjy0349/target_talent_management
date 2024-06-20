package com.ssafy.s10p31s102be.project.infra.repository;

import com.ssafy.s10p31s102be.project.infra.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Integer> {

    @Query("select p from Project p join fetch p.projectProfiles pp where p.id = :projectId")
    public Optional<Project> findByIdWithProfiles(@Param(value = "projectId") Integer projectId );
    @Query("select p from Project p join fetch p.targetDepartment")
    public List<Project> findAllWithTargetDepartment();
}
