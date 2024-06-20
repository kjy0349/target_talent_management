package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.JobRank;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRankJpaRepository extends JpaRepository<JobRank,Integer> {

    @Query("select j from JobRank  j where j.isDeleted = false and j.description = :description")
    Optional<JobRank> findByDescription( @Param( value = "description") String description);

    @Query("select j from JobRank  j where j.isDeleted = false")
    public List<JobRank> findAllNotDeleted();

}
