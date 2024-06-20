package com.ssafy.s10p31s102be.profile.infra.entity;

import jakarta.persistence.*;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Country {

    @Builder
    public Country(String name, String code, String continent) {
        this.name = name;
        this.code = code;
        this.continent = continent;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String code;

    private String continent;

    @OneToMany(mappedBy = "country")
    private List<CountryDisabledColumn> countryDisabledColumns = new ArrayList<>();
}
