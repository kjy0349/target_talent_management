package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileFilterSearchedDto {
    List<Profile> filteredProfiles;
    Integer count;
    List<RangeCountPair> careerRange;
    List<RangeCountPair> graduateAtRange;
    List<RangeCountPair> founderDepartmentCounts;
    List<RangeCountPair> employStatusCounts;
    String myDepartment;
    Integer myProfileCount;
}
