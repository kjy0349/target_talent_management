package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentHistoryType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
public class EmploymentHistory extends BaseTimeEntity {

    @Builder
    public EmploymentHistory(EmploymentHistoryType type, String step, String result, String targetDepartmentName, String targetJobRankName, String executiveName, LocalDateTime targetEmployDate, String description, Profile profile, Member member) {
        this.type = type;
        this.step = step;
        this.result = result;
        this.targetDepartmentName = targetDepartmentName;
        this.targetJobRankName = targetJobRankName;
        this.executiveName = executiveName;
        this.targetEmployDate = targetEmployDate;
        this.description = description;
        this.profile = profile;
        this.memberDepartment = member.getDepartment().getName();
        this.memberName = member.getName();

        this.isFavorite = Boolean.FALSE;
        this.modifiedAt = LocalDateTime.now();
    }

    // TODO: 타입에 맞게 명확한 Update 로직 분리 필요
    public void update(String result, String targetDepartmentName, String targetJobRankName, String executiveName, LocalDateTime targetEmployDate, String description) {
        this.result = result;
        this.targetDepartmentName = targetDepartmentName;
        this.targetJobRankName = targetJobRankName;
        this.executiveName = executiveName;
        this.targetEmployDate = targetEmployDate;
        this.description = description;

        this.modifiedAt = LocalDateTime.now();
    }

    public void updateFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

//    public void updateNegotiation(String targetJobRankName, LocalDateTime targetEmployDate, String description) {
//        this.targetJobRankName = targetJobRankName;
//        this.targetEmployDate = targetEmployDate;
//        this.description = description;
//
//        this.modifiedAt = LocalDateTime.now();
//    }


    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentHistoryType type;

    private String step;

    private String result;

    private String targetDepartmentName;

    private String targetJobRankName;

    private String executiveName;

    private LocalDateTime targetEmployDate;

    private Boolean isFavorite;

    @Lob
    @Size(max = 1000)
    private String description;

    private String memberDepartment;

    private String memberName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
