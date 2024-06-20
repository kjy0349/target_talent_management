package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.EmploymentHistory;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentHistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentHistoryJpaRepository extends JpaRepository<EmploymentHistory, Long> {
    List<EmploymentHistory> findAllByProfileId(Integer profileId);
}
