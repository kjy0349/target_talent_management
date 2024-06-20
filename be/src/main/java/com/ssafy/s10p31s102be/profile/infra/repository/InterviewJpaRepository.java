package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Interview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewJpaRepository extends JpaRepository<Interview,Integer> {
    List<Interview> findAllByProfile(Profile profile);
}
