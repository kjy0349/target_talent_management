package com.ssafy.s10p31s102be.common.infra.enums;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TextOption {

    @Enumerated( EnumType.STRING )
    TextEffect textEffect;

    @Enumerated( EnumType.STRING )
    TextSort textSort;

    String textColor;
}
