package com.ssafy.s10p31s102be.networking.infra.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.s10p31s102be.networking.infra.entity.QNetworking.networking;

@Repository
public class NetworkingRepositoryCustomImpl implements NetworkingRepositoryCustom{
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public NetworkingRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Networking> getAllNetworkingsWithFilter(Integer departmentId, Integer targetYear, String category, Pageable pageable) {
        List<Networking> networkings = jpaQueryFactory
                .select(networking)
                .from(networking)
                .where(
                        categoryContains(category),
                        departmentIdEq(departmentId),
                        targetYearEq(targetYear)
                )
                .orderBy( networking.createdAt.desc() )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        JPAQuery<Long> count= jpaQueryFactory.select(networking.count()).from(networking).where(
                categoryContains(category),
                departmentIdEq(departmentId),
                targetYearEq(targetYear));

        return PageableExecutionUtils.getPage(networkings, pageable, count::fetchOne);
    }
    private BooleanExpression categoryContains(String keyword) {
        if( keyword == null ) return null;
        return !keyword.isEmpty() ? networking.category.like( "%" + keyword + "%") : null;
    }

    private BooleanExpression targetYearEq(Integer targetYear) {
        if( targetYear == null ){
            return null;
        }
        if( targetYear == 0 ){
            return null;
        }
        return networking.createdAt.year().eq(targetYear);
    }

    private BooleanExpression departmentIdEq(Integer departmentId) {
        if( departmentId == null ){
            return null;
        }

//        System.out.println("departmentId: " + departmentId);

        return networking.member.department.id.eq(departmentId);
    }

}
