package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectCompany;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechCompanyReadDto {
    private Integer id;
    private String data;

    public static TechCompanyReadDto fromEntity(TechmapProjectCompany techmapProjectCompany){
        return TechCompanyReadDto.builder()
                .id(techmapProjectCompany.getCompany().getId())
                .data(techmapProjectCompany.getCompany().getName())
                .build();
    }
}
