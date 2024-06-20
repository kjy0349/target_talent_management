package com.ssafy.s10p31s102be.member.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.DepartmentAdminUpdateDto;
import com.ssafy.s10p31s102be.board.infra.entity.BoardDepartment;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Department {
    @Builder
    public Department(String name, String description, Member manager) {
        this.name = name;
        this.description = description;
        this.manager = manager;
    }

    @Id
    @GeneratedValue
    private Integer id;

    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setManagerToDepartment(Member manager) {
        this.manager = manager;
    }

    private String name;

    private String description;

    @OneToMany(mappedBy = "department")
    private List<Member> members = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Member manager;

//    @OneToMany(mappedBy="department")
//    private List<Team> teams = new ArrayList<>();

    private Boolean isDeleted = false;


    public void update(DepartmentAdminUpdateDto dto, List<Member> members, Member manager) {
        this.members = members;
        this.manager = manager;
        this.name = dto.getName();
        this.description = dto.getDescription();
    }

    public void delete(){
        this.isDeleted = true;
        this.manager = null;
    }

    @OneToMany(mappedBy = "department")
    private List<BoardDepartment> boardDepartments = new ArrayList<>();
}
