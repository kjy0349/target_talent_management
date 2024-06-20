package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityJpaRepository extends JpaRepository<Authority, Integer> {
    @Query( "select a from Authority a where a.isDeleted = false")
    List<Authority> findAuthorities();

    @Query( "select a from Authority a where a.isDeleted = false and a.authName = :authName")
    Optional<Authority> findByName(@Param(value = "authName") String authName);
}
