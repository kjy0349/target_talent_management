package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationCreateDto;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;

public interface AdminScheduledService {

    void fetch( SystemNotificationCreateDto dto );

    //인재풀 최신화 알림, 담당자 인재풀 업데이트 알림
    void notifyPoolUserProfileNotUpdated(SystemNotificationCreateDto dto);
    //채용 부서장 용 비업데이트 담당자 알림
    void notifyHeadRecruiterPoolNotDoneMember(SystemNotificationCreateDto dto);
    //자신이 관리하는 네트워킹 활동 매핑 완료 건 수 조회.
    void notifyAllNetworkingMappingDone(SystemNotificationCreateDto dto);
    // 네트워킹 매핑 진행경과 알림
    void notifyAllNetworkingMappingNotUpdated(SystemNotificationCreateDto dto);

    //추가 가능한 동적 주기 알림 서비스
    void notifyTargetMembersByDynamicRequest( SystemNotificationCreateDto dto );

}
