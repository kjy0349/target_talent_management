package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamJpaRepository extends JpaRepository<Team, Integer> {
    @Query( "select t from Team t where t.isDeleted = false")
    List<Team> findTeams();
}
