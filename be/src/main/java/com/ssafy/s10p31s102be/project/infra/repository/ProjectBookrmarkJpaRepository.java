package com.ssafy.s10p31s102be.project.infra.repository;

import com.ssafy.s10p31s102be.project.infra.entity.ProjectBookmark;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface ProjectBookrmarkJpaRepository extends JpaRepository<ProjectBookmark, Integer> {

    @Modifying
    @Query("delete from ProjectBookmark pb where pb.member.id=:memberId and pb.project.id=:projectId")
    public void deleteBookmarkByMemberIdAndProjectId(@Param("projectId") Integer projectId, @Param("memberId") Integer memberId);

}
