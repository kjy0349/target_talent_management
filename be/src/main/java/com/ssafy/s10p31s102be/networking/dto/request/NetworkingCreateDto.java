package com.ssafy.s10p31s102be.networking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetworkingCreateDto {

    private Integer memberId;
    private Integer executiveId;
    private String category;
}
