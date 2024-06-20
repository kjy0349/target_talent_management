package com.ssafy.s10p31s102be.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateDto {
    private String name;
    private Integer readAuthorityLevel;
    private Integer writeAuthorityLevel;
    private Integer manageAuthorityLevel;
    private Boolean isCommentUsed;
    private Boolean canViewCount;
    private Boolean canViewWriter;
}
