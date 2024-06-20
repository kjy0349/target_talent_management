package com.ssafy.s10p31s102be.admin.dto.request.networking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingAdminCreateDto {
    private Integer memberId;
    private Integer executiveId;
    private String category;
}
