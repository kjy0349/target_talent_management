package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabJpaRepository extends JpaRepository<Lab, Integer> {
    Optional<Lab> findByLabName(String labName);

    @Query("select distinct l.labName from Lab l " +
            "where upper(l.labName) like concat('%', upper(:word), '%') and l.isDeleted = false")
    List<String> findLabsByLabName(@Param("word") String word);

    @Query("select l from Lab l where l.school.id = :schoolId and l.isDeleted = false")
    List<Lab> findAllBySchoolIdNotDeleted( @Param( value = "schoolId") Integer schoolId);

    @Query("select l from Lab l where l.isDeleted = false")
    List<Lab> findAllNotDeleted();

    @Query("select l from Lab l where l.isDeleted = false and l.labName = :labName and l.school.id = :schoolId")
    Optional<Lab> findLabsByLabNameAndSchoolId( @Param(value = "labName") String labName, @Param(value = "schoolId") Integer schoolId );
}
