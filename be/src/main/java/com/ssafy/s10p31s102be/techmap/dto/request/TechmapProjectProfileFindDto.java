package com.ssafy.s10p31s102be.techmap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechmapProjectProfileFindDto {
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer size;
}
