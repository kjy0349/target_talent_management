package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.Member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member,Integer> {

    @Query( "select m from Member m where m.knoxId = :knoxId and m.isDeleted = false")
    Optional<Member> findByKnoxId(@Param("knoxId") String knoxId);

    @Query( "select m from Member m where m.isDeleted = false")
    List<Member> findMembers(Pageable pageable);

    @Query( "select m from Member m where m.isDeleted = false and m.authority.authLevel > 2")
    List<Member> findMembersForNotification();

    @Query( "select m from Member m where m.isDeleted = false and m.authority.authLevel = 3")
    List<Member> findHeadRecruitersForNotification();

    @Query("select m from Member m where m.isDeleted = false and m.department.id = :departmentId")
    List<Member> findMembersByDepartmentId(@Param("departmentId") Integer departmentId );

    @Query("select count(*) from Member m where m.isDeleted = false")
    Long selectCount();


}
