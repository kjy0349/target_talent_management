package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.common.infra.enums.DomainType;
import com.ssafy.s10p31s102be.common.util.ExcelUtil;
import com.ssafy.s10p31s102be.common.util.AuthorityUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.DepartmentNotFoundException;
import com.ssafy.s10p31s102be.member.exception.JobRankNotFoundException;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import com.ssafy.s10p31s102be.profile.dto.request.*;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileDetailDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileExcelImportResponseDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileFilterSearchedDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.RequiredDynamicColumnMissingException;
import com.ssafy.s10p31s102be.profile.infra.entity.Country;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumn;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.career.CareerKeyword;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployType;
import com.ssafy.s10p31s102be.profile.infra.enums.GraduateStatus;
import com.ssafy.s10p31s102be.profile.infra.repository.*;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileColumnDataJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileColumnJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileSearchRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final MemberJpaRepository memberRepository;
    private final ProfileJpaRepository profileRepository;
    private final ProfileColumnJpaRepository profileColumnRepository;
    private final ProfileColumnDataJpaRepository profileColumnDataRepository;
    private final ProfileSearchRepository profileSearchRepository;
    private final CountryJpaRepository countryRepository;
    private final JobRankJpaRepository jobRankRepository;
    private final CareerKeywordJpaRepository careerKeywordJpaRepository;
    private final KeywordEducationJpaRepository keywordEducationJpaRepository;
    private final CountryDisabledColumnJpaRepository countryDisabledColumnRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final EducationService educationService;
    private final CareerService careerService;
    private final ExcelUtil excelUtil;
    private final ProfileColumnDataQueryDSLRepository profileColumnDataQueryDSLRepository;

    @Override
    public Profile create(UserDetailsImpl userDetails, ProfileCreateDto dto) {
        Member member = memberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new MemberNotFoundException(userDetails.getMemberId(), this));

        // TODO: 당장은 권한 체크가 필요없어서 userDetails를 쓸 곳은 없을 것 같다. 확인하기

        if (checkInvalidProfileDynamicColumns(dto.getColumns())) throw new RequiredDynamicColumnMissingException(this);

        Profile profile = Profile.builder()
                .founder(member.getName())
                .profileImage(dto.getProfileImage())
                .manager(member)
                .targetJobRank(dto.getTargetJobRankId() != null ? jobRankRepository.findById(dto.getTargetJobRankId())
                        .orElseThrow(() -> new JobRankNotFoundException(dto.getTargetJobRankId(), this)) : null)
                .profileImage(dto.getProfileImage())
                .isPrivate(dto.getIsPrivate())
                .isAllCompany(dto.getIsAllCompany())
                .build();

        // TODO: 영속성 컨텍스트에 Entity를 저장하지 않은 채로 Profile Entity에 접근해서 Transient Exception이 발생함.
        // 생성할 때에는 한번 미리 저장하는 방식으로 우선 구현해두었지만, 추후 개선 필요
        profileRepository.save(profile);

        createOrUpdateProfileDynamicColumns(profile, dto.getColumns());

        profileRepository.save(profile);
        return profile;
    }

    @Override
    public Profile createFromExcel(Member manager, ProfileCreateDto dto) {
        if (checkInvalidProfileDynamicColumns(dto.getColumns())) throw new RequiredDynamicColumnMissingException(this);

        Profile profile = Profile.builder()
                .founder(manager.getName())
                .manager(manager)
                .targetJobRank(dto.getTargetJobRankId() != null ? jobRankRepository.findById(dto.getTargetJobRankId())
                        .orElseThrow(() -> new JobRankNotFoundException(dto.getTargetJobRankId(), this)) : null)
                .profileImage(dto.getProfileImage())
                .isPrivate(dto.getIsPrivate())
                .isAllCompany(dto.getIsAllCompany())
                .build();
        profile.setModifiedAt(LocalDateTime.now());

        // TODO: 영속성 컨텍스트에 Entity를 저장하지 않은 채로 Profile Entity에 접근해서 Transient Exception이 발생함.
        // 생성할 때에는 한번 미리 저장하는 방식으로 우선 구현해두었지만, 추후 개선 필요
        profileRepository.save(profile);

        createOrUpdateProfileDynamicColumns(profile, dto.getColumns());

        profileRepository.save(profile);
        return profile;
    }

    @Override
    public List<ProfileColumn> getProfileDynamicColumns() {
        return profileColumnRepository.findAll();
    }

    @Override
    public Map<String, List<String>> getCountryDisabledColumnsMap() {
        return countryRepository.findAll().stream().collect(Collectors.toMap(
                Country::getName,
                c -> c.getCountryDisabledColumns().stream().map(x -> x.getProfileColumn().getName()).toList()
        ));
    }

    @Override
    public Profile update(UserDetailsImpl userDetails, Integer profileId, ProfileCreateDto dto) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);

        if (checkInvalidProfileDynamicColumns(dto.getColumns())) throw new RequiredDynamicColumnMissingException(this);
        profile.updateJobRank(jobRankRepository.findById(dto.getTargetJobRankId())
                .orElseThrow(() -> new JobRankNotFoundException(dto.getTargetJobRankId(), this)));
        createOrUpdateProfileDynamicColumns(profile, dto.getColumns());
        profile.updateOpenStatus(dto.getIsPrivate(), dto.getIsAllCompany());
        profile.updateProfileImage(dto.getProfileImage());
        profileRepository.save(profile);
        return profile;
    }

    private boolean checkInvalidProfileDynamicColumns(Map<String, String> dynamicColumns) {
        Set<ProfileColumn> requiredColumns = profileColumnRepository.findAllByRequiredTrue();

        // 필수 프로필 컬럼이 하나라도 존재하지 않으면 에러
        return dynamicColumns == null || !requiredColumns.stream().allMatch(profileColumn -> dynamicColumns.containsKey(profileColumn.getName()));
    }

    // TODO: create와 update 분리하자. 업데이트 시 동적 컬럼 일괄 삭제 후 다시 INSERT 하는게 좋을 것 같다.
    private void createOrUpdateProfileDynamicColumns(Profile profile, Map<String, String> dynamicColumns) {

        // 전체 동적 컬럼 리스트
        Map<String, ProfileColumn> dynamicColumnMap = profileColumnRepository.findAllById(dynamicColumns.keySet()).stream()
                .collect(Collectors.toMap(ProfileColumn::getName, Function.identity()));

        // 기존 데이터 중 입력받지 않은 데이터는 삭제
        // 정상 요청의 경우에는 null이 아닌 비어있는 String이 올 것이기 때문에 일반적인 상황에서는 Validation이 필요 없긴 하다.
        // 동적 컬럼의 정보가 변경되었을 때를 위한 체크
//        profile.getProfileColumnDatas().forEach(profileColumnData -> {
//            if (!dynamicColumns.containsKey(profileColumnData.getProfileColumn().getName())) {
//                profileColumnDataRepository.delete(profileColumnData);
//            }
//        });

        // 기존 데이터를 한 번씩 조회하는데에 너무 많은 소요가 발생. 모두 지우고 다시 만드는 쪽이 성능적으로 유리.
        profileColumnDataRepository.deleteAllByProfileId(profile.getId());
        List<ProfileColumnData> inputColumnDatas = new ArrayList<>();
        for (Map.Entry<String, String> entry : dynamicColumns.entrySet()) {
            ProfileColumn targetColumn = dynamicColumnMap.get(entry.getKey());
            if (targetColumn == null) {
                log.info("올바르지 않은 동적컬럼 key 값입니다." + entry.getKey());
                continue;
            }
            inputColumnDatas.add(
                    ProfileColumnData.builder()
                            .profile(profile)
                            .profileColumn(targetColumn)
                            .content(dynamicColumns.get(entry.getKey()))
                            .build()
            );
        }
        profileColumnDataRepository.saveAll(inputColumnDatas);
        profile.getProfileColumnDatas().addAll(inputColumnDatas);

        // 입력받은 컬럼 Iteration -> 이미 존재하면 update 해주고 존재하지 않으면 create 해준다.
//        dynamicColumns.forEach((key, value) -> {
//            ProfileColumnData data = profileColumnDataRepository.findByProfileAndProfileColumn(profile, dynamicColumnMap.get(key))
//                    .map(prev -> {
//                        prev.updateContent(value);
//                        return prev;
//                    })
//                    .orElseGet(() -> ProfileColumnData.builder()
//                            .profileColumn(dynamicColumnMap.get(key))
//                            .profile(profile)
//                            .content(value)
//                            .build()
//                    );
//
//            profileColumnDataRepository.save(data);
//            profile.getProfileColumnDatas().add(data);
//        });
    }

    @Override
    public List<Profile> updateProfileManager(UserDetailsImpl userDetails, ProfileManagerUpdateDto dto) {
        Member targetManager = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new MemberNotFoundException(dto.getMemberId(), this));

        return profileRepository.findAllById(dto.getProfileIds()).stream().map(profile -> {
            try {
                AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);
                profile.updateManager(targetManager);
                return profile;
            } catch (InvalidAuthorizationException e) {
                return profile;
            }
        }).toList();
    }

    @Override
    public Profile updateProfileImage(UserDetailsImpl userDetails, Integer profileId, String profileImageUrl) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 6);

        profile.updateProfileImage(profileImageUrl);
        return profile;
    }

    @Override
    public List<EmployStatus> updateProfileEmployStatus(UserDetailsImpl userDetails, ProfileStatusUpdateDto dto) {

        List<EmployStatus> statusList = profileRepository.findAllById(dto.getProfileIds()).stream().map(profile -> {
            AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 5);
            profile.updateStatus(dto.getStatus());

            return profile.getEmployStatus();
        }).toList();

        return statusList;
    }

    @Override
    public void delete(UserDetailsImpl userDetails, ProfileIdsRequestDto dto) {
        profileRepository.findAllById(dto.getProfileIds()).forEach(profile -> {
            AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 4);
            profileRepository.delete(profile);
        });
    }

    @Override
    public ProfilePreviewDto getProfilePreviewById(Integer profileId) {
        Profile profile = profileRepository.findProfileByIdWithProfileColumnDatas(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));
        return ProfilePreviewDto.fromEntity(profile);
    }

    @Override
    public ProfileDetailDto getProfileDetailById(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        Set<Integer> memberIds = profile.getProjectProfiles().stream()
                .flatMap(pp -> pp.getProject().getProjectMembers().stream())
                .map(pm -> pm.getMember().getId())
                .collect(Collectors.toSet());

        if( !memberIds.contains( userDetails.getMemberId() ) ){
            if (profile.getIsPrivate()) {
            /*
                나만 보기 프로필인 경우, Admin은 볼 수 있음.
                AuthorityLevel이 Admin이 아니면서, 담당자와 다른 사람이면 볼 수 없어야 한다.
            */
                if (userDetails.getAuthorityLevel() >= 2 && !profile.getManager().getId().equals(userDetails.getMemberId())) {
                    throw new InvalidAuthorizationException(userDetails.getMemberId(), this);
                }
            }
            if (!profile.getIsAllCompany()) { // 사업부 프로필일 경우
                AuthorityUtil.validateAuthority(profile.getManager(), userDetails, 6);
            }
            // 전사 프로필일 경우, 나만보기가 아닌 경우 모두 조회 가능
        }


        return (ProfileDetailDto.fromEntity(userDetails.getMemberId(), profile));
    }

    @Override
    public ProfileFilterSearchedDto getAllProfilePreviewByFilter(UserDetailsImpl userDetails, Pageable pageable, ProfileFilterSearchDto profileFilterSearchDto) {
        ProfileFilterSearchedDto allProfilePreviewByFilter = profileSearchRepository.getAllProfilePreviewByFilter(userDetails, pageable, profileFilterSearchDto);
        Department department = departmentRepository.findById(userDetails.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(userDetails.getDepartmentId(), this));
        allProfilePreviewByFilter.setMyDepartment(department.getName());
        return allProfilePreviewByFilter;
    }

    @Override
    public ProfileExcelImportResponseDto insertFromExcel(UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        List<Map<String, String>> profiles = excelUtil.excelToMap(file);
        List<ProfileCreateDto> profileCreateDtos = new ArrayList<>();
        Map<String, JobRank> jobRankMap = jobRankRepository.findAllNotDeleted().stream()
                .collect(Collectors.toMap(JobRank::getDescription, Function.identity()));
        Map<String, ProfileColumn> dynamicColumnMap = profileColumnRepository.findAllByLabels(profiles.get(0).keySet()).stream()
                .collect(Collectors.toMap(ProfileColumn::getLabel, Function.identity()));
        Map<String, EmployStatus> recruitmentStepMap = Map.ofEntries(
                Map.entry("발굴", EmployStatus.FOUND),
                Map.entry("접촉", EmployStatus.CONTACT),
                Map.entry("활용도검토", EmployStatus.USAGE_REVIEW),
                Map.entry("중/장기관리", EmployStatus.MID_LONG_TERM_MANAGE),
                Map.entry("단기관리", EmployStatus.SHORT_TERM_MANAGE),
                Map.entry(" 인터뷰", EmployStatus.인터뷰1),
                Map.entry(" 인터뷰", EmployStatus.인터뷰2),
                Map.entry("interview3", EmployStatus.인터뷰3),
                Map.entry("인터뷰 4", EmployStatus.for_interview),
                Map.entry("처우협의", EmployStatus.NEGOTIATION),
                Map.entry("처우결렬", EmployStatus.NEGOTIATION_DENIED),
                Map.entry("입사포기", EmployStatus.EMPLOY_ABANDON),
                Map.entry("입사대기", EmployStatus.EMPLOY_WAITING),
                Map.entry("입사", EmployStatus.EMPLOYED)
        );
        Integer failCount = 0;
        Integer successCount = 0;
        outLoop:
        for (Map<String, String> map : profiles) {
            ProfileCreateDto profileCreateDto = new ProfileCreateDto();
            profileCreateDto.setColumns(new LinkedHashMap<>());
            List<CareerCreateDto> careerCreateDtos = new ArrayList<>(List.of(new CareerCreateDto(),
                    new CareerCreateDto(), new CareerCreateDto(), new CareerCreateDto(),
                    new CareerCreateDto(), new CareerCreateDto(), new CareerCreateDto(), new CareerCreateDto()));
            List<EducationCreateDto> educationCreateDtos = new ArrayList<>(List.of(new EducationCreateDto(),
                    new EducationCreateDto(),
                    new EducationCreateDto()));

            Set<Map.Entry<String, String>> entries = map.entrySet();
            Optional<Member> manager = Optional.empty();
            EmployStatus employStatus = EmployStatus.FOUND;
            for (Map.Entry<String, String> entry : entries) {
                ProfileColumn byLabel = dynamicColumnMap.getOrDefault(entry.getKey(), null);
                if (byLabel != null) {
                    putProfileCreateDto(profileCreateDto, byLabel.getName(), entry.getValue());
                } else {
                    String key = entry.getKey();
                    switch (key) {
                        case "담당자 Knox ID" -> {
                            manager = memberRepository.findByKnoxId(entry.getValue());
                            if (manager.isEmpty()) {
                                log.info("해당 사용자를 찾을 수 없습니다. ID : " + entry.getValue());
                                failCount++;
                                continue outLoop;
                            } else {
                                if (userDetails.getAuthorityLevel() != 1 && !manager.get().getDepartment().getId().equals(userDetails.getDepartmentId())) {
                                    log.info("다른 사업부 입니다.");
                                    failCount++;
                                    continue outLoop;
                                }
                            }
                        }
                        case "타겟직급" -> {
                            JobRank targetJobRank = jobRankMap.getOrDefault(entry.getValue(), jobRankMap.get("C2"));
                            profileCreateDto.setTargetJobRankId(targetJobRank.getId());
                        }
                        case "채용단계" ->
                                employStatus = recruitmentStepMap.getOrDefault(entry.getValue(), EmployStatus.FOUND);
                        case "나만보기 여부" ->
                                profileCreateDto.setIsPrivate(entry.getValue().equals("Y"));
                        case "전사/사업부 프로필 여부" ->
                                profileCreateDto.setIsAllCompany(entry.getValue().equals("Y"));
                    }
                    int idx = key.charAt(key.length() - 1) - '0' - 1;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
                    makeEducations(entry, key, educationCreateDtos, idx, formatter);
                    makeCareers(entry, key, careerCreateDtos, idx, formatter);
                }
            }
            Profile profile = createFromExcel(manager.get(), profileCreateDto);
            profile.updateStatus(employStatus);
            successCount++;
            for (EducationCreateDto educationCreateDto : educationCreateDtos) {
                if (educationCreateDto.getDegree() != null &&
                        educationCreateDto.getSchoolCountry() != null &&
                        educationCreateDto.getSchoolName() != null) {
                    educationService.create(userDetails, profile.getId(), educationCreateDto);
                }
            }
            for (CareerCreateDto careerCreateDto : careerCreateDtos) {
                if (careerCreateDto.getCompanyName() != null) {
                    careerService.create(userDetails, profile.getId(), careerCreateDto);
                }
            }
        }
        return ProfileExcelImportResponseDto.builder()
                .failCount(failCount)
                .successCount(successCount)
                .build();
    }

    private void makeCareers(Map.Entry<String, String> entry, String key, List<CareerCreateDto> careerCreateDtos, int idx, DateTimeFormatter formatter) {
        Map<String, Degree> degreeMap = Map.of(
                "박사", Degree.pPollSize,
                "석사", Degree.MASTER,
                "학사", Degree.BACHELOR
        );
        Map<String, EmployType> employTypeMap = Map.of(
                "정규직", EmployType.FULL_TIME,
                "계약직", EmployType.CONTRACT
        );
        if (key.startsWith("회사명") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setCompanyName(entry.getValue());
        }
        if (key.startsWith("직급") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setJobRank(entry.getValue());
        }
        if (key.startsWith("레벨") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setLevel(entry.getValue());
        }
        if (key.startsWith("매니저여부") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setIsManager(entry.getValue().equals("Y"));
        }
        if (key.startsWith("고용형태") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setEmployType(employTypeMap.get(entry.getValue()));
        }
        if (key.startsWith("회사국가") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setCompanyCountryName(entry.getValue());
        }
        if (key.startsWith("회사지역") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setCompanyRegion(entry.getValue());
        }
        if (key.startsWith("경력시작년월") && !entry.getValue().isEmpty()) {
            YearMonth yearMonth = YearMonth.parse(entry.getValue(), formatter);
            careerCreateDtos.get(idx).setStartedAt(yearMonth.atDay(1).atStartOfDay());
        }
        if (key.startsWith("경력종료년월") && !entry.getValue().isEmpty()) {
            YearMonth yearMonth = YearMonth.parse(entry.getValue(), formatter);
            careerCreateDtos.get(idx).setEndedAt(yearMonth.atDay(1).atStartOfDay());
        }
        if (key.startsWith("현재재직여부") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setIsCurrent(entry.getValue().equals("Y"));
        }
        if (key.startsWith("근무부서") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setDept(entry.getValue());
        }
        if (key.startsWith("담당업무") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setRole(entry.getValue());
        }
        if (key.startsWith("설명") && !entry.getValue().isEmpty()) {
            careerCreateDtos.get(idx).setDescription(entry.getValue());
        }
    }
    private void makeEducations(Map.Entry<String, String> entry, String key, List<EducationCreateDto> educationCreateDtos, int idx, DateTimeFormatter formatter) {
        Map<String, Degree> degreeMap = Map.of(
                "박사", Degree.pPollSize,
                "석사", Degree.MASTER,
                "학사", Degree.BACHELOR
        );
        Map<String, GraduateStatus> graduateStatusMap = Map.of(
                "졸업", GraduateStatus.GRADUATED,
                "졸업예정", GraduateStatus.EXPECTED,
                "수료", GraduateStatus.COMPLETED
        );
        if (key.startsWith("학위") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setDegree(degreeMap.get(entry.getValue()));
        }
        if (key.startsWith("학교국가") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setSchoolCountry(entry.getValue());
        }
        if (key.startsWith("학교명") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setSchoolName(entry.getValue());
        }
        if (key.startsWith("전공명") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setMajor(entry.getValue());
        }
        if (key.startsWith("입학년월") && !entry.getValue().isEmpty()) {
            log.info(entry.toString());
            YearMonth yearMonth = YearMonth.parse(entry.getValue(), formatter);
            educationCreateDtos.get(idx).setEnteredAt(yearMonth.atDay(1).atStartOfDay());
        }
        if (key.startsWith("졸업년월") && !entry.getValue().isEmpty()) {
            YearMonth yearMonth = YearMonth.parse(entry.getValue(), formatter);
            educationCreateDtos.get(idx).setGraduatedAt(yearMonth.atDay(1).atStartOfDay());
        }
        if (key.startsWith("졸업여부") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setGraduateStatus(graduateStatusMap.get(entry.getValue()));
        }
        if (key.startsWith("연구실명") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setLabName(entry.getValue());
        }
        if (key.startsWith("연구분야") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setLabResearchType(entry.getValue());
        }
        if (key.startsWith("연구실적") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setLabResearchResult(entry.getValue());
        }
        if (key.startsWith("지도교수") && !entry.getValue().isEmpty()) {
            educationCreateDtos.get(idx).setLabProfessor(entry.getValue());
        }
    }

    private void putProfileCreateDto(ProfileCreateDto createDto, String key, String value) {
        if (!value.isEmpty()) {
            createDto.getColumns().put(key, value);
        }
    }

