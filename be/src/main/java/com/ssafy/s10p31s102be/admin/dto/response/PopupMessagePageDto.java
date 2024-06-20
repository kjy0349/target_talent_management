package com.ssafy.s10p31s102be.admin.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupMessagePageDto {
    private List<PopupMessageReadDto> popupMessages;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer size;
}
