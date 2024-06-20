package com.ssafy.s10p31s102be.networking.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.networking.dto.request.*;
import com.ssafy.s10p31s102be.networking.dto.response.ExecutiveSearchResultDto;
import com.ssafy.s10p31s102be.networking.dto.response.NetworkingSearchResultDto;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface NetworkingService {
    Networking createNetworking(UserDetailsImpl userDetails, NetworkingCreateDto dto );
    List<Networking> getAllNetworkings(UserDetailsImpl userDetails);

    NetworkingSearchResultDto getAllNetworkingsWithFilter(UserDetailsImpl userDetails, NetworkingSearchCondition searchConditionDto, Pageable pageable);

//    Networking updateNetworking(Integer networkingId, UserDetailsImpl userDetails, NetworkingUpdateDto dto );

    List<Networking> updateNetworking(UserDetailsImpl userDetails, List<NetworkingUpdateDto> dtos );
    List<Department> getAllDepartments(UserDetailsImpl userDetails);
    Networking updateNetworking(UserDetailsImpl userDetails, Integer NetworkingId ,NetworkingUpdateDto dto);
    Networking updateNetworkingProfiles(UserDetailsImpl userDetails, Integer networkingId, NetworkingUpdateDto dto);
    Networking updateNetworkingProfiles(Integer networkingId, NetworkingUpdateDto dto);
    void deleteNetworking( UserDetailsImpl userDetails,Integer networkingId );
    List<Executive> getAllExecutives(UserDetailsImpl userDetails);

    void deleteNetworkings(UserDetailsImpl userDetails, List<Integer> networkIds);

    ExecutiveSearchResultDto getAllExecutiveByFilter(UserDetailsImpl userDetails, ExecutiveSearchConditionDto executiveAdminSearchConditionDto, Pageable pageable);
    List<Profile> getAllProfilesNotNetworked(UserDetailsImpl userDetails);

    Networking updateExecutive(UserDetailsImpl userDetails, Integer networkingId, NetworkingUpdateDto dto);

    Networking createNetworkingWithProfile(UserDetailsImpl userDetails, Integer profileId, NetworkingCreateDto dto);

    void submitNetworking(UserDetailsImpl userDetails, NetworkingSubmitDto networkingSubmitDto);

    void approveNetworking(UserDetailsImpl userDetails, Integer networkingId, Integer notificationId);

    Boolean checkSubmittableNetworking(UserDetailsImpl userDetails, NetworkingSubmitDto dto);

    ByteArrayResource excelDownload(UserDetailsImpl userDetails, NetworkingExcelDto networkingExcelDto) throws IOException;
}
