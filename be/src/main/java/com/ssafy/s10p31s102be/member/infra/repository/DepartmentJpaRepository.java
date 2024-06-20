package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentJpaRepository extends JpaRepository<Department,Integer> {
    @Query( "select d from Department d where d.isDeleted = false")
    List<Department> findDepartments();


    @Query("select d from Department d where d.name = :name")
    Optional<Department> findDepartmentByName( @Param(value = "name") String name );


}
