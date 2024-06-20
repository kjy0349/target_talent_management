package com.ssafy.s10p31s102be.member.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.TeamAdminUpdateDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Team(String name, String description, Department department) {
        this.name = name;
        this.description = description;
//        this.department = department;
    }

    private String name;

    private String description;

    private Boolean isDeleted = false;

//    @ManyToOne
//    @JoinColumn(name = "department_id")
//    private Department department;

    public void update(TeamAdminUpdateDto dto) {
        this.name = name;
        this.description = description;
    }
//    public void updateDepartment( Department department ){
//        this.department = department;
//    }

    public void delete() {
        this.isDeleted = true;
    }

//    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
//    private List<Member> members = new ArrayList<>();
}
