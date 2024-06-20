package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.education.KeywordEducation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordEducationJpaRepository extends JpaRepository<KeywordEducation, Integer> {
    @Query("select ke.keyword.data from KeywordEducation ke where ke.education.profile.id = :profileId")
    List<String> findAllByProfileId(@Param("profileId") Integer profileId);
}