package com.ssafy.s10p31s102be.common.infra.entity;

import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KeywordType type;

    @Column(nullable = false)
    private String data;

    private Long count = 0L;

    public Keyword(KeywordType type, String data) {
        this.type = type;
        this.data = data;
    }

    public void use() {
        this.count += 1;
    }

    public void drop(){
        this.count -= 1;
    }
}
