package com.ssafy.s10p31s102be.techmap.infra.entity;

import com.ssafy.s10p31s102be.member.infra.entity.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TechmapDepartment {
    public TechmapDepartment(Techmap techmap, Department department) {
        this.techmap = techmap;
        this.department = department;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "techmap_id" )
    private Techmap techmap;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn( name = "department_id")
    private Department department;
}
