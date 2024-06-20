package com.ssafy.s10p31s102be.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileAdminFilterGraphFullDto {
    private Map<String,Integer> grapPollSizeata;
}
