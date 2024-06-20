package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileStatusUpdateDto {

    @NotNull(message = "프로필 ID가 존재하지 않습니다.")
    private List<Integer> profileIds;

    @NotNull(message = "채용 상태가 존재하지 않습니다.")
    private EmployStatus status;
}
