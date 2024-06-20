package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsagePlanJpaRepository extends JpaRepository<UsagePlan, Integer> {
    Optional<UsagePlan> findByProfileId(Integer profileId);

    List<UsagePlan> findAllByProfileId(Integer profileId);
}