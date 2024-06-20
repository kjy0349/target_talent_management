package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.infra.enums.DomainType;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.dto.request.*;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileDetailDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileExcelImportResponseDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileFilterSearchedDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumn;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    Profile create(UserDetailsImpl userDetails, ProfileCreateDto dto);

    Profile update(UserDetailsImpl userDetails, Integer profileId, ProfileCreateDto dto);

    List<Profile> updateProfileManager(UserDetailsImpl userDetails, ProfileManagerUpdateDto dto);

    Profile updateProfileImage(UserDetailsImpl userDetails, Integer profileId, String profileImageUrl);

    List<EmployStatus> updateProfileEmployStatus(UserDetailsImpl userDetails, ProfileStatusUpdateDto dto);

    void delete(UserDetailsImpl userDetails, ProfileIdsRequestDto dto);

    Map<String, List<String>> getCountryDisabledColumnsMap();

    Profile createFromExcel(Member manager, ProfileCreateDto dto);

    List<ProfileColumn> getProfileDynamicColumns();

    ProfileFilterSearchedDto getAllProfilePreviewByFilter(UserDetailsImpl userDetails, Pageable pageable, ProfileFilterSearchDto profileFilterSearchDto);

    ProfilePreviewDto getProfilePreviewById(Integer profileId);

    List<String> getSearchedNames(UserDetailsImpl userDetails, ProfileNameSearchDto nameSearchDto);

    ProfileDetailDto getProfileDetailById(UserDetailsImpl userDetails, Integer profileId);

    ProfileExcelImportResponseDto insertFromExcel(UserDetailsImpl userDetails, MultipartFile file) throws IOException ;

    //Project Network 및 전 프론트엔드 Profile 다중 셀렉트 모달 제작을 위한 서비스 로직 지우지 마세요. - 주석
    Page<Profile> findForSelection(UserDetailsImpl userDetails, Integer domainId, DomainType domainType, ProfileExternalSearchConditionDto searchConditionDto, Pageable pageable);

    Profile updateProfileEmployStatusById( Integer profileId, EmployStatus employStatus );

    List<Profile> recommendSimilarProfiles(UserDetailsImpl userDetails, Integer profileId);
}