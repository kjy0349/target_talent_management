package com.ssafy.s10p31s102be.profile.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileFilterResponseDto {
    Page<ProfilePreviewDto> profilePreviews;
    List<RangeCountPair> careerRange;
    List<RangeCountPair> graduateAtRange;
    List<RangeCountPair> founderDepartmentCounts;
    List<RangeCountPair> employStatusCounts;
    String myDepartment;
    Integer myProfileCount;
}
