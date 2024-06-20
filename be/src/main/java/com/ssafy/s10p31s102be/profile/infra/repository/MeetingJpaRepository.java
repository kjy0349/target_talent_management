package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingJpaRepository extends JpaRepository<Meeting,Integer> {
    @Query("SELECT m FROM Meeting m " +
            "join fetch m.profile mp " +
            "where m.profile.id=:profileId")
    List<Meeting> findMeetingsByProfileId(@Param("profileId") Integer profileId);
}
