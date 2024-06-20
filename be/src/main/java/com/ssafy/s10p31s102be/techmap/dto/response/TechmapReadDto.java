package com.ssafy.s10p31s102be.techmap.dto.response;

import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import static com.ssafy.s10p31s102be.techmap.infra.entity.QTechmapProject.techmapProject;

@Data
@Builder
@Getter
public class TechmapReadDto {
    private Integer techmapId;
    private Integer targetYear;
    private String info;
    private String description;
    private LocalDateTime dueDate;
    private LocalDateTime createDate;
    private List<String> keywords;
    private List<String> departments;

    public static TechmapReadDto fromEntity(Techmap techmap){
        Map<TechCompanyRelativeLevel, Long> techCompanyRelativeLevelCounts = techmap.getTechmapProjects().stream()
                .collect(Collectors.groupingBy(
                        TechmapProject::getTechCompanyRelativeLevel, // 프로젝트에서 TechStatus 가져오기
                        Collectors.counting()          // 개수 세기
                ));

        Map<TechCompanyRelativeLevel, Long> counts = initializeTechLevelCounts();
        counts.putAll(techCompanyRelativeLevelCounts);

        String techCompanyRelativeLevelCountsStr = counts.entrySet().stream()
                .map(entry -> TechCompanyRelativeLevelDescriptions.get(entry.getKey()) + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        return TechmapReadDto.builder()
                .techmapId(techmap.getId())
                .targetYear(techmap.getTargetYear())
                .info("총 기술 분야: " + techmap.getTechmapProjects().size() + ", " + techCompanyRelativeLevelCountsStr)
                .description(techmap.getDescription())
                .dueDate(techmap.getDueDate())
                .createDate(techmap.getCreatedAt())
                .keywords(techmap.getTechmapKeywords().stream()
                        .map(techmapKeyword -> techmapKeyword.getKeyword().getData()).toList())
                .departments(techmap.getTechmapDepartments().stream()
                        .map(techmapDepartment -> techmapDepartment.getDepartment().getName()).toList())
                .build();
    }

    public static TechmapReadDto fromEntity(Techmap techmap, Integer departmentId) {
        List<TechmapProject> filteredProjects = techmap.getTechmapProjects().stream()
                .filter(rp -> rp.getDepartmentId().equals(departmentId))
                .toList();

        Map<TechCompanyRelativeLevel, Long> techCompanyRelativeLevelCounts = filteredProjects.stream()
                .collect(Collectors.groupingBy(
                        TechmapProject::getTechCompanyRelativeLevel, // 프로젝트에서 TechStatus 가져오기
                        Collectors.counting()          // 개수 세기
                ));

        Map<TechCompanyRelativeLevel, Long> counts = initializeTechLevelCounts();
        counts.putAll(techCompanyRelativeLevelCounts);

        String techCompanyRelativeLevelCountsStr = counts.entrySet().stream()
                .map(entry -> TechCompanyRelativeLevelDescriptions.get(entry.getKey()) + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        return TechmapReadDto.builder()
                .techmapId(techmap.getId())
                .targetYear(techmap.getTargetYear())
                .info("총 기술 분야: " + filteredProjects.size() + ", " + techCompanyRelativeLevelCountsStr)
                .description(techmap.getDescription())
                .dueDate(techmap.getDueDate())
                .createDate(techmap.getCreatedAt())
                .keywords(techmap.getTechmapKeywords().stream()
                        .map(techmapKeyword -> techmapKeyword.getKeyword().getData()).toList())
                .departments(techmap.getTechmapDepartments().stream()
                        .map(techmapDepartment -> techmapDepartment.getDepartment().getName()).toList())
                .build();
    }

    private static Map<TechCompanyRelativeLevel, Long> initializeTechLevelCounts() {
        Map<TechCompanyRelativeLevel, Long> counts = new EnumMap<>(TechCompanyRelativeLevel.class);
        for (TechCompanyRelativeLevel level : TechCompanyRelativeLevel.values()) {
            counts.put(level, 0L);
        }
        return counts;
    }

    private static final Map<TechCompanyRelativeLevel, String> TechCompanyRelativeLevelDescriptions = new EnumMap<>(TechCompanyRelativeLevel.class);

    static {
        TechCompanyRelativeLevelDescriptions.put(TechCompanyRelativeLevel.NORMAL, "동등");
        TechCompanyRelativeLevelDescriptions.put(TechCompanyRelativeLevel.OUTNUMBERED, "열세");
        TechCompanyRelativeLevelDescriptions.put(TechCompanyRelativeLevel.SUPERIOR, "우세");
    }
}
