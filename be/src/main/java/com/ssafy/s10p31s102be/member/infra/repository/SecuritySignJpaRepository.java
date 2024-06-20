package com.ssafy.s10p31s102be.member.infra.repository;


import com.ssafy.s10p31s102be.member.infra.entity.SecuritySign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecuritySignJpaRepository extends JpaRepository<SecuritySign, Integer> {
}
