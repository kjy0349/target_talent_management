package com.ssafy.s10p31s102be.member.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.RoleAdminUpdateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Role(String description) {
        this.description = description;
    }

    private String description;

//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
//    private List<Member> members = new ArrayList<>();

    private Boolean isDeleted = false;

    public void update(RoleAdminUpdateDto dto) {
        this.description = dto.getDescription();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
