package com.ssafy.s10p31s102be.admin.infra.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationData {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer dataId;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "notification_id")
    @Setter
    @JsonIgnore
    private Notification notification;


    public static NotificationData from(Integer dataId) {
        return NotificationData.builder()
                .dataId(dataId)
                .build();

    }
}
