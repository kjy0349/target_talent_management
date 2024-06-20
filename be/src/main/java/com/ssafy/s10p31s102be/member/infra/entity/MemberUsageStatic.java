package com.ssafy.s10p31s102be.member.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class MemberUsageStatic {



    @Id
    @Setter
    private Integer id;

    private Integer count = 0;

    private Integer visitorCounts = 0;

    public void updateCount() {
        this.count++;
    }

    public void updateVisitors(){
        this.visitorCounts++;
    }
}
