package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.QProfile;
import com.ssafy.s10p31s102be.techmap.infra.entity.QTechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.QTechmapProjectProfile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TechmapProjectProfileRepositoryImpl implements TechmapProjectProfileRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Profile> findProfilesBytechmapProjectConditions(Integer techmapProjectId, Pageable pageable) {
        QProfile profile = QProfile.profile;
        QTechmapProject techmapProject = QTechmapProject.techmapProject;
        QTechmapProjectProfile techmapProjectProfile = QTechmapProjectProfile.techmapProjectProfile;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(techmapProjectProfile.techmapProject.id.eq(techmapProjectId));

        List<Profile> profiles = jpaQueryFactory
                .select(profile)
                .from(profile)
                .join(profile.techmapProjectProfiles, techmapProjectProfile)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = jpaQueryFactory
                .select(profile.count())
                .from(profile)
                .join(profile.techmapProjectProfiles, techmapProjectProfile)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(profiles, pageable, count);
    }
}
