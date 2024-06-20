package com.ssafy.s10p31s102be.networking.dto.response;


import com.ssafy.s10p31s102be.admin.dto.response.member.DepartmentAdminSummaryDto;
import com.ssafy.s10p31s102be.member.dto.response.DepartmentSummaryDto;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingFilterFullDto {
    private List<DepartmentSummaryDto> departmentSummaryDtos;
    private List< String > categories;
    private Integer maxCount;
    private Long executiveCount;
    private Integer profileCount;
    private Integer categoryCount;


    public static NetworkingFilterFullDto fromEntity(List<Networking> networkings, List<Department> departments){
        List<DepartmentSummaryDto> departmentSummaryDtos = departments.stream().map( DepartmentSummaryDto::fromEntity).toList();
        List< String > categories = networkings.stream().map( n -> n.getCategory()).collect(Collectors.toSet()).stream().toList();
        Integer profileCount = 0;
        for( Networking n : networkings ){
            for(NetworkingProfile np : n.getNetworkingProfiles() ){
                profileCount+=1;
            }
        }

        return NetworkingFilterFullDto.builder()
                .departmentSummaryDtos(departmentSummaryDtos)
                .categories(categories)
                .maxCount( networkings.size() )
                .executiveCount( networkings.stream().map( n -> n.getExecutive() ).filter(Objects::nonNull).count() )
                .profileCount( profileCount )
                .categoryCount( categories.size() )
                .build();
    }
}
