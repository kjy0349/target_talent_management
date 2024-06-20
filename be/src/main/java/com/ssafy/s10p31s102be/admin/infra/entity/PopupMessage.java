package com.ssafy.s10p31s102be.admin.infra.entity;

import com.ssafy.s10p31s102be.common.infra.enums.TextOption;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PopupMessage  extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public PopupMessage(String content, Boolean isUsed, LocalDateTime startDate, LocalDateTime endDate,
                        Authority viewAuthority) {
        this.content = content;
        this.isUsed = isUsed;
        this.startDate = startDate;
        this.endDate = endDate;
        this.viewAuthority = viewAuthority;
    }

    @Lob
    @Size(max = 1000)
    private String content;

    private Boolean isUsed;

    private Boolean isPreSaved;

    private Boolean isDeleted;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Embedded
    private TextOption textOption;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn( name = "view_authority_id" )
    private Authority viewAuthority;

    public void updatePopupMessage(String content, Boolean isUsed, LocalDateTime startDate, LocalDateTime endDate,
                                           Authority viewAuthority){
        this.content = content;
        this.isUsed = isUsed;
        this.startDate = startDate;
        this.endDate = endDate;
        this.viewAuthority = viewAuthority;
    }
}
