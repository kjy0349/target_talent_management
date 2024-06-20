package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.career.CareerKeyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerKeywordJpaRepository extends JpaRepository<CareerKeyword, Long> {
    @Query("select ck.keyword.data from CareerKeyword ck where ck.career.profile.id = :profileId")
    List<String> findAllByProfileId(@Param("profileId") Integer profileId);
}