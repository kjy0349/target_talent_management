package com.ssafy.s10p31s102be.project.infra.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.project.dto.request.ProjectSearchConditionDto;
import com.ssafy.s10p31s102be.project.infra.entity.Project;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectType;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ssafy.s10p31s102be.member.infra.entity.QMember.member;
import static com.ssafy.s10p31s102be.project.infra.entity.QProject.project;
import static com.ssafy.s10p31s102be.project.infra.entity.QProjectMember.projectMember;

@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom{
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public ProjectRepositoryCustomImpl( EntityManager em ){
        this.em = em;
        jpaQueryFactory = new JPAQueryFactory( this.em );
    }

    @Override
    public Page<Project> findAllWithFilter(UserDetailsImpl userDetails, ProjectSearchConditionDto dto, Pageable pageable) {
        System.out.println("조건 체크: " + dto.toString());
        JPAQuery<Long> count = jpaQueryFactory.select(project.countDistinct())
                .from(project)
                .leftJoin(projectMember).on(project.id.eq(projectMember.project.id))
                .leftJoin(projectMember.member, member)
                .where(
                        member.id.eq(userDetails.getMemberId())
                                .or(project.targetDepartment.id.eq(userDetails.getDepartmentId()).and(project.isPrivate.eq(false)))
                )
                .where(
                        targetJobRankFilter(dto.getTargetJobRankId())
                )
                .where(countryNameEq(dto.getCountryName()))
                .where(departmentIdEq(dto.getDepartmentId()))
                .where(targetYearEq(dto.getTargetYear()))
                .where(keywordContains(dto.getKeyword()))
                .where(isPrivateEq(dto.getIsPrivate()))
                .where(projectTypeEq(dto.getProjectType()))
                .where(targetCountryEq(dto.getTargetCountry()));

        List<Project> projects = jpaQueryFactory
                .select(project)
                .from(project)
                .leftJoin(projectMember).on(project.id.eq(projectMember.project.id))
                .leftJoin(projectMember.member, member)
                .where(
                        member.id.eq(userDetails.getMemberId())
                                .or(project.targetDepartment.id.eq(userDetails.getDepartmentId()).and(project.isPrivate.eq(false)))
                )
                .where(
                        targetJobRankFilter(dto.getTargetJobRankId())
                )
                .where(countryNameEq(dto.getCountryName()))
                .where(departmentIdEq(dto.getDepartmentId()))
                .where(targetYearEq(dto.getTargetYear()))
                .where(keywordContains(dto.getKeyword()))
                .where(isPrivateEq(dto.getIsPrivate()))
                .where(projectTypeEq(dto.getProjectType()))
                .where(targetCountryEq(dto.getTargetCountry()))
                .where(memberIdEq(dto.getMemberId()))
                .distinct()
                .orderBy(
                        new CaseBuilder()
                                .when(project.projectBookMarks.any().member.id.eq(userDetails.getMemberId())).then(1)
                                .otherwise(2).asc(),
                        getOrderByExpression(dto.getOrderBy())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(projects, pageable, count::fetchOne );
    }


    //TODO 수정 필 현재는 하드 코딩 앞으론 동적 값으로 가능하게 컬럼 추가할 것.
    private BooleanExpression targetJobRankFilter(Integer targetJobRankId) {

        if( targetJobRankId == null || targetJobRankId == 0 ){
            return null;
        }
        //isE
        if( targetJobRankId == 1 ){

            return project.targetJobRanks.any().jobRank.id.loe(54);
        }else{

            return project.targetJobRanks.any().jobRank.id.goe(55);
        }
    }


    public List<Project> findAllForFilterValue( UserDetailsImpl userDetails ){



        List<Project> projects = jpaQueryFactory
                .select(project)
                .from(project)
                .leftJoin(projectMember).on(project.id.eq(projectMember.project.id))
                .leftJoin(projectMember.member, member)
                .where(
                        member.id.eq(userDetails.getMemberId())
                                .or(project.targetDepartment.id.eq(userDetails.getDepartmentId()).and(project.isPrivate.eq(false)))
                ).distinct()
                .fetch();


        return  projects;

    }
    @Override
    public Page<Project> findAllWithFilterByAdmin(Integer memberId, ProjectSearchConditionDto dto, Pageable pageable) {
        System.out.println("조건 체크: " + dto.toString());
        List<Project> projects = jpaQueryFactory
                .select(project)
                .from(project)
                .where(
                        dto.getMemberId() != null ? project.projectMembers.any().member.id.eq(dto.getMemberId()) : null,
                        countryNameEq(dto.getCountryName()),
                        departmentIdEq(dto.getDepartmentId()),
                        targetYearEq(dto.getTargetYear()),
                        keywordContains(dto.getKeyword()),
                        isPrivateEq(dto.getIsPrivate()),
                        projectTypeEq(dto.getProjectType()),
                        targetJobRankFilter(dto.getTargetJobRankId()),
                        targetCountryEq(dto.getTargetCountry())
                ).distinct()
                .orderBy(
                        new CaseBuilder()
                                .when(project.projectBookMarks.any().member.id.eq(memberId)).then(1)
                                .otherwise(2).asc(),
                        getOrderByExpression(dto.getOrderBy())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count=  jpaQueryFactory
                .select(project.count())
                .from(project)
                .where(
                        dto.getMemberId() != null ? project.projectMembers.any().member.id.eq(dto.getMemberId()) : null,
                        countryNameEq(dto.getCountryName()),
                        departmentIdEq(dto.getDepartmentId()),
                        targetYearEq(dto.getTargetYear()),
                        keywordContains(dto.getKeyword()),
                        isPrivateEq(dto.getIsPrivate()),
                        targetJobRankFilter(dto.getTargetJobRankId()),
                        projectTypeEq(dto.getProjectType()),
                        targetCountryEq(dto.getTargetCountry())
                ).distinct();

        return PageableExecutionUtils.getPage(projects, pageable, count::fetchOne);
    }

    public List<Project> findAllForFilterValueAdmin( Integer authorityLevel ){



        List<Project> projects = jpaQueryFactory
                .select(project)
                .from(project)
                .where(
                        isExecutive( authorityLevel )
                ).distinct()
                .fetch();
        return projects;

    }

    private BooleanExpression isExecutive(Integer authorityLevel) {
        if( authorityLevel <= 1 ) return null;

        return project.isPrivate.eq(false);
    }
    private BooleanExpression memberIdEq( Integer memberId ){
        if( memberId == null || memberId == 0) return null;
        return project.projectMembers.any().member.id.eq( memberId );
    }
    private Comparator<Project> getOrderByComparator(String orderBy) {
        switch (orderBy) {
            case "PJT_OLD":
                return Comparator.comparing(Project::getCreatedAt);
            case "POOL_DESC":
                return Comparator.comparing(Project::getPoolSize).reversed();
            case "POOL_ASC":
                return Comparator.comparing(Project::getPoolSize);
            case "NAME_ASC":
                return Comparator.comparing(Project::getTitle);
            case "NAME_DESC":
                return Comparator.comparing(Project::getTitle).reversed();
            default:
                return Comparator.comparing(Project::getCreatedAt).reversed();
        }
    }
    private OrderSpecifier<?> getOrderByExpression(String orderBy) {
        switch (orderBy) {
            case "PJT_OLD":
                return project.createdAt.asc();
            case "POOL_DESC":
                return project.projectProfiles.size().desc();
            case "POOL_ASC":
                return project.projectProfiles.size().asc();
            case "NAME_ASC":
                return project.title.asc();
            case "NAME_DESC":
                return project.title.desc();
            default:
                return project.createdAt.desc();
        }
    }
    private BooleanExpression keywordContains(String keyword) {
        if( keyword == null ) return null;
        return keyword.isEmpty() ? null : project.title.contains(keyword);
    }

    private BooleanExpression targetYearEq(Integer targetYear) {
        if( targetYear == null ){
            return null;
        }
        return project.targetYear.eq(targetYear);
    }

    private BooleanExpression departmentIdEq(Integer departmentId) {
        if( departmentId == null ){
            return null;
        }
        return project.targetDepartment.id.eq(departmentId);
    }

    private BooleanExpression countryNameEq(String countryName) {
        if( countryName == null ){
            return null;
        }
        return project.targetCountry.eq(countryName);
    }
    private BooleanExpression isPrivateEq(Boolean isPrivate) {
        return isPrivate != null ? project.isPrivate.eq(isPrivate) : null;
    }

    private BooleanExpression projectTypeEq(ProjectType projectType) {
        return projectType != null ? project.projectType.eq(projectType) : null;
    }

    private BooleanExpression targetCountryEq(String targetCountry) {
        return targetCountry != null ? project.targetCountry.eq(targetCountry) : null;
    }



}
