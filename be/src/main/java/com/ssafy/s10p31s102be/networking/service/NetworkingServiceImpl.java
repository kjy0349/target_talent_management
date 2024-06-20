package com.ssafy.s10p31s102be.networking.service;

import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminCreateDto;
import com.ssafy.s10p31s102be.admin.dto.request.NotificationAdminMemberCreateDto;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationData;
import com.ssafy.s10p31s102be.admin.infra.entity.NotificationDataType;
import com.ssafy.s10p31s102be.admin.infra.enums.NotificationType;
import com.ssafy.s10p31s102be.admin.infra.repository.NotificationDataJpaRepository;
import com.ssafy.s10p31s102be.admin.infra.repository.NotificationJpaRepository;
import com.ssafy.s10p31s102be.admin.service.KafkaNotificationProducerService;
import com.ssafy.s10p31s102be.admin.service.NotificationServiceImpl;
import com.ssafy.s10p31s102be.common.exception.InternalServerException;
import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.exception.NotificationNotFoundException;
import com.ssafy.s10p31s102be.common.util.ExcelUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.ExecutiveNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberRepositoryCustom;
import com.ssafy.s10p31s102be.networking.dto.request.*;
import com.ssafy.s10p31s102be.networking.dto.response.ExecutiveSearchResultDto;
import com.ssafy.s10p31s102be.networking.dto.response.NetworkingSearchResultDto;
import com.ssafy.s10p31s102be.networking.exception.NetworkingNotFoundException;
import com.ssafy.s10p31s102be.networking.exception.NotificationDataNotFoundException;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingProfileJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingRepositoryCustom;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NetworkingServiceImpl implements NetworkingService{

    private final NetworkingJpaRepository networkingRepository;
    private final MemberJpaRepository memberRepository;
    private final ProfileJpaRepository profileRepository;
    private final ExecutiveJpaRepository executiveRepository;
    private final NetworkingProfileJpaRepository networkingProfileRepository;
    private final NetworkingRepositoryCustom networkingRepositoryCustom;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final DepartmentJpaRepository departmentRepository;
    private final KafkaNotificationProducerService kafkaNotificationProducerService;
    private final ExcelUtil excelUtil;
    private final NotificationJpaRepository notificationRepository;
    private final NotificationDataJpaRepository notificationDataRepository;

    @Override
    public Networking createNetworking(UserDetailsImpl userDetails, NetworkingCreateDto dto) {
        
        Executive executive = executiveRepository.findById( dto.getExecutiveId() )
                .orElseThrow( () -> new ExecutiveNotFoundException( dto.getExecutiveId(), this));
        Member member = memberRepository.findById( dto.getMemberId() )
                .orElseThrow( () -> new MemberNotFoundException( dto.getMemberId(), this ));
        Networking networking = Networking.builder()
                .networkingStatus(NetworkingStatus.READY_NETWORKING)
                .category(dto.getCategory())
                .member( member )
                .executive(executive)

                .build();
        networking.setModifiedAt(LocalDateTime.now());
        networkingRepository.save( networking );
        return networking;
    }

    @Override
    public List<Networking> getAllNetworkings(UserDetailsImpl userDetails) {
        List<Networking> networkings = networkingRepository.findAll();
        System.out.println(networkings.size());
        if( userDetails.getAuthorityLevel() > 2 ){
            networkings = networkings.stream().filter( networking -> networking.getMember().getDepartment().getId().equals(userDetails.getDepartmentId())).collect(Collectors.toList());
        }

        return networkings;
    }

    @Override
    public NetworkingSearchResultDto getAllNetworkingsWithFilter(UserDetailsImpl userDetails, NetworkingSearchCondition searchConditionDto, Pageable pageable) {
        Integer departmentId = ( searchConditionDto == null ) || searchConditionDto.getDepartmentId() == null || ( searchConditionDto.getDepartmentId() != null && searchConditionDto.getDepartmentId() == 0 )? null :searchConditionDto.getDepartmentId();
        //운영진 아래라면 자기 부서만
        if( userDetails.getAuthorityLevel() > 2 ){
            departmentId = userDetails.getDepartmentId();
        }

        Page<Networking> networkings = networkingRepositoryCustom.getAllNetworkingsWithFilter(
                departmentId,
                ( searchConditionDto == null ) || searchConditionDto.getTargetYear() == null || ( searchConditionDto.getTargetYear() != null && searchConditionDto.getTargetYear() == 0 )? null :searchConditionDto.getTargetYear(),
                searchConditionDto == null ? null :searchConditionDto.getCategory(),
                pageable );
        return NetworkingSearchResultDto.fromEntity( networkings );
    }
    @Override
    public List<Profile> getAllProfilesNotNetworked(UserDetailsImpl userDetails) {
        return profileRepository.findAllProfilesWithProfileColumnDatasAndNotAndNetworkingNot();
    }

    @Override
    public Networking updateExecutive(UserDetailsImpl userDetails, Integer networkingId, NetworkingUpdateDto dto) {

        Networking networking = networkingRepository.findById( networkingId )
                .orElseThrow( () -> new NetworkingNotFoundException( networkingId,this));
        validUser( userDetails, networking );
        Executive executive = executiveRepository.findById( dto.getExecutiveId() )
                .orElseThrow( () -> new ExecutiveNotFoundException( dto.getExecutiveId(), this));
        networking.setModifiedAt(LocalDateTime.now());
        networking.update(executive);
        return networking;

    }

    @Override
    public Networking createNetworkingWithProfile(UserDetailsImpl userDetails, Integer profileId, NetworkingCreateDto dto) {
        Executive executive = executiveRepository.findById( dto.getExecutiveId() )
                .orElseThrow( () -> new ExecutiveNotFoundException( dto.getExecutiveId(), this));
        Member member = memberRepository.findById( dto.getMemberId() )
                .orElseThrow( () -> new MemberNotFoundException( dto.getMemberId(), this ));
        Networking networking = Networking.builder()
                .networkingStatus(NetworkingStatus.READY_NETWORKING)
                .category(dto.getCategory())
                .member( member )
                .executive(executive)

                .build();
        Profile profile = profileRepository.findById( profileId ).orElseThrow(() -> new ProfileNotFoundException( profileId, this )) ;
        NetworkingProfile networkingProfile = new NetworkingProfile( networking, profile );
        networking.getNetworkingProfiles().add(networkingProfile);
        networking.setModifiedAt(LocalDateTime.now());
        networkingRepository.save( networking );

        return networking;
    }

    @Override
    public void submitNetworking(UserDetailsImpl userDetails, NetworkingSubmitDto networkingSubmitDto) {
        networkingSubmitDto.getNetworkingIds().forEach( networkingId -> {

            Networking networking = networkingRepository.findById(networkingId)
                    .orElseThrow( () -> new NetworkingNotFoundException(networkingId,this));
            validUser( userDetails, networking );
            Member head = networking.getMember().getDepartment().getManager();
            if( head == null ) throw new InternalServerException("채용 부서장 설정이 되어있지 않습니다. 부서의 설정을 변경해주세요.", this);
            List<Integer> data = new ArrayList<>();
            data.add(networkingId);
            NotificationAdminCreateDto notificationAdminCreateDto =
                    NotificationAdminCreateDto.builder()
                            .notificationType(NotificationType.NETWOKRING_APPROAVE)
                            .member(NotificationAdminMemberCreateDto.fromEntity(head))
                            .notificationDataType(NotificationDataType.NETWORKING)
                            .content(userDetails.getUsername() + "님의 네트워킹 승인 요청입니다.")
                            .senderName( userDetails.getUsername() )
                            .data( data )
                            .build();
            kafkaNotificationProducerService.sendNotificationToKafkaTopic( notificationAdminCreateDto );
            networking.updateStatus(NetworkingStatus.READY_APPROVEMENT);
        });

    }

    @Override
    public void approveNetworking(UserDetailsImpl userDetails, Integer networkingId, Integer notificationId) {

        Networking  networking = networkingRepository.findById(networkingId)
                .orElseThrow( () -> new NetworkingNotFoundException( networkingId,this));
        Member head = networking.getMember().getDepartment().getManager();

        Notification notification = notificationRepository.findById( notificationId )
                .orElseThrow(()-> new NotificationNotFoundException( notificationId, this ));

        notification.updateRead();

        if( !userDetails.getMemberId().equals( head.getId() ) ){
            log.warn("주어진 네트워킹을 승인할 권한이 없습니다. networkingId: ", networking.getId() + "-" + userDetails.getUsername() );
            throw new InvalidAuthorizationException(networkingId, this );
        }
        networking.updateStatus( NetworkingStatus.DOING_NETWORKING);
        networking.setModifiedAt(LocalDateTime.now());

        return;

    }

    @Override
    public Boolean checkSubmittableNetworking(UserDetailsImpl userDetails, NetworkingSubmitDto dto) {
        List< Networking > networkings = dto.getNetworkingIds().stream().map( id -> networkingRepository.findById( id ).orElseThrow( () -> new NetworkingNotFoundException( id, this ))).toList();
        System.out.println(dto.getNetworkingIds().toString());
        return networkings.stream().allMatch( n -> n.getNetworkingStatus().equals(NetworkingStatus.READY_NETWORKING));
    }

    @Override
    public ByteArrayResource excelDownload(UserDetailsImpl userDetails, NetworkingExcelDto networkingExcelDto) throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<Networking> networkings = networkingExcelDto.getNetworkingIds().stream().map( networkingId -> networkingRepository.findById(networkingId)
                .orElseThrow(() -> new NetworkingNotFoundException(networkingId, this)) ).toList();

        Integer maxProfiles = 0;

        for( int i = 0; i < networkings.size(); i++){
            maxProfiles = Math.max( maxProfiles, networkings.get( i ).getNetworkingProfiles().size() );
        }

        for (Networking networking: networkings ) {

            Map<String, String> map = new LinkedHashMap<>();

            map.put("분야", networking.getCategory());
            map.put("네트워킹 담당자", networking.getExecutive().getName());
            for( int i = 0; i < maxProfiles; i ++ ){
                if( i >= networking.getNetworkingProfiles().size() ){
                    map.put("후보자" + (i + 1), "비고");
                }else{

                    String name =networking.getNetworkingProfiles().get( i ).getProfile().getProfileColumnDatas().stream()
                            .filter( pc -> pc.getProfileColumn().getName().equals("name")).findFirst().get().getContent();

                    String column1 = networking.getNetworkingProfiles().get( i ).getProfile().getProfileColumnDatas().stream()
                            .filter( pc -> pc.getProfileColumn().getName().equals("column1")).findFirst().get().getContent();
                    Career recentCareer = networking.getNetworkingProfiles().get(i).getProfile().getCareers() == null ? null : networking.getNetworkingProfiles().get(i).getProfile().getCareers().size() == 0 ? null :networking.getNetworkingProfiles().get(i).getProfile().getCareers().get( networking.getNetworkingProfiles().get(i).getProfile().getCareers().size() - 1);
                    Education recentEducation = networking.getNetworkingProfiles().get(i).getProfile().getEducations() == null ? null : networking.getNetworkingProfiles().get(i).getProfile().getEducations().size() == 0 ? null :networking.getNetworkingProfiles().get(i).getProfile().getEducations().get(networking.getNetworkingProfiles().get(i).getProfile().getEducations().size()-1);
                    String companyName = recentCareer == null ? "" : recentCareer.getCompany().getName();
                    LocalDateTime careerStartedAt = recentCareer == null ? null :recentCareer.getStartedAt();
                    LocalDateTime careerEndedAt = (recentCareer == null ? null :recentCareer.getEndedAt());
                    String schoolName = ( recentEducation == null ? null : recentEducation.getSchool() == null ? null :recentEducation.getSchool().getSchoolName());
                    String schoolMajor = (recentEducation == null ? null :recentEducation.getMajor());
                    String schoolDegree = (recentEducation == null ? null: recentEducation.getDegree() == null ? null :recentEducation.getDegree().name());
                    String techDetailName = ( networking.getNetworkingProfiles().get(i).getProfile().getKeyword() != null ? networking.getNetworkingProfiles().get(i).getProfile().getKeyword().getData(): null );
                    String jobRank = networking.getNetworkingProfiles().get(i).getProfile().getTargetJobRank().getDescription();
                    String row1 =  getNullStringvalue(name) + getNullStringvalue( "(" + column1 + ")"+"\r\n" );
                    String row2 =  getNullStringvalue(companyName) +  ", "+ getNullStringvalue(jobRank) +"(" + parseLocalDateTime( careerStartedAt ) + "~" + parseLocalDateTime(careerEndedAt) + ")\r\n";
                    String row3 =  ("(" + schoolName + "), ") + getNullStringvalue(schoolMajor) + ", " + getNullStringvalue(schoolDegree) + "\r\n";
                    String row4 = techDetailName == null ? "": ("분야: " + techDetailName );

                    log.info("export:" +row1 +row2 +row3 +row4);
                    String row =row1 +row2 +row3 +row4;
                    map.put("후보자" + (i + 1), row);
                }

            }

            map.put("현황", getNetworkingStatusKor(networking.getNetworkingStatus() ) );

            dataList.add(map);
        }

        return new ByteArrayResource(excelUtil.mapToExcel(dataList));
    }

    public String parseLocalDateTime( LocalDateTime localDateTime ){
        if(localDateTime == null ) return "비고";
        return localDateTime.getYear() + "." + (localDateTime.getMonthValue() + 1) + "." + localDateTime.getDayOfMonth();
    }

    public String getNullStringvalue( String value ){
        return value == null ? "" : value;
    }

    @Override
    public List<Executive> getAllExecutives(UserDetailsImpl userDetails) {
        return executiveRepository.findAll();
    }



    @Override
    public List<Networking> updateNetworking(UserDetailsImpl userDetails, List<NetworkingUpdateDto> dtos) {

        return dtos.stream().map( dto -> {
            Networking networking = networkingRepository.findById( dto.getNetworkingId() )
                    .orElseThrow( () -> new NetworkingNotFoundException( dto.getNetworkingId(),this));
            Executive executive = executiveRepository.findById( dto.getExecutiveId() )
                    .orElseThrow( () -> new ExecutiveNotFoundException( dto.getExecutiveId(), this));
            Member member = memberRepository.findById( dto.getMemberId() )
                    .orElseThrow( () -> new MemberNotFoundException( dto.getMemberId(), this ));
            updateNetworkingProfiles( dto.getNetworkingId(), dto );
            networking.update( dto.getCategory(), member, dto.getNetworkingStatus(), executive);
            networking.setModifiedAt(LocalDateTime.now());
            return networking;
        }).toList();
    }

    //TODO 중복 코드 리팩토링
    @Override
    public Networking updateNetworking(UserDetailsImpl userDetails, Integer NetworkingId, NetworkingUpdateDto dto) {
        Networking networking = networkingRepository.findById( dto.getNetworkingId() )
                .orElseThrow( () -> new NetworkingNotFoundException( dto.getNetworkingId(),this));
        validUser( userDetails, networking );
        Executive executive = executiveRepository.findById( dto.getExecutiveId() )
                .orElseThrow( () -> new ExecutiveNotFoundException( dto.getExecutiveId(), this));
        Member member = memberRepository.findById( dto.getMemberId() )
                .orElseThrow( () -> new MemberNotFoundException( dto.getMemberId(), this ));
        updateNetworkingProfiles( dto.getNetworkingId(), dto );
        System.out.println( dto.getNetworkingStatus() +"!!");
        networking.update( dto.getCategory(), member, dto.getNetworkingStatus(), executive);
        networking.setModifiedAt(LocalDateTime.now());
        return networking;
    }

    @Override
    public Networking updateNetworkingProfiles(UserDetailsImpl userDetails, Integer networkingId, NetworkingUpdateDto dto) {
        Networking networking = networkingRepository.findById( networkingId ).orElseThrow( () -> new NetworkingNotFoundException(networkingId,this));
        validUser( userDetails, networking );
        return updateNetworkingProfiles( networkingId, dto );
    }

    @Override
    public Networking updateNetworkingProfiles(Integer networkingId, NetworkingUpdateDto dto) {

        Networking networking = networkingRepository.findById( networkingId ).orElseThrow( () -> new NetworkingNotFoundException(networkingId,this));


        List<NetworkingProfile> existingProfiles = networking.getNetworkingProfiles();


        List< Integer > updatedProfileIds = dto.getNetworkingProfileIds();

//        existingProfiles.stream().filter(p -> !updatedProfileIds.contains(p.getProfile().getId())).forEach(networkingProfileRepository::delete);
        for (NetworkingProfile existingProfile : existingProfiles) {
            if( !updatedProfileIds.contains(existingProfile.getProfile().getId())){
                networkingProfileRepository.deleteByNetworkingProfileId( existingProfile.getNetworkingProfileId() );
            }
        }
        // 새로운 ProjectProfile 추가
        if( updatedProfileIds != null ){
            updatedProfileIds.forEach(profileId -> {
                if (existingProfiles.stream().noneMatch(np -> np.getProfile().getId().equals(profileId))) {
                    Profile profile = profileRepository.findById(profileId)
                            .orElseThrow(() -> new ProfileNotFoundException(profileId, this));
                    NetworkingProfile networkingProfile = new NetworkingProfile( networking, profile );
                    networking.getNetworkingProfiles().add( networkingProfile );
                }
            });
        }
        networking.setModifiedAt(LocalDateTime.now());
        return networking;
    }


    @Override
    public void deleteNetworking(UserDetailsImpl userDetails, Integer networkingId) {

        Networking networking = networkingRepository.findById( networkingId ).orElseThrow( () -> new NetworkingNotFoundException(networkingId,this));

        validUser( userDetails, networking );

        networkingRepository.delete( networking );
    }
    @Override
    public void deleteNetworkings(UserDetailsImpl userDetails, List<Integer> networkIds) {

        networkIds.forEach( id -> {
            Networking networking = networkingRepository.findById( id ).orElseThrow( () -> new NetworkingNotFoundException(id,this));
            validUser( userDetails, networking );
            networkingRepository.deleteById( id );

            Optional<NotificationData> notificationData = notificationDataRepository.getAllNotificationNoReadableTypeByDataAndDataType( NotificationType.NETWOKRING_APPROAVE, id);

            if( notificationData.isPresent() ){
                notificationDataRepository.deleteById(notificationData.get().getId());
                Notification notification = notificationData.get().getNotification();
                notificationRepository.deleteById( notification.getId() );
            }


        });


    }
        @Override
    public ExecutiveSearchResultDto getAllExecutiveByFilter(UserDetailsImpl userDetails, ExecutiveSearchConditionDto executiveSearchConditionDto, Pageable pageable) {
        
        if( executiveSearchConditionDto == null ) executiveSearchConditionDto = new ExecutiveSearchConditionDto();
        if( "".equals(executiveSearchConditionDto.getKeyword()) ){
            executiveSearchConditionDto.setKeyword(null);
        }

        Page<Executive> executives = memberRepositoryCustom.getAllExecutiveByFilter(executiveSearchConditionDto,pageable );

        return ExecutiveSearchResultDto.fromEntity(executives);
    }
    @Override
    public List<Department> getAllDepartments(UserDetailsImpl userDetails) {
        if( userDetails.getAuthorityLevel() > 2 ){
            return departmentRepository.findById(userDetails.getDepartmentId()).stream().toList();
        }
        return departmentRepository.findDepartments();
    }

    private String getNetworkingStatusKor( NetworkingStatus status ){
        switch (status){
            case READY_NETWORKING :
                return "미할당";
            case READY_APPROVEMENT:
                return "승인 대기중";
            case DOING_NETWORKING:
                return "네트워킹 진행 중";
            case DONE:
                return "완료";
            default:
                return "없음";
        }

    }

    private void validUser(UserDetailsImpl userDetails, Networking networking){
        if( !userDetails.getMemberId().equals( networking.getMember().getId()) ){
            if( userDetails.getAuthorityLevel() > 2 ){
                throw new InvalidAuthorizationException(userDetails.getMemberId(),this);
            }
        }
    }
}
