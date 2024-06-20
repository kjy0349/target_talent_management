package com.ssafy.s10p31s102be.profile.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingCombinedDetailDto {
    private List<Object> meetingDetails = new ArrayList<>();
}
