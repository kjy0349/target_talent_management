package com.ssafy.s10p31s102be.common.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Excel {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Excel(String name, String targetYear) {
        this.name = name;
        this.targetYear = targetYear;
    }

    private String name;
    private String targetYear;
}
