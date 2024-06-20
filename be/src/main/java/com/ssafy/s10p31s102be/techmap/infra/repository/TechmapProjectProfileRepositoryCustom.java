package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TechmapProjectProfileRepositoryCustom {
    Page<Profile> findProfilesBytechmapProjectConditions(Integer techmapProjectId, Pageable pageable);
}
