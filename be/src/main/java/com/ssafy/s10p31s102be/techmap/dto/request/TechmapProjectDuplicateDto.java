package com.ssafy.s10p31s102be.techmap.dto.request;

import com.ssafy.s10p31s102be.techmap.infra.enums.MoveStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechmapProjectDuplicateDto {
    private Integer techmapId;
    private MoveStatus moveStatus;
    private List<Integer> techmapProjectId;
}
