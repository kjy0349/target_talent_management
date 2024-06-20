package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumn;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileColumnDataJpaRepository extends JpaRepository<ProfileColumnData, Long> {
    Optional<ProfileColumnData> findByProfileAndProfileColumn(Profile profile, ProfileColumn profileColumn);

    void deleteAllByProfileId(Integer profileId);
}
