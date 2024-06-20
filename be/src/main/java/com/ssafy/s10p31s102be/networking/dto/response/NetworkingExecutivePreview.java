package com.ssafy.s10p31s102be.networking.dto.response;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingExecutivePreview {
    private Integer executiveId;
    private String executiveName;
    private Integer NetworkingCount;

    public static NetworkingExecutivePreview fromEntity(Networking networking){
        if( networking == null ) return null;
        return NetworkingExecutivePreview.builder()
                .executiveId(networking.getExecutive().getId())
                .executiveName(networking.getExecutive().getName())
                .build();
    }
}
