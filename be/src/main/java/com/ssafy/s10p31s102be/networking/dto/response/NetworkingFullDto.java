package com.ssafy.s10p31s102be.networking.dto.response;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingFullDto {
    public Integer networkId;
    public LocalDateTime modifiedAt;
    public String category;
    public String selectDescription;
    public String executiveName;
    public NetworkingStatus networkingStatus;
    public List< NetworkingProfileFullDto> networkingProfiles;

    public static NetworkingFullDto fromEntity(Networking n) {
        return NetworkingFullDto.builder()
                .networkId(n.getId())
                .networkingStatus(n.getNetworkingStatus())
                .networkingProfiles(n.getNetworkingProfiles().stream().map( np -> NetworkingProfileFullDto.fromEntity( np.getProfile() )).toList())
                .executiveName(n.getExecutive().getName())
//                .selectDescription(n.getSelectDescription())
                .modifiedAt(n.getModifiedAt())
                .category(n.getCategory())
                .build();
    }

}
