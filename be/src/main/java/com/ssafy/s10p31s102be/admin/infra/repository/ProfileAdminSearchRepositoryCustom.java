package com.ssafy.s10p31s102be.admin.infra.repository;

import com.ssafy.s10p31s102be.admin.dto.request.ProfileAdminFilterSearchDto;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileFilterSearchDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileAdminSearchRepositoryCustom {
    public List<Profile> getAllProfilePreviewByFilter(Pageable pageable, ProfileAdminFilterSearchDto profileFilterSearchDto);
}
