package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationJpaRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findAllByProfileId(Integer profileId);

    void deleteByProfileId(Integer profileId);
}
