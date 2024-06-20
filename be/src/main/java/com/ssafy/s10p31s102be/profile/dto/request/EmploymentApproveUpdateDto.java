package com.ssafy.s10p31s102be.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentApproveResultType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentApproveUpdateDto {
    @NotNull(message = "단계 값이 존재하지 않습니다.")
    private Integer step;
    @NotNull(message = "승인 날짜 값이 존재하지 않습니다.")
    private LocalDateTime approvedAt;
    private String description;
    @NotNull(message = "승인 결과 값이 존재하지 않습니다.")
    private EmploymentApproveResultType resultType;
}
