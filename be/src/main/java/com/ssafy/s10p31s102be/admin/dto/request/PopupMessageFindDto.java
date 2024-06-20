package com.ssafy.s10p31s102be.admin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupMessageFindDto {
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer size;
}
