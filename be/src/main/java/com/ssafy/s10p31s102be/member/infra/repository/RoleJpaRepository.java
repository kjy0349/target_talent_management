package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
    @Query( "select r from Role r where r.isDeleted = false")
    List<Role> findRoles();
}
