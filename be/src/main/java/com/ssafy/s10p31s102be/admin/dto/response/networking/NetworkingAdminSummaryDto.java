package com.ssafy.s10p31s102be.admin.dto.response.networking;

import com.ssafy.s10p31s102be.admin.dto.response.ProfilePreviewAdminSummaryDto;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingAdminSummaryDto {
    private Integer networkingId;
    private Integer memberId;
    private Integer executiveId;
    private String category;
    private String responsibleMemberName;
    private String executiveName;
    private List<ProfilePreviewAdminSummaryDto> profilePreviewAdminSummaryDtos;
    private NetworkingStatus status;

    public static NetworkingAdminSummaryDto fromEntity(Networking networking){
        return NetworkingAdminSummaryDto.builder()
                .networkingId(networking.getId())
                .memberId(networking.getMember().getId())
                .executiveId(networking.getExecutive().getId())
                .category(networking.getCategory())
                .executiveName( networking.getExecutive().getName() )
                .responsibleMemberName( networking.getMember() == null ? null : networking.getMember().getName() )
                .profilePreviewAdminSummaryDtos( networking.getNetworkingProfiles().stream().map( np -> ProfilePreviewAdminSummaryDto.fromEntity( np.getProfile() )).toList())
                .status(networking.getNetworkingStatus())
                .build();

    }
}
