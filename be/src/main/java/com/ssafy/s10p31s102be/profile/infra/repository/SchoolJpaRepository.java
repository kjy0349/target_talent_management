package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolJpaRepository extends JpaRepository<School, Integer> {

    @Query("select s from School s where s.isDeleted = false and s.schoolName = :schoolName")
    Optional<School> findBySchoolName(@Param( value = "schoolName" ) String schoolName );



    @Query( "select s from School s where s.isDeleted = false")
    List<School> findAllNotDeleted();
}

