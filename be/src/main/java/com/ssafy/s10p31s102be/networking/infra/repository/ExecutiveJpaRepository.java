package com.ssafy.s10p31s102be.networking.infra.repository;

import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveJpaRepository extends JpaRepository<Executive,Integer> {
}
