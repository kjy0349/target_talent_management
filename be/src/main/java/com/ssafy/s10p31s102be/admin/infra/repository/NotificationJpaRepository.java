package com.ssafy.s10p31s102be.admin.infra.repository;

import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Integer> {

    @Query("select n from Notification n where n.member.id = :memberId and n.isRead = false order by n.createdAt desc")
    List<Notification> getAllNotificationsByAuthentication( @Param(value = "memberId") Integer memberId );


    @Query("select n from Notification n where n.notificationType = :status and n.isRead = false")
    List<Notification>  getAllPersonalNotification(@Param(value = "status") NotificationType notificationType );



    @Modifying
    @Query("delete from Notification n where n.id = :notificationId")
    void deleteById( @Param( value = "notificationId") Integer notificationId );
}