//    @Override
//    public
//Project Network 및 전 프론트엔드 Profile 다중 셀렉트 모달 제작을 위한 서비스 로직 지우지 마세요. - 주석
    @Override
    public Page<Profile> findForSelection(UserDetailsImpl userDetails, Integer domainId, DomainType domainType, ProfileExternalSearchConditionDto searchConditionDto, Pageable pageable) {

        if( userDetails.getAuthorityLevel() > 2 ){
            searchConditionDto.setDepartmentId( userDetails.getDepartmentId() );
        }
        Page<Profile> profiles = profileSearchRepository.getAllProfileByDomainType( domainId, domainType, searchConditionDto,pageable );
        return profiles;
    }

    @Override
    public Profile updateProfileEmployStatusById(Integer profileId, EmployStatus employStatus) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        profile.updateStatus(employStatus);
        return profile;
    }

    @Override
    public List<String> getSearchedNames(UserDetailsImpl userDetails, ProfileNameSearchDto nameSearchDto) {
        return profileColumnDataQueryDSLRepository.getSimilarName(userDetails, nameSearchDto.getNameSearchString());
    }

    @Override
    public List<Profile> recommendSimilarProfiles(UserDetailsImpl userDetails, Integer profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(profileId, this));

        long sumCareerPeriods = profile.getCareers().stream().filter(career -> {
            Long careerPeriodMonth = career.getCareerPeriodMonth();
            return careerPeriodMonth != null;
        }).mapToLong(Career::getCareerPeriodMonth).sum() / 12;
        List<String> techSkillKeywords = new ArrayList<>();
        profile.getCareers().stream().forEach(
                career -> {
                    techSkillKeywords.addAll(career.getCareerKeywords().stream().map(careerKeyword -> {
                        return careerKeyword.getKeyword().getData();
                    }).toList());
                }
        );
        profile.getEducations().stream().forEach(
                education -> {
                    techSkillKeywords.addAll(education.getKeywordEducation().stream().map(keywordEducation -> {
                        return keywordEducation.getKeyword().getData();
                    }).toList());
                }
        );

        ProfileFilterSearchDto target = ProfileFilterSearchDto
                .builder()
                .careerMinYear((int) sumCareerPeriods - 5)
                .careerMaxYear((int) sumCareerPeriods + 5)
                .skillSubCategory(profile.getProfileColumnDatas().stream().filter(profileColumnData -> {
                    return profileColumnData.getProfileColumn().getName().equals("skillSubCategory");
                }).findFirst().map(ProfileColumnData::getContent).orElseGet(null))
                .techSkillKeywords(techSkillKeywords)
                .exceptProfileIds(new ArrayList<>(List.of(profileId)))
                .isMine(false)
                .build();
        ProfileFilterSearchedDto profiles = getAllProfilePreviewByFilter(userDetails, PageRequest.of(0, 3), target);
        return profiles.getFilteredProfiles();
    }
}