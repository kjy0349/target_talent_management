package com.ssafy.s10p31s102be.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.enums.TreatNegotiationStep;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreatNegotiationUpdateDto {
    private TreatNegotiationStep step;
    private LocalDateTime estimatedJoinDate;
    private String description;
    private Integer jobRankId;
}
