package com.ssafy.s10p31s102be.member.infra.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.networking.dto.request.ExecutiveSearchConditionDto;
import com.ssafy.s10p31s102be.admin.dto.request.MemberAdminSearchConditionDto;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.s10p31s102be.member.infra.entity.QExecutive.executive;
import static com.ssafy.s10p31s102be.member.infra.entity.QMember.member;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory( em );
    }

    @Override
    public Page<Member> getAllMembersByFilter(MemberAdminSearchConditionDto memberAdminSearchConditionDto, Pageable pageable) {
        List<Member> members = jpaQueryFactory.selectFrom(member)
                .where(
                        nameContains(memberAdminSearchConditionDto.getKeyword()),
                        departmentEq(memberAdminSearchConditionDto.getDepartmentName()),
                        withDeleted(memberAdminSearchConditionDto.getWithDelete())
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count= jpaQueryFactory.select(member.count()).from(member).where(
                        nameContains(memberAdminSearchConditionDto.getKeyword()),
                        departmentEq(memberAdminSearchConditionDto.getDepartmentName()),
                        withDeleted(memberAdminSearchConditionDto.getWithDelete()),
                        memberIdnotIn(memberAdminSearchConditionDto.getMemberIds())
                );
        return PageableExecutionUtils.getPage(members, pageable, count::fetchOne);
    }

    private BooleanExpression memberIdnotIn(List<Integer> memberIds) {
        if( memberIds == null ){
            return null;
        }
        return memberIds.isEmpty() ? null : member.id.notIn( memberIds );
    }

    @Override
    public Page<Executive> getAllExecutiveByFilter(ExecutiveSearchConditionDto executiveSearchConditionDto, Pageable pageable) {
        List<Executive> executives = jpaQueryFactory.selectFrom(executive)
                .where(
                        nameExecutiveContains(executiveSearchConditionDto.getKeyword())
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count= jpaQueryFactory.select(executive.count()).from(member).where(
                nameExecutiveContains(executiveSearchConditionDto.getKeyword())
        );
        return PageableExecutionUtils.getPage(executives, pageable, count::fetchOne);
    }


    private BooleanExpression departmentEq(String departmentName) {
        if( departmentName == null ){
             return null;
        }
        return departmentName.isEmpty() ?  null : member.department.name.eq( departmentName );
    }


    private BooleanExpression nameContains(String keyword) {
        if( keyword == null ){
             return null;
        }
        return keyword.isEmpty() ? null : member.name.like( "%"+keyword+"%");
    }

    private BooleanExpression nameExecutiveContains(String keyword) {
        if( keyword == null ){
            return null;
        }
        return keyword.isEmpty() ? null : executive.name.like("%"+keyword+"%");
    }

    private BooleanExpression withDeleted(Boolean withDelete ){

        return withDelete ? null : member.isDeleted.eq(false);
    }



}
