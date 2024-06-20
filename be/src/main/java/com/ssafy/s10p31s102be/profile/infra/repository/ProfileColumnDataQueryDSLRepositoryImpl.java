package com.ssafy.s10p31s102be.profile.infra.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

import static com.ssafy.s10p31s102be.profile.infra.entity.QProfileColumnData.profileColumnData;

@Repository
public class ProfileColumnDataQueryDSLRepositoryImpl implements ProfileColumnDataQueryDSLRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public ProfileColumnDataQueryDSLRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(this.entityManager);
    }

    @Override
    public List<String> getSimilarName(UserDetailsImpl userDetails, String nameSearchString) {
        Integer memberId = userDetails.getMemberId();
        Integer departmentId = userDetails.getMemberId();
        if (userDetails.getAuthorityLevel() == 1) { // 관리자일 경우, 모든 프로필에 대한 자동완성 제공
            return queryFactory.select(profileColumnData.content)
                    .from(profileColumnData)
                    .where(
                            (profileColumnData.profileColumn.name.eq("name").or(profileColumnData.profileColumn.name.eq("nameEng"))).and(profileColumnData.content.like(nameSearchString+"%"))
                    )
                    .orderBy(profileColumnData.content.asc())
                    .limit(5)
                    .fetch();
        } else if (userDetails.getAuthorityLevel() == 2) { // 운영진일 경우, 전사/부서별 프로필에 대해서 가능.
            return queryFactory.select(profileColumnData.content)
                    .from(profileColumnData)
                    .where(
                            (profileColumnData.profileColumn.name.eq("name").or(profileColumnData.profileColumn.name.eq("nameEng"))).and(profileColumnData.content.like(nameSearchString+"%")),
                            profileColumnData.profile.isPrivate.isFalse().or(profileColumnData.profile.isPrivate.isTrue().and(profileColumnData.profile.manager.id.eq(memberId)))
                    )
                    .orderBy(profileColumnData.content.asc())
                    .limit(5)
                    .fetch();
        } else {
            return queryFactory.select(profileColumnData.content)
                    .from(profileColumnData)
                    .where(
                            (profileColumnData.profileColumn.name.eq("name").or(profileColumnData.profileColumn.name.eq("nameEng"))).and(profileColumnData.content.like(nameSearchString+"%")),
                            profileColumnData.profile.isPrivate.isFalse().or(profileColumnData.profile.isPrivate.isTrue().and(profileColumnData.profile.manager.id.eq(memberId))),
                            profileColumnData.profile.isAllCompany.isTrue().or(profileColumnData.profile.isAllCompany.isFalse().and(profileColumnData.profile.manager.department.id.eq(departmentId)))
                    )
                    .orderBy(profileColumnData.content.asc())
                    .limit(5)
                    .fetch();
        }
    }
}
