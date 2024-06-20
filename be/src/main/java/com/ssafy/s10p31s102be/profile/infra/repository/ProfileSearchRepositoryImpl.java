package com.ssafy.s10p31s102be.profile.infra.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.common.infra.entity.QKeyword;
import com.ssafy.s10p31s102be.common.infra.enums.DomainType;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardCountrySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardDepartmentSearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardMonthlySearchConditionDto;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileExternalSearchConditionDto;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileFilterSearchDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileFilterSearchedDto;
import com.ssafy.s10p31s102be.profile.dto.response.RangeCountPair;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.education.QEducation;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.query.criteria.JpaSubQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static com.ssafy.s10p31s102be.member.infra.entity.QDepartment.department;
import static com.ssafy.s10p31s102be.member.infra.entity.QJobRank.jobRank;
import static com.ssafy.s10p31s102be.member.infra.entity.QMember.member;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfile.profile;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfileColumn.profileColumn;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfileColumnData.profileColumnData;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfileKeyword.*;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCareer.career;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCareerKeyword.*;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCompany.*;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QSpecialization.*;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QEducation.education;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QKeywordEducation.*;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QSchool.school;

@Repository
public class ProfileSearchRepositoryImpl implements ProfileSearchRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public ProfileSearchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(this.entityManager);
    }

    @Override
    public ProfileFilterSearchedDto getAllProfilePreviewByFilter(UserDetailsImpl userDetails, Pageable pageable, ProfileFilterSearchDto profileFilterSearchDto) {
        // 경력 계산
        NumberExpression<Long> sumCareerPeriods = career.careerPeriodMonth.sum();

        QKeyword prKeyword = new QKeyword("prKeyword");
        QKeyword crKeyword = new QKeyword("crKeyword");
        QKeyword edKeyword = new QKeyword("edKeyword");

        List<OrderSpecifier<?>> orders = getOrderSpecifiers(pageable);

        JPAQuery<Integer> searchedProfiles = searchProfileByFilter(prKeyword, crKeyword, edKeyword, userDetails, profileFilterSearchDto);
        List<Integer> filteredProfileIds = filterSearchedProfiles(prKeyword, crKeyword, edKeyword, profileFilterSearchDto, searchedProfiles, sumCareerPeriods);
        List<Profile> filteredProfiles = queryFactory.selectFrom(profile)
                .distinct()
                .leftJoin(profile.profileColumnDatas, profileColumnData).on(profileColumnData.profile.eq(profile))
                .leftJoin(profileColumnData.profileColumn, profileColumn).on(profileColumn.name.eq("name"))
                .where(profile.id.in(filteredProfileIds.stream().filter(Objects::nonNull).toList()))
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /*
            JPA + QueryDSL에서는, from절 Subquery 작성이 불가능하기 때문에, 조회 된 데이터로 필터링 하기 위해서는
            1. 검색 조건에 맞는 profile들의 id를 모두 조회한다.
            2. 필터링을 할 때 마다, profile 전체 테이블에서 in절을 이용해 full table search를 해야한다.
         */
        // 경력기간, 졸업년도 집계
        Map<String, Integer> careerRangeMap = new HashMap<>();
        Map<String, Integer> graduateAtMap = new HashMap<>();
        NumberExpression<Long> sumCareerPeriod = career.careerPeriodMonth.sum();

        List<Tuple> careerCountQuery = queryFactory
                .select(
                        profile,
                        sumCareerPeriod
                )
                .distinct()
                .from(profile)
                .leftJoin(profile.careers, career)
                .where(profile.id.in(filteredProfileIds))
                .groupBy(profile.id)
                .fetch();
        careerCountQuery.forEach((tuple) -> {
            Optional<Long> careerPeriod = Optional.ofNullable(tuple.get(sumCareerPeriod));
            careerPeriod.ifPresent(period -> getFilterRangeCount(String.valueOf(period / 12), careerRangeMap));
            if (careerPeriod.isEmpty()) getFilterRangeCount(null, careerRangeMap);
        });

        DateTimeExpression<LocalDateTime> maxGraduateAt = education.graduatedAt.max();
        List<Tuple> graduateCountQuery = queryFactory
                .select(
                        profile,
                        maxGraduateAt
                )
                .distinct()
                .from(profile)
                .leftJoin(profile.educations, education)
                .where(profile.id.in(filteredProfileIds))
                .groupBy(profile.id)
                .fetch();

        graduateCountQuery.forEach((tuple) -> {
            Optional<LocalDateTime> graduateAt = Optional.ofNullable(tuple.get(maxGraduateAt));
            graduateAt.ifPresent(graduate -> getFilterRangeCount(String.valueOf(graduate.getYear()), graduateAtMap));
            if (graduateAt.isEmpty()) getFilterRangeCount(null, graduateAtMap);
        });

        // 채용단계 집계
        Map<String, Integer> employStatusMap = new HashMap<>();
        NumberExpression<Long> sumStatus = profile.employStatus.count().as("employStatusSum");
        Expression<EmployStatus> employStatus = profile.employStatus.as("employStatus");
        List<Tuple> employStatusQuery = queryFactory.select(employStatus, sumStatus)
                .from(profile)
                .where(profile.id.in(filteredProfileIds))
                .groupBy(profile.employStatus)
                .fetch();
        employStatusQuery.forEach((tuple) -> {
            Optional<EmployStatus> employStatusString = Optional.ofNullable(tuple.get(employStatus));
            Optional<Long> sumStatusValue = Optional.ofNullable(tuple.get(sumStatus));
            employStatusString.ifPresent(string -> employStatusMap.put(employStatusString.get().toString(), Math.toIntExact(sumStatusValue.get())));
        });

        // 발굴 사업부 집계
        Map<String, Integer> founderDepartmentMap = new HashMap<>();
        NumberExpression<Long> departmentCount = profile.manager.department.count().as("departmentCountSum");
        Expression<String> departmentString = profile.manager.department.name;
        List<Tuple> departmentQuery = queryFactory.select(departmentCount, departmentString)
                .from(profile)
                .where(profile.id.in(filteredProfileIds))
                .groupBy(profile.manager.department)
                .fetch();
        departmentQuery.forEach((tuple) -> {
            Optional<String> departmentName = Optional.ofNullable(tuple.get(departmentString));
            Optional<Long> departmentValue = Optional.ofNullable(tuple.get(departmentCount));
            departmentName.ifPresent(string -> founderDepartmentMap.put(departmentName.get(), Math.toIntExact(departmentValue.get())));
        });

        // 내가 발굴한 프로필 집계
        Integer myProfileCount = queryFactory.select(profile.count().intValue())
                .from(profile)
                .where(profile.id.in(filteredProfileIds).and(profile.manager.id.eq(userDetails.getMemberId())))
                .fetchFirst();

        return ProfileFilterSearchedDto.builder()
                .filteredProfiles(filteredProfiles)
                .count(filteredProfileIds.size())
                .careerRange(convertMapToList(careerRangeMap))
                .graduateAtRange(convertMapToList(graduateAtMap))
                .founderDepartmentCounts(convertMapToList(founderDepartmentMap))
                .employStatusCounts(convertMapToList(employStatusMap))
                .myProfileCount(myProfileCount)
                .build();
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder<Object> pathBuilder;

            if (property.equals("name")) {
                orders.add(new OrderSpecifier<>(direction, new CaseBuilder()
                        .when(profileColumn.name.eq("name")).then(profileColumnData.content)
                        .otherwise(Expressions.constant(""))));
            } else {
                pathBuilder = new PathBuilder<>(profile.getType(), profile.getMetadata());
                orders.add(new OrderSpecifier(direction, pathBuilder.get(order.getProperty())));
            }
        }
        return orders;
    }



    private List<RangeCountPair> convertMapToList(Map<String, Integer> map) {
        List<RangeCountPair> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            list.add(new RangeCountPair(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    private List<Integer> filterSearchedProfiles(QKeyword prKeyword, QKeyword crKeyword, QKeyword edKeyword, ProfileFilterSearchDto profileFilterSearchDto, JPAQuery<Integer> searchedProfiles, NumberExpression<Long> sumCareerPeriods) {
        QEducation educationSub = new QEducation("educationSub");

        // 최종 학력 id 구하기
        List<Long> latestGraduationIds = queryFactory.select(educationSub.id, educationSub.graduatedAt.max())
                .from(educationSub)
                .groupBy(educationSub.profile.id)
                .fetch()
                .stream().map(tuple -> {
                    Optional<Long> id = Optional.ofNullable(tuple.get(educationSub.id));
                    return id.orElse(-1L);
                }).toList();
        return queryFactory.select(profile.id)
                .from(profile)
                .leftJoin(profile.profileColumnDatas, profileColumnData)
                .leftJoin(profileColumnData.profileColumn, profileColumn)
                .leftJoin(profile.profileKeywords, profileKeyword)
                .leftJoin(profileKeyword.keyword, prKeyword).on(profileKeyword.keyword.id.eq(prKeyword.id))
                .leftJoin(profile.targetJobRank, jobRank)
                .leftJoin(profile.careers, career)
                .leftJoin(career.careerKeywords, careerKeyword)
                .leftJoin(careerKeyword.keyword, crKeyword).on(careerKeyword.keyword.id.eq(crKeyword.id))
                .leftJoin(career.company, company)
                .leftJoin(profile.educations, education)
                .leftJoin(education.school, school)
                .leftJoin(education.keywordEducation, keywordEducation)
                .leftJoin(keywordEducation.keyword, edKeyword).on(keywordEducation.keyword.id.eq(edKeyword.id))
                .leftJoin(profile.manager, member)
                .leftJoin(member.department, department)
                .where(
                        profile.id.in(searchedProfiles),
                        searchCareerRange(profileFilterSearchDto.getCareerMinYear(), profileFilterSearchDto.getCareerMaxYear(), sumCareerPeriods),
                        skillSubCategoryEq(profileFilterSearchDto.getSkillSubCategory()),
                        column1Eq(profileFilterSearchDto.getcolumn2()),
                        jobRankEq(profileFilterSearchDto.getJobRanks()),
                        companyNameEq(profileFilterSearchDto.getCompanyNames()),
                        degreeEq(latestGraduationIds, profileFilterSearchDto.getDegrees()),
                        schoolNameEq(profileFilterSearchDto.getSchoolNames()),
                        departmentNameEq(profileFilterSearchDto.getDepartmentNames()),
                        founderNameEq(profileFilterSearchDto.getFounderNames()),
                        techSkillKeywordsEq(crKeyword, edKeyword, profileFilterSearchDto.getTechSkillKeywords()),
                        graduateRange(latestGraduationIds, profileFilterSearchDto),
                        employStatusEq(profileFilterSearchDto.getEmployStatuses()),
                        profileKeywordsEq(profileFilterSearchDto.getProfileKeywords())
                )
                .distinct()
                .fetch();
    }

    private void getFilterRangeCount(String key, Map<String, Integer> map) {
        if (key != null) {
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        } else {
            if (map.containsKey("none")) {
                map.put("none", map.get("none") + 1);
            } else {
                map.put("none", 1);
            }
        }
    }

    private JPAQuery<Integer> searchProfileByFilter(QKeyword prKeyword, QKeyword crKeyword, QKeyword edKeyword, UserDetailsImpl userDetails, ProfileFilterSearchDto profileFilterSearchDto) {
        return queryFactory.select(profile.id)
                .from(profile)
                .leftJoin(profile.profileColumnDatas, profileColumnData).on(profile.id.eq(profileColumnData.profile.id))
                .leftJoin(profileColumnData.profileColumn, profileColumn).on(profileColumnData.profileColumn.name.eq(profileColumn.name))
                .leftJoin(profile.careers, career).on(profile.id.eq(career.profile.id))
                .leftJoin(career.company, company).on(career.company.id.eq(company.id))
                .leftJoin(profile.educations, education).on(profile.id.eq(education.profile.id))
                .leftJoin(education.school, school).on(education.school.id.eq(school.id))
                .leftJoin(education.keywordEducation, keywordEducation).on(education.id.eq(keywordEducation.education.id))
                .leftJoin(keywordEducation.keyword, edKeyword).on(keywordEducation.keyword.id.eq(edKeyword.id))
                .leftJoin(profile.profileKeywords, profileKeyword).on(profile.id.eq(profileKeyword.profile.id))
                .leftJoin(profileKeyword.keyword, prKeyword).on(profileKeyword.keyword.id.eq(prKeyword.id))
                .leftJoin(career.careerKeywords, careerKeyword).on(career.id.eq(careerKeyword.career.id))
                .leftJoin(careerKeyword.keyword, crKeyword).on(careerKeyword.keyword.id.eq(crKeyword.id))
                .leftJoin(specialization).on(specialization.profile.id.eq(profile.id))
                .where(
                        profile.isNotNull(),
                        searchBySearchString(prKeyword, crKeyword, edKeyword, profileFilterSearchDto.getSearchString()),
                        isPrivateCheck(userDetails),
                        isAllCompanyCheck(userDetails),
                        isMineCheck(profileFilterSearchDto.getIsMine(), userDetails),
                        exceptCheck(profileFilterSearchDto.getExceptProfileIds())
                )
                .distinct();
    }


    @Override
    public Page<Profile> getAllProfileByDomainType(Integer domainId, DomainType domainType, ProfileExternalSearchConditionDto searchConditionDto, Pageable pageable) {
        QKeyword prKeyword = new QKeyword("prKeyword");
        QKeyword crKeyword = new QKeyword("crKeyword");
        QKeyword edKeyword = new QKeyword("edKeyword");


        List< Profile > profiles = queryFactory.selectDistinct(profile)
                .from(profile)
                .leftJoin(profile.profileColumnDatas, profileColumnData)
                .leftJoin(profileColumnData.profileColumn, profileColumn)
                .leftJoin(profile.careers, career)
                .leftJoin(career.company, company)
                .leftJoin(profile.educations, education)
                .leftJoin(education.school, school)
                .leftJoin(education.keywordEducation, keywordEducation)
                .leftJoin(keywordEducation.keyword, edKeyword)
                .leftJoin(profile.profileKeywords, profileKeyword)
                .leftJoin(profileKeyword.keyword, prKeyword)
                .leftJoin(career.careerKeywords, careerKeyword)
                .leftJoin(careerKeyword.keyword, crKeyword)
                .leftJoin(specialization).on(specialization.profile.id.eq(profile.id))
                .where(
                        getBooleanExpressionByDomainTypeAndId( domainType, domainId ),
                        searchBySearchString(prKeyword, crKeyword, edKeyword, searchConditionDto.getSearchString()),
                        departmentIdEq(searchConditionDto.getDepartmentId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = queryFactory.select(profile.countDistinct())
                .from(profile)
                .leftJoin(profile.profileColumnDatas, profileColumnData)
                .leftJoin(profileColumnData.profileColumn, profileColumn)
                .leftJoin(profile.careers, career)
                .leftJoin(career.company, company)
                .leftJoin(profile.educations, education)
                .leftJoin(education.school, school)
                .leftJoin(education.keywordEducation, keywordEducation)
                .leftJoin(keywordEducation.keyword, edKeyword)
                .leftJoin(profile.profileKeywords, profileKeyword)
                .leftJoin(profileKeyword.keyword, prKeyword)
                .leftJoin(career.careerKeywords, careerKeyword)
                .leftJoin(careerKeyword.keyword, crKeyword)
                .leftJoin(specialization).on(specialization.profile.id.eq(profile.id))
                .where(
                        getBooleanExpressionByDomainTypeAndId(domainType, domainId),
                        searchBySearchString(prKeyword, crKeyword, edKeyword, searchConditionDto.getSearchString()),
                        departmentIdEq(searchConditionDto.getDepartmentId())
                );
        System.out.println( "조회결과: " + count.fetchOne() + " " + "offset: " + pageable.getOffset() + " size " + pageable.getPageSize()
                + " page: " + profiles.stream().collect(Collectors.toList()).size()
        );
        return PageableExecutionUtils.getPage( profiles, pageable ,count::fetchOne);
    }

    public BooleanExpression skillSubCategoryEq(String skillSubCategory){
        if(skillSubCategory == null) return null;

        return profileColumn.name.eq("skillSubCategory").and(profileColumnData.content.eq(skillSubCategory));
    }

    public BooleanExpression nameEq( String keyword ){
        if( keyword == null ) return null;

        if( keyword == "" ) return null;

        return profileColumn.name.eq("name").and(profileColumnData.content.like("%" + keyword + "%"));
    }
    public List<Profile> getAllProfileByDashboardDepartmentDto(DashboardDepartmentSearchConditionDto dashboardDepartmentSearchConditionDto ) {


        List< Profile > profiles = queryFactory.selectDistinct(profile)
                .from(profile)
                .leftJoin(profile.profileColumnDatas, profileColumnData)
                .leftJoin(profileColumnData.profileColumn, profileColumn)
                .where(
                    targetJobRankIdEq(dashboardDepartmentSearchConditionDto.getJobRankId() ),
                    skillMainCategoryIn( dashboardDepartmentSearchConditionDto.getSkillMainCategory())
                )

                .fetch();

        System.out.println( "조회결과: "
                + " page: " + profiles.stream().collect(Collectors.toList()).size()
        );
        return profiles;
    }

    @Override
    public List<Profile> getAllProfileByDashboardMonthlySearchCondition(DashboardMonthlySearchConditionDto searchConditionDto) {
        List< Profile > profiles = queryFactory.selectDistinct(profile)
                .from(profile)
                .leftJoin(profile.manager)
                .where(
                        departmentIdEq(searchConditionDto.getDepartmentId() ),
                        createdYearEq(searchConditionDto.getCreatedYear())
                )

                .fetch();

        System.out.println( "조회결과: "
                + " page: " + profiles.stream().collect(Collectors.toList()).size()
                + "조건 : " + searchConditionDto.getCreatedYear() + "::"  + searchConditionDto.getDepartmentId()
        );
        return profiles;
    }

    @Override
    public List<Profile> getAllProfileByDashboardCountryDto(DashboardCountrySearchConditionDto dashboardCountrySearchConditionDto) {

        List< Profile > profiles = queryFactory.selectDistinct(profile)
                .from(profile)
                .leftJoin(profile.profileColumnDatas, profileColumnData)
                .leftJoin(profileColumnData.profileColumn, profileColumn)
                .where(
                        createdYearAndMonthEq( dashboardCountrySearchConditionDto )
                ).fetch();

        System.out.println( "조회결과: "
                + " page: " + profiles.stream().collect(Collectors.toList()).size()
        );
        return profiles;
    }

    private BooleanExpression createdYearAndMonthEq(DashboardCountrySearchConditionDto dashboardCountrySearchConditionDto) {
        if( dashboardCountrySearchConditionDto.getViewYear() == null || dashboardCountrySearchConditionDto.getViewMonth() == null ) return null;
        if( dashboardCountrySearchConditionDto.getViewYear() == 0 || dashboardCountrySearchConditionDto.getViewMonth() == 0 ){
            return null;
        }

        return profile.createdAt.year().eq(dashboardCountrySearchConditionDto.getViewYear()).and(profile.createdAt.month().eq(dashboardCountrySearchConditionDto.getViewMonth()));
    }

    public BooleanExpression createdYearEq( Integer createdYear ){
        if( createdYear == null || createdYear == 0){
            return profile.createdAt.year().eq(LocalDateTime.now().getYear());
        }

        return profile.createdAt.year().eq( createdYear );
    }
    public BooleanExpression departmentIdEq( Integer departmentId ){
        if( departmentId == null || departmentId == 0 ){
            return null;
        }
        return profile.manager.department.id.eq( departmentId );
    }
    public BooleanExpression skillMainCategoryIn(String skillMainCategory) {
        if (skillMainCategory == null || skillMainCategory.isEmpty()) {
            return null;
        }

        return profile.profileColumnDatas.any()
                .profileColumn.name.eq("skillMainCategory")
                .and(profileColumnData.content.eq(skillMainCategory));
    }
    private BooleanExpression targetJobRankIdEq( Integer jobRankId ){
        if( jobRankId == null || jobRankId == 0 ){
            return null;
        }
        return profile.targetJobRank.id.eq( jobRankId );
    }
    private BooleanExpression getBooleanExpressionByDomainTypeAndId(DomainType domainType, Integer domainId) {
        if( domainType.equals( DomainType.techmap)){
            return profile.techmapProjectProfiles.any().techmapProject.id.eq(domainId).not();
        }else if( domainType.equals(DomainType.network)){
            return profile.networkings.any().networking.id.eq(domainId).not();
        }else if( domainType.equals(DomainType.project)){
            return profile.projectProfiles.any().project.id.eq(domainId).not();
        }else{
            return null;
        }
    }

    private BooleanExpression exceptCheck(List<Integer> exceptProfileIds) {
        if (exceptProfileIds == null || exceptProfileIds.isEmpty()) {
            return null;
        }
        return profile.id.notIn(exceptProfileIds);
    }


    private BooleanExpression isMineCheck(Boolean isMine, UserDetailsImpl userDetails) {
        if (!isMine) {
            return null;
        }
        return profile.manager.id.eq(userDetails.getMemberId());
    }

    private BooleanExpression isAllCompanyCheck(UserDetailsImpl userDetails) {
        if (userDetails.getAuthorityLevel() <= 2) { // Admin, 운영진은 해당사항 없음
            return null;
        }
        // 채용부서장부터는, 전사 프로필이 아닌 경우 볼 수 없음. isAllCompany가 false면, 발굴자 부서와 같아야 열람 가능.
        return profile.isAllCompany.isTrue()
                .or(profile.isAllCompany.isFalse().and(profile.manager.department.id.eq(userDetails.getDepartmentId())));
    }

    private BooleanExpression isPrivateCheck(UserDetailsImpl userDetails) {
        if (userDetails.getAuthorityLevel() == 1) { // Admin은 해당사항 없음
            return null;
        }
        // 운영진부터는 isPrivate가 true인 Profile인 경우, 호출자의 memberId와 발굴자 Id가 동일해야 열람 가능
        return profile.isPrivate.isTrue().and(profile.manager.id.eq(userDetails.getMemberId()))
                .or(profile.isPrivate.isFalse());
    }

    private BooleanExpression searchCareerRange(Integer careerMinYear, Integer careerMaxYear, NumberExpression<Long> sumCareerPeriods) {
        if (careerMaxYear == null || careerMinYear == null) {
            return null;
        }
        BooleanExpression careerInRange = profile.id.in(
                JPAExpressions.select(career.profile.id)
                        .distinct()
                        .from(career)
                        .groupBy(career.profile.id)
                        .having(sumCareerPeriods.between(careerMinYear * 12, (careerMaxYear + 1) * 12 - 1))
        );

        // 경력 정보가 없는 프로필도 포함하는 조건
        BooleanExpression noCareer = profile.careers.isEmpty();

        // careerMinYear가 0인 경우, 경력 데이터가 없는 프로필 포함
        if (careerMinYear == 0) {
            return careerInRange.or(noCareer);
        } else {
            return careerInRange;
        }
    }

    private BooleanBuilder searchBySearchString(QKeyword prKeyword, QKeyword crKeyword, QKeyword edKeyword, String longSearchString) {
        if (longSearchString == null || longSearchString.isEmpty() || longSearchString.isBlank()) {
            return null;
        }
        BooleanBuilder builder = new BooleanBuilder();
        String[] searchKeywords = longSearchString.trim().split("\\s+");

        for (String searchString : searchKeywords) {
            BooleanBuilder keywordBuilder = new BooleanBuilder();
            keywordBuilder.or(profileColumn.name.eq("name").and(profileColumnData.content.like("%" + searchString + "%")))
                    .or(profileColumn.name.eq("nameEng").and(profileColumnData.content.like("%" + searchString + "%")))
                    .or(company.name.like("%" + searchString + "%"))
                    .or(school.schoolName.like("%" + searchString + "%"))
                    .or(education.major.like("%" + searchString + "%"))
                    .or(prKeyword.data.like("%" + searchString + "%"))
                    .or(crKeyword.data.like("%" + searchString + "%"))
                    .or(edKeyword.data.like("%" + searchString + "%"))
                    .or(specialization.specialPoint.like("%" + searchString + "%"))
                    .or(specialization.description.like("%" + searchString + "%"));
            builder.or(keywordBuilder);
        }
        return builder.hasValue() ? builder : null;
    }

    private BooleanExpression techSkillKeywordsEq(QKeyword crKeyword, QKeyword edKeyword, List<String> techSkillKeywords) {
        if (techSkillKeywords == null || techSkillKeywords.isEmpty()) {
            return null;
        }
        return crKeyword.type.eq(KeywordType.TECH_SKILL).and(crKeyword.data.in(techSkillKeywords))
                .or(edKeyword.type.eq(KeywordType.TECH_SKILL).and(edKeyword.data.in(techSkillKeywords)));
    }

    private BooleanExpression graduateRange(List<Long> latestGraduationIds, ProfileFilterSearchDto profileFilterSearchDto){
        Integer graduateMinYear = profileFilterSearchDto.getGraduateMinYear();
        Integer graduateMaxYear = profileFilterSearchDto.getGraduateMaxYear();
        if (graduateMinYear == null || graduateMaxYear == null) {
            return null;
        }
        LocalDateTime startOfYear = LocalDateTime.of(graduateMinYear, Month.JANUARY, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(graduateMaxYear, Month.DECEMBER, 31, 23, 59, 59);
        return profile.id.in(
                JPAExpressions.select(education.profile.id)
                        .distinct()
                        .from(education)
                        .where(
                                education.id.in(latestGraduationIds),
                                education.graduatedAt.between(startOfYear, endOfYear)
                        )
        );
    }

    private BooleanExpression employStatusEq(List<EmployStatus> employStatuses) {
        if (employStatuses == null || employStatuses.isEmpty()) {
            return null;
        }
        return profile.employStatus.in(employStatuses);
    }

    private BooleanExpression profileKeywordsEq(List<String> profileKeywords) {
        if (profileKeywords == null || profileKeywords.isEmpty()) {
            return null;
        }
        QKeyword prKeyword = new QKeyword("prKeyword");
        return prKeyword.type.eq(KeywordType.PROFILE).and(prKeyword.data.in(profileKeywords));
    }

    private BooleanExpression column1Eq(List<String> column2) {
        if (column2 == null || column2.isEmpty()) {
            return null;
        }
        return profileColumn.name.eq("column1").and(profileColumnData.content.in(column2));
    }

    private BooleanExpression jobRankEq(List<String> jobRanks) {
        if (jobRanks == null || jobRanks.isEmpty()) {
            return null;
        }
        return jobRank.description.in(jobRanks);
    }

    private BooleanExpression companyNameEq(List<String> companyNames) {
        if (companyNames == null || companyNames.isEmpty()) {
            return null;
        }
        return company.name.in(companyNames);
    }

    private BooleanExpression degreeEq(List<Long> latestGraduationIds, List<Degree> degrees) {
        if (degrees == null || degrees.isEmpty()) {
            return null;
        }

        // 최종 학력을 기준으로 degree 판단
        return education.id.in(latestGraduationIds)
                .and(education.degree.in(degrees));
    }

    private BooleanExpression schoolNameEq(List<String> schoolNames) {
        if (schoolNames == null || schoolNames.isEmpty()) {
            return null;
        }
        return school.schoolName.in(schoolNames);
    }

    private BooleanExpression departmentNameEq(List<String> departmentNames) {
        if (departmentNames == null || departmentNames.isEmpty()) {
            return null;
        }
        return department.name.in(departmentNames);
    }

    private BooleanExpression founderNameEq(List<String> founderNames) {
        if (founderNames == null || founderNames.isEmpty()) {
            return null;
        }
        return member.name.in(founderNames);
    }
}
