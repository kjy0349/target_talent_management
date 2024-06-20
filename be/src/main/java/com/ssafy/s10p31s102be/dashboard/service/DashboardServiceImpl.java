package com.ssafy.s10p31s102be.dashboard.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardCountrySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardDepartmentSearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardMonthlySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.*;
import com.ssafy.s10p31s102be.dashboard.utils.DashboardUtils;
import com.ssafy.s10p31s102be.member.exception.JobRankNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.repository.DepartmentJpaRepository;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.ProfileSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {
    private final DashboardUtils dashboardUtils;
    private final ProfileJpaRepository profileRepository;
    private final JobRankJpaRepository jobRankRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final ProfileSearchRepository profileSearchRepository;
    private final NetworkingJpaRepository networkingRepository;
    @Override
    public DashboardMainContentFullDto getDashboardMainContent(UserDetailsImpl userDetails) {

        List<Profile> profiles;
        if( userDetails.getAuthorityLevel() > 2 ){
            //직급4진 아래면 자기 부서 통계만 조회.
            profiles = profileRepository.findAllProfilesByDepartmentId( userDetails.getDepartmentId() );
            System.out.println("자기 부서:" + profiles.size());
        }else{
            profiles = profileRepository.findAll();
        }


        Map< String, Integer > jobranksMapper = new HashMap<>();

        for( Profile p : profiles ){
            String jr = p.getTargetJobRank() != null ? p.getTargetJobRank().getDescription() : "타겟 직급 없음";
            jobranksMapper.put( jr, jobranksMapper.getOrDefault( jr, 0 ) + 1 );
        }
//        List<String >sortOrder = jobRankRepository.findAllNotDeleted().stream().sorted( (j1,j2) -> Integer.compare( j2.getId(), j1.getId() )).map( j -> j.getDescription() ).collect(Collectors.toList());
        List<String> sortOrder = Arrays.asList("직급1↑","직급1", "부직급1↑","부직급1", "직급4", "직급4","C3", "C2", "직급1", "타겟 직급 없음");
        List<String> labels = sortOrder.stream()
                .filter(jobranksMapper::containsKey) // departmentMapper에 있는 키만 포함
                .collect(Collectors.toList());
        labels.add("타겟 직급 없음");
        List<Integer> series = labels.stream().map( l -> (int) Math.ceil((double)jobranksMapper.getOrDefault(l, 0) / profiles.size() * 100) ).collect(Collectors.toList());
        //n번 쿼리 쏘는 거보다 1번 쿼리쏘고 application 레벨에서 처리 // 프로필 많아지면 쿼리 적은게 나음
        //시간 없으니 하드 코딩
        ProfileTargetJobRankOverviewDto profileTargetJobRankOverviewDto = ProfileTargetJobRankOverviewDto.builder()
//                .labels( jobranksMapper.keySet().stream().toList() )
//                .series( jobranksMapper.values().stream().map(v -> {
//                    return (int) Math.floor((double)v / profiles.size() * 100);
//
//                } ).toList() )
                .labels(labels)
                .series(series)
                .build();
        Integer executerId = jobRankRepository.findByDescription("직급4")
                .orElseThrow( () -> new JobRankNotFoundException( null, this )).getId();
        DashboardMainContentFullDto dashboardMainContentFullDto =
                DashboardMainContentFullDto.builder()
                        .mainPageContent(DashboardMainContentDto.builder()
                                .totalPoolSize( profiles.size() )
                                .ePollSize((int) profiles.stream().filter(p -> p.getTargetJobRank() != null && p.getTargetJobRank().getId() <= executerId ).count())
                                .developerPoolSize((int) profiles.stream().filter(p -> p.getCareers() != null ).filter( profile -> profile.getCareers().stream().anyMatch( c ->
                                        dashboardUtils.getJobMapper().getOrDefault(c.getJobRank(), false))).count() )
                                .pPollSizePoolSize((int)profiles.stream().filter( p -> p.getEducations().stream().anyMatch( e -> e.getDegree().equals(Degree.pPollSize))).count())
                                .networkingPoolSize((int)profiles.stream().filter( p -> p.getNetworkings() != null && !p.getNetworkings().isEmpty()).count())
                                .techmapPoolSize((int)profiles.stream().filter( p -> p.getTechmapProjectProfiles() != null && !p.getTechmapProjectProfiles().isEmpty()).count())
                                .build())
                        .acquisitionOverview(profileTargetJobRankOverviewDto)
                        .build();
        return dashboardMainContentFullDto;
    }

    @Override
    public Long getSumProfilePool(UserDetailsImpl userDetails){
        if( userDetails.getAuthorityLevel() > 2 ){
            return profileRepository.findCountsProfilesByDepartmentId( userDetails.getDepartmentId() );
        }

        return profileRepository.count();
    }

    @Override
    public DashboardDepartmentFullDto getDashboardDepartment(UserDetailsImpl userDetails, DashboardDepartmentSearchConditionDto searchConditionDto) {

        List<Profile> profiles = profileSearchRepository.getAllProfileByDashboardDepartmentDto( searchConditionDto );

        Map< String, Integer > departmentMapper = new HashMap<>();
        List<Department> departments = departmentRepository.findDepartments();
        for( Profile p : profiles ){
//            System.out.println(p.getManager().getDepartment().getName());
            String departmentName = p.getManager().getDepartment()!= null ? p.getManager().getDepartment().getName() : "담당 부서 없음";
            departmentMapper.put( departmentName, departmentMapper.getOrDefault(departmentName, 0) + 1);
        }
        List<String> nextLabels = departments.stream().map(Department::getName).collect(Collectors.toList());

//        List<String > labels = departmentMapper.keySet().stream().toList();
        List<String> labels = departments.stream().map(Department::getName).filter(departmentMapper::containsKey).collect(Collectors.toList());
        System.out.println(labels.toString());
        List<DashboardDepartmentSeriesDto> seriesDtos = new ArrayList<>();
        seriesDtos.add( DashboardDepartmentSeriesDto.builder()
                .name("인재 모집 현황")
                .data(departments.stream().filter(d -> departmentMapper.containsKey( d.getName() )).map( d -> departmentMapper.getOrDefault( d.getName(), 0 ) ).collect(Collectors.toList()) )
                .build());

        DashboardDepartmentFullDto dashboardDepartmentFullDto = DashboardDepartmentFullDto
                .builder()
                .labels(labels)
                .visitors( profiles.size() )
                .percentage(50)
                .series(seriesDtos)
                .build();
        return dashboardDepartmentFullDto;
    }

    @Override
    public DashboardMonthlyFullDto getDashboardMonthly(UserDetailsImpl userDetails, DashboardMonthlySearchConditionDto searchConditionDto) {
        List<Profile> profiles = profileSearchRepository.getAllProfileByDashboardMonthlySearchCondition( searchConditionDto );
        if( userDetails.getAuthorityLevel() > 2 ){
            profiles = profiles.stream().filter( p -> p.getManager().getDepartment().getId().equals( userDetails.getDepartmentId() ) ).collect(Collectors.toList());
        }
        Map< Integer, Integer > monthlyMapper = new HashMap<>();
        
        for( Profile p : profiles ){
            int month = p.getCreatedAt().getMonthValue();
            monthlyMapper.put( month, monthlyMapper.getOrDefault( month, 0 ) + 1 );
        }
        DashboardMonthlyFullDto fullDto = DashboardMonthlyFullDto
                .builder()
                .labels(monthlyMapper.keySet().stream().map( m -> m.toString() + "월").toList())
                .series(new ArrayList<>())
                .percentage(50)
                .visitors(profiles.size())
                .build();
        
        DashboardMonthlySeriesDto dto = DashboardMonthlySeriesDto
                .builder()
                .name("월별 현황")
                .data(monthlyMapper.values().stream().toList())
                .build();
        fullDto.getSeries().add(dto);
        return fullDto;
    }

    @Override
    public List<String> getDashboardProfileSkillMainCategory(UserDetailsImpl userDetails) {
        List<Profile> profiles = profileRepository.findAll();

        List<String> skills = profiles.stream().map( p -> p.getProfileColumnDatas().stream().filter( pc -> pc.getProfileColumn().getName().equals("skillMainCategory")).findFirst().orElseGet(() -> new ProfileColumnData(
                "분야 없음",null,null
        )).getContent()).collect(Collectors.toSet()).stream().toList();
        return skills;
    }

    @Override
    public Map<String, DashboardCountrySummaryDto> getDashboardCountry(UserDetailsImpl userDetails, DashboardCountrySearchConditionDto dashboardCountrySearchConditionDto) {
        Map<String, DashboardCountrySummaryDto > result = new HashMap<>();
        List<Profile> profiles = profileRepository.findAll();
        if( userDetails.getAuthorityLevel() > 2 ){
            profiles = profiles.stream().filter( p -> p.getManager().getDepartment().getId().equals(userDetails.getDepartmentId())).collect(Collectors.toList());
        }
        YearMonth standard = YearMonth.of(dashboardCountrySearchConditionDto.getViewYear(), dashboardCountrySearchConditionDto.getViewMonth()).plusMonths(1);
        YearMonth prev = standard.minusMonths(1);

        HashMap<String, Integer> presentMapper = new HashMap<>();
        HashMap<String, Integer> prevMapper = new HashMap<>();

        for (Profile p : profiles) {
            if( p.getProfileColumnDatas().stream().anyMatch(pc -> pc.getProfileColumn() != null && pc.getProfileColumn().getName().equals("column1")) ){
                String nation = p.getProfileColumnDatas().stream().filter( pc -> pc.getProfileColumn() != null && pc.getProfileColumn().getName().equals("column1")).findFirst().get().getContent();
                YearMonth targetYearMonth = YearMonth.from( p.getCreatedAt() );

                if(targetYearMonth.isBefore( standard ) ){
                    presentMapper.put( nation, presentMapper.getOrDefault( nation, 0 ) + 1 );
                }

                if(targetYearMonth.isBefore( prev ) ){
                    prevMapper.put( nation, prevMapper.getOrDefault( nation, 0 ) + 1 );
                }
            }else{
                String nation = "column1 없음";
                YearMonth targetYearMonth = YearMonth.from( p.getCreatedAt() );

                if(targetYearMonth.isBefore( standard ) ){
                    presentMapper.put( nation, presentMapper.getOrDefault( nation, 0 ) + 1 );
                }

                if(targetYearMonth.isBefore( prev ) ){
                    prevMapper.put( nation, prevMapper.getOrDefault( nation, 0 ) + 1 );
                }
            }


        }

        for( String nation: presentMapper.keySet() ){
            result.put( nation, new DashboardCountrySummaryDto( presentMapper.get( nation ), presentMapper.get( nation ) - prevMapper.getOrDefault( nation, 0 ) ) );
        }

        return result;
    }

    @Override
    public Long getNetworkingValue() {
        AtomicReference<Long> count = new AtomicReference<>(0L);
        networkingRepository.findAll().stream().forEach( n -> count.updateAndGet(v -> v + n.getNetworkingProfiles().size()));
        return count.get();
    }

}
