package com.ssafy.s10p31s102be.member.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SecuritySign {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    private LocalDateTime enrollDate;

    @OneToOne
    @JoinColumn(name = "security_sign_id")
    private SecuritySign securitySign;
}
