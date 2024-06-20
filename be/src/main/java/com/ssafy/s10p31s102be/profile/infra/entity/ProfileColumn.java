package com.ssafy.s10p31s102be.profile.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.profile.infra.enums.ProfileColumnDataType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class ProfileColumn extends BaseTimeEntity {

    @Builder
    public ProfileColumn(String name, String label, ProfileColumnDataType dataType, Boolean required, Boolean isPreview) {
        this.name = name;
        this.label = label;
        this.dataType = dataType;
        this.required = required;
        this.isPreview = isPreview;
    }

    @Id
    private String name;

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileColumnDataType dataType;

    @Column(nullable = false)
    private Boolean required;

    @Column(nullable = false)
    private Boolean isPreview;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileColumn")
    private List<ProfileColumnData> profileColumnDatas = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileColumn")
    private List<ProfileColumnDictionary> profileColumnDictionaries = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CountryDisabledColumn> countryDisabledColumns = new ArrayList<>();
}