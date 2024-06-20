package com.ssafy.s10p31s102be.admin.infra.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.admin.dto.request.ProfileAdminFilterSearchDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.s10p31s102be.member.infra.entity.QDepartment.department;
import static com.ssafy.s10p31s102be.member.infra.entity.QJobRank.jobRank;
import static com.ssafy.s10p31s102be.member.infra.entity.QMember.member;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfile.profile;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfileColumn.profileColumn;
import static com.ssafy.s10p31s102be.profile.infra.entity.QProfileColumnData.profileColumnData;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCareer.career;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCompany.company;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QEducation.education;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QSchool.school;

@Repository
public class ProfileAdminSearchRepositoryCustomImpl implements ProfileAdminSearchRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public ProfileAdminSearchRepositoryCustomImpl( EntityManager em ){
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory( em );
    }

    @Override
    public List<Profile> getAllProfilePreviewByFilter(Pageable pageable, ProfileAdminFilterSearchDto profileFilterSearchDto) {
        return jpaQueryFactory.selectFrom(profile)
                .distinct()
                .leftJoin(profile.profileColumnDatas, profileColumnData)
                .leftJoin(profileColumnData.profileColumn, profileColumn)
                .leftJoin(profile.targetJobRank, jobRank)
                .leftJoin(profile.careers, career)
                .leftJoin(career.company, company)
                .leftJoin(profile.educations, education)
                .leftJoin(education.school, school)
                .leftJoin(profile.manager, member)
                .leftJoin(member.department, department)
                .where(
                        column1Eq(profileFilterSearchDto.getcolumn2()),
                        nameEq(profileFilterSearchDto.getNames()),
                        jobRankEq(profileFilterSearchDto.getJobRanks()),
                        companyNameEq(profileFilterSearchDto.getCompanyNames()),
                        careerPeriodBetween(profileFilterSearchDto.getCareerStartedAt(), profileFilterSearchDto.getCareerEndedAt()),
                        degreeEq(profileFilterSearchDto.getDegrees()),
                        schoolNameEq(profileFilterSearchDto.getSchoolNames()),
                        majorEq(profileFilterSearchDto.getMajors()),
                        graduatedAtEq(profileFilterSearchDto.getGraduatedAt()),
                        employStatusEq(profileFilterSearchDto.getEmployStatuses()),
                        departmentNameEq(profileFilterSearchDto.getFoundDeptNames()),
                        founderNameEq(profileFilterSearchDto.getFounderNames()),
                        managerNameEq(profileFilterSearchDto.getNetworkingResponsibleMemberNames()),
                        keywordEq(profileFilterSearchDto.getKeywords())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    private BooleanExpression nameEq(String names) {
        if (names == null || names.isEmpty()) {
            return null;
        }
        return profileColumn.name.eq("name");
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

    private BooleanExpression careerPeriodBetween(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (startedAt == null || endedAt == null) {
            return null;
        }
        return career.startedAt.goe(startedAt).and(career.endedAt.loe(endedAt));
    }

    private BooleanExpression degreeEq(List<Degree> degrees) {
        if (degrees == null || degrees.isEmpty()) {
            return null;
        }
        return education.degree.in(degrees);
    }

    private BooleanExpression schoolNameEq(List<String> schoolNames) {
        if (schoolNames == null || schoolNames.isEmpty()) {
            return null;
        }
        return school.schoolName.in(schoolNames);
    }

    private BooleanExpression majorEq(List<String> majors) {
        if (majors == null || majors.isEmpty()) {
            return null;
        }
        return education.major.in(majors);
    }

    private BooleanExpression graduatedAtEq(LocalDateTime graduatedAt) {
        if (graduatedAt == null) {
            return null;
        }
        return education.graduatedAt.eq(graduatedAt);
    }

    private BooleanExpression employStatusEq(List<String> employStatuses) {
        if (employStatuses == null || employStatuses.isEmpty()) {
            return null;
        }
        return profile.employStatus.stringValue().in(employStatuses);
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

    private BooleanExpression managerNameEq(List<String> managerNames) {
        if (managerNames == null || managerNames.isEmpty()) {
            return null;
        }
        return profile.manager.name.in(managerNames);
    }

    private BooleanExpression keywordEq(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }
        return profile.keyword.data.in(keywords);
    }
}
