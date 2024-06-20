package com.ssafy.s10p31s102be.techmap.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechmapProjectListDto {
    private List<Integer> techmapProjectIds;
}
