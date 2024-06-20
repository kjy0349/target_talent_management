package com.ssafy.s10p31s102be.networking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetworkingExcelDto {
    List<Integer> networkingIds;
}
