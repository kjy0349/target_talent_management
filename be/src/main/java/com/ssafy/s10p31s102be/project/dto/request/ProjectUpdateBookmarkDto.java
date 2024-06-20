package com.ssafy.s10p31s102be.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateBookmarkDto {
    Boolean isBookMarked;
}
