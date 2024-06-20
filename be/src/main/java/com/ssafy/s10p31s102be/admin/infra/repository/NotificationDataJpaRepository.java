package com.ssafy.s10p31s102be.admin.infra.repository;

import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationData;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationDataJpaRepository extends JpaRepository<NotificationData, Integer> {

    @Modifying
    @Query("delete from NotificationData nd where nd.id = :id")
    void deleteById(@Param( value = "id") Integer id );

    @Query("select nd from NotificationData nd where nd.notification.notificationType = :status and nd.dataId = :dataId")
    Optional<NotificationData> getAllNotificationNoReadableTypeByDataAndDataType(@Param(value = "status") NotificationType dataType, @Param( value = "dataId") Integer dataId );

}
