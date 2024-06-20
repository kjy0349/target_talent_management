package com.ssafy.s10p31s102be.techmap.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechmapCreateDto {
    private Integer targetYear;
    private List<Integer> departments;
    private String description;
    private List<String> keywords;
    private LocalDateTime dueDate;
    private Boolean isAlarmSend;
}