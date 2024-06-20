package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import com.ssafy.s10p31s102be.admin.infra.repository.SystemNotificaitonJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminInitService {
    private final SystemNotificaitonJpaRepository systemNotificationRepository;


    @PostConstruct
    public void settingSystemNotificaiton(){
        SystemNotification systemNotification1 = SystemNotification.builder()
                .title("인재풀 최신화 알림")
                .content("%d주를 넘어 최신화 하지 않은 프로필이 %d건 있습니다.")
                .idx(0)
                .calculateWeek(1)
                .period(1)
                .lastSendedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        systemNotification1.setModifiedAt(LocalDateTime.now());
        SystemNotification systemNotification2 = SystemNotification.builder()
                .title("인재 풀 신규 등록 실적 알림")
                .content("지난 %d주간 업데이트를 하지 않으셨습니다.")
                .idx(1)
                .calculateWeek(1)
                .period(1)
                .lastSendedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        systemNotification2.setModifiedAt(LocalDateTime.now());
        SystemNotification systemNotification3 = SystemNotification.builder()
                .title("인재 풀 소속 담당자 실적 알림")
                .content("지난 %d주간 업데이트를 하지 않은 담당자가 %d명 입니다.")
                .idx(2)
                .calculateWeek(1)
                .period(1)
                .lastSendedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        systemNotification3.setModifiedAt(LocalDateTime.now());
        SystemNotification systemNotification4 = SystemNotification.builder()
                .title("네트워크 후보자 매핑")
                .content("담당 인재중 매핑 완료건은 %d건 입니다.")
                .idx(3)
                .calculateWeek(1)
                .period(1)
                .lastSendedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        systemNotification4.setModifiedAt(LocalDateTime.now());
        SystemNotification systemNotification5 = SystemNotification.builder()
                .title("네트워킹 매핑 진행 경과")
                .content("지난 %d주간 업데이트 되지 않은 활동은 %d건입니다.")
                .idx(4)
                .calculateWeek(1)
                .period(1)
                .lastSendedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        systemNotification5.setModifiedAt(LocalDateTime.now());
        SystemNotification[] systemNotifications = new SystemNotification[]{ systemNotification1, systemNotification2, systemNotification3, systemNotification4, systemNotification5 };
        for( int i = 0; i < 5; i ++ ){
            SystemNotification targetNotificaiton = systemNotifications[i];
            Optional<SystemNotification> findNotification = systemNotificationRepository.findSystemNotificaitonByIdx(targetNotificaiton.getIdx()) ;
            if(findNotification.isEmpty()) {
                systemNotificationRepository.save(targetNotificaiton);
            }
        }
    }
}
