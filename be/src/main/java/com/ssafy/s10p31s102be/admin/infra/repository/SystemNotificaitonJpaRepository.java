package com.ssafy.s10p31s102be.admin.infra.repository;

import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemNotificaitonJpaRepository extends JpaRepository<SystemNotification, Integer> {
    @Query("select sn from SystemNotification  sn where sn.idx = :idx")
    public Optional<SystemNotification> findSystemNotificaitonByIdx(@Param("idx") Integer idx );

    @Modifying
    @Query("delete from SystemNotification sn where sn.id = :id")
    public void deleteNotificationById( @Param("id") Integer id );

    @Query("select max( sn.idx ) from SystemNotification sn ")
    public Integer getMaxIdx();
}
