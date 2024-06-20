package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileColumnDictionaryJpaRepository extends JpaRepository<ProfileColumnDictionary, Long> {
}
