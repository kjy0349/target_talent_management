package com.ssafy.s10p31s102be.profile.infra.entity.career;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectCompany;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Company extends BaseTimeEntity {

    @Builder
    public Company(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private List<TechmapProjectCompany> techmapProjectCompanies = new ArrayList<>();
}