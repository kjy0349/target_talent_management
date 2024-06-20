package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechmapProjectProfilePageDto {
    private List<ProfilePreviewDto> profilePreviews;
    private int[] filteredArray;
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer size;

    public static TechmapProjectProfilePageDto fromEntity(List<ProfilePreviewDto> profilePreviews, Page<Profile> pages){
        EnumMap<EmployStatus, Integer> statusCount = new EnumMap<>(EmployStatus.class);

        Arrays.stream(EmployStatus.values()).forEach(status -> statusCount.put(status, 0));

        pages.getContent().forEach(profile -> {
            EmployStatus status = profile.getEmployStatus();
            if (status != null){
                statusCount.merge(status, 1, Integer::sum);
            }
        });

        int[] filteredArray = Arrays.stream(EmployStatus.values())
                .mapToInt(statusCount::get)
                .toArray();

        return TechmapProjectProfilePageDto.builder()
                .profilePreviews(profilePreviews)
                .filteredArray(filteredArray)
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .currentPage(pages.getNumber())
                .size(pages.getSize())
                .build();
    }

}
