package com.ssafy.s10p31s102be.admin.service;

import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminMemberCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.SystemNotificationCreateDto;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import com.ssafy.s10p31s102be.admin.infra.repository.SystemNotificaitonJpaRepository;
import com.ssafy.s10p31s102be.common.exception.SystemNotificationNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.admin.infra.repository.NotificationJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminScheduledServiceImpl implements AdminScheduledService{
    private final String topicName = "notification";
    private final NotificationJpaRepository notificationRepository;
    private final MemberJpaRepository memberRepository;
    private final KafkaNotificationProducerService producerService;
    private final ProfileJpaRepository profileRepository;
    private final NetworkingJpaRepository networkingRepository;
    private final NetworkingProfileJpaRepository networkingProfileJpaRepository;
    private final SystemNotificaitonJpaRepository systemNotificaitonRepository;
    //전사 알림은 기본적으로 업무가 끝나는 토요일 자정 12시를 기준으로 설정.

    @Override
    public void fetch( SystemNotificationCreateDto dto ){
        switch (dto.getIdx()){
            case 0:
                notifyPoolUserProfileNotUpdated(dto);
                break;
            case 1:
                notifyPoolUserProfileNotUpdated( dto );
                break;
            case 2:
                notifyHeadRecruiterPoolNotDoneMember( dto );
                break;
            case 3:
                notifyAllNetworkingMappingDone( dto );
                break;
            case 4:
                notifyAllNetworkingMappingNotUpdated( dto );
                break;
            default:
                notifyTargetMembersByDynamicRequest( dto );
        }
    }

    @Override
    public void notifyPoolUserProfileNotUpdated(SystemNotificationCreateDto dto) {
        //지난 x주간 최신화 하지 않은 후보자 프로필이 00건 있습니다.
        final String format = dto.getContent();
        SystemNotification systemNotification = systemNotificaitonRepository.findSystemNotificaitonByIdx( dto.getIdx() ).orElseThrow( () -> new SystemNotificationNotFoundException(dto.getIdx(), this, ""));
        systemNotification.updateSendedAt();
        List<Member> members = memberRepository.findMembersForNotification();
        LocalDateTime now = LocalDateTime.now();
        members.forEach( member -> {
            AtomicInteger count = new AtomicInteger(0);
            List<Integer> tartgetProfileIds = new ArrayList<>();
            if( member.getAuthority() == null ){
                return;
            }
            List<Profile> profiles = profileRepository.findAllProfilesByMemberId( member.getId() );
            profiles.forEach( profile -> {
                Period period = Period.between( profile.getModifiedAt() == null ? profile.getCreatedAt().toLocalDate() : profile.getModifiedAt().toLocalDate(), now.toLocalDate() );
                int weeks = period.getDays() / 7;
                if( weeks >= dto.getCalculateWeek() || period.getYears() > 0){
                    tartgetProfileIds.add( profile.getId() );
                    count.addAndGet(1);
                }
            });
            String content = String.format( format, dto.getCalculateWeek(), count.get());
            System.out.println( "!!!:" + String.format( format, dto.getCalculateWeek(), count.get()));
            NotificationAdminCreateDto createDto = NotificationAdminCreateDto.builder()
                    .content(content)
                    .member( NotificationAdminMemberCreateDto.fromEntity(member) )
                    .data(tartgetProfileIds)
                    .notificationDataType(NotificationDataType.PROFILE)
                    .notificationType(NotificationType.SYSTEM)
                    .senderName("관리자")
                    .build();

            producerService.sendNotificationToKafkaTopic( createDto );
        });


    }

    @Override
    public void notifyHeadRecruiterPoolNotDoneMember(SystemNotificationCreateDto dto) {
        //지난 x주간 인재Pool을 업데이트 하지 않은 담당자가 00명 입니다.

        final String format = dto.getContent();
        SystemNotification systemNotification = systemNotificaitonRepository.findSystemNotificaitonByIdx( dto.getIdx() ).orElseThrow( () -> new SystemNotificationNotFoundException(dto.getIdx(), this, ""));
        systemNotification.updateSendedAt();
        List<Member> members = memberRepository.findHeadRecruitersForNotification();
        LocalDateTime now = LocalDateTime.now();
        members.forEach( head -> {
            List<Integer> targetMemberIds= new ArrayList<>();
            AtomicInteger count = new AtomicInteger(0);
            if( head.getAuthority() == null ){
                return;
            }
            List<Member> findMembers = memberRepository.findMembersByDepartmentId( head.getDepartment().getId() );
            findMembers.forEach( findMember -> {
                //나는 볼 필요가 없음.
                if( findMember.getId().equals( head.getId())){
                    return;
                }
                Period period = Period.between( findMember.getLastProfileUpdateDate() == null ? findMember.getCreatedAt().toLocalDate() : findMember.getLastProfileUpdateDate().toLocalDate(), now.toLocalDate() );
                int weeks = period.getDays() / 7;
                if( weeks >= dto.getCalculateWeek() || period.getYears() > 0) {
                    targetMemberIds.add( findMember.getId());
                    count.addAndGet(1);
                }
            });
            System.out.println( "!!!:" + String.format( format, dto.getCalculateWeek(), count.get()));
            String content = String.format( format, dto.getCalculateWeek(), count.get());
            NotificationAdminCreateDto createDto = NotificationAdminCreateDto.builder()
                    .content( content )
                    .member( NotificationAdminMemberCreateDto.fromEntity(head) )
                    .data(targetMemberIds)
                    .notificationDataType(NotificationDataType.MEMBER)
                    .notificationType(NotificationType.SYSTEM)
                    .senderName("관리자")
                    .build();

            producerService.sendNotificationToKafkaTopic( createDto );
        });
    }

    @Override
    public void notifyAllNetworkingMappingDone(SystemNotificationCreateDto dto) {
        // 지난 x주간 네트워킹 매핑이 완료된 건은 총 00명 입니다.

        final String format = dto.getContent();
        SystemNotification systemNotification = systemNotificaitonRepository.findSystemNotificaitonByIdx( dto.getIdx() ).orElseThrow( () -> new SystemNotificationNotFoundException(dto.getIdx(), this, ""));
        systemNotification.updateSendedAt();
        List<Member> members = memberRepository.findMembersForNotification();

        members.forEach( member -> {
            ArrayList<Integer> targetProfileIds = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(0);
            if( member.getAuthority() == null ){
                return;
            }
            List<Profile> profiles = profileRepository.findAllProfilesByMemberId( member.getId() );
            profiles.forEach( profile -> {
                List<NetworkingProfile> nps = networkingProfileJpaRepository.findAllByProfile( profile );
                for( NetworkingProfile np : nps ){
                    if( np.getNetworking() == null ){
                        continue;
                    }
                    if( np.getNetworking().getNetworkingStatus().equals(NetworkingStatus.DOING_NETWORKING)){
                        Period period = Period.between( np.getNetworking().getCreatedAt() == null ? LocalDate.now() : np.getNetworking().getModifiedAt().toLocalDate(), LocalDate.now() );
                        int weeks = period.getDays() / 7;
                        if( weeks <= dto.getCalculateWeek() && period.getYears() < 0) {
                            if( !targetProfileIds.contains( profile.getId() ) ){
                                targetProfileIds.add( profile.getId() );
                                count.addAndGet(1);
                            }

                        }
                    }
                }

            });
            System.out.println( "!!!:" + String.format( format, dto.getCalculateWeek(), count.get()) + count.get());
            String content = String.format( format, count.get());
            NotificationAdminCreateDto createDto = NotificationAdminCreateDto.builder()
                    .content( content )
                    .member( NotificationAdminMemberCreateDto.fromEntity(member) )
                    .data(targetProfileIds)
                    .notificationDataType(NotificationDataType.PROFILE)
                    .notificationType(NotificationType.SYSTEM)
                    .senderName("관리자")
                    .build();
            producerService.sendNotificationToKafkaTopic( createDto );
        });
    }

    @Override
    public void notifyAllNetworkingMappingNotUpdated(SystemNotificationCreateDto dto) {
        // 지난 x주간 업데이트 되지 않은 네트워킹 활동은 00건 입니다.
        SystemNotification systemNotification = systemNotificaitonRepository.findSystemNotificaitonByIdx( dto.getIdx() ).orElseThrow( () -> new SystemNotificationNotFoundException(dto.getIdx(), this, ""));
        systemNotification.updateSendedAt();
        final String format = dto.getContent();
        List<Member> members = memberRepository.findMembersForNotification();
        LocalDateTime now = LocalDateTime.now();
        members.forEach(member -> {
            List<Integer> targetNetworkingIds = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(0);
            if (member.getAuthority() == null) {
                return;
            }
            List<Networking> networkings = networkingRepository.findNetworkingsByMemberId(member.getId());
            networkings.forEach(networking -> {
                Period period = Period.between(networking.getModifiedAt() == null ? networking.getCreatedAt().toLocalDate() : networking.getModifiedAt().toLocalDate(), now.toLocalDate());
                int weeks = period.getDays() / 7;
                if (weeks >= dto.getCalculateWeek() || period.getYears() > 0) {
                    targetNetworkingIds.add(networking.getId());
                    count.addAndGet(1);
                }
            });
            String content = String.format(format, dto.getCalculateWeek(), count.get()); // format 변수를 사용하여 포맷 문자열을 대체
            System.out.println("!!!:" + content);
            NotificationAdminCreateDto createDto = NotificationAdminCreateDto.builder()
                    .content(content) // 생성 시 대체된 content 값을 사용
                    .member(NotificationAdminMemberCreateDto.fromEntity(member))
                    .data(targetNetworkingIds)
                    .notificationDataType(NotificationDataType.NETWORKING)
                    .notificationType(NotificationType.SYSTEM)
                    .senderName("관리자")
                    .build();
            System.out.println("dto : " + createDto.toString());
            producerService.sendNotificationToKafkaTopic(createDto);
        });
    }

    @Override
    public void notifyTargetMembersByDynamicRequest(SystemNotificationCreateDto dto) {
        List<Member> members = memberRepository.findAll();
        SystemNotification systemNotification = systemNotificaitonRepository.findSystemNotificaitonByIdx( dto.getIdx() ).orElseThrow( () -> new SystemNotificationNotFoundException(dto.getIdx(), this, ""));
        systemNotification.updateSendedAt();
        members.forEach( member -> {
            NotificationAdminCreateDto createDto = NotificationAdminCreateDto.builder()
                    .content(dto.getContent())
                    .member( NotificationAdminMemberCreateDto.fromEntity(member) )
                    .data(null)
                    .notificationDataType(NotificationDataType.NONE)
                    .notificationType(NotificationType.SYSTEM)
                    .senderName("관리자")
                    .build();
            producerService.sendNotificationToKafkaTopic( createDto );
        });
    }
}
