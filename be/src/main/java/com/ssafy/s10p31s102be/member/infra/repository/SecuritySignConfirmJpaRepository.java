package com.ssafy.s10p31s102be.member.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.SecuritySignConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecuritySignConfirmJpaRepository extends JpaRepository<SecuritySignConfirm, Integer> {
}
