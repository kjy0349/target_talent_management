package com.ssafy.s10p31s102be.techmap.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class TechMainCategory {
    public TechMainCategory(String techMainCategoryName) {
        this.techMainCategoryName = techMainCategoryName;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String techMainCategoryName;
}
