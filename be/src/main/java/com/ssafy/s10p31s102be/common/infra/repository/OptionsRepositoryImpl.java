package com.ssafy.s10p31s102be.common.infra.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.s10p31s102be.member.infra.entity.Authority;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.entity.Role;
import com.ssafy.s10p31s102be.member.infra.entity.Team;
import com.ssafy.s10p31s102be.profile.infra.entity.Country;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.s10p31s102be.member.infra.entity.QAuthority.authority;
import static com.ssafy.s10p31s102be.member.infra.entity.QDepartment.department;
import static com.ssafy.s10p31s102be.member.infra.entity.QExecutive.executive;
import static com.ssafy.s10p31s102be.member.infra.entity.QJobRank.jobRank;
import static com.ssafy.s10p31s102be.member.infra.entity.QMember.member;
import static com.ssafy.s10p31s102be.member.infra.entity.QRole.role;
import static com.ssafy.s10p31s102be.member.infra.entity.QTeam.team;
import static com.ssafy.s10p31s102be.profile.infra.entity.QCountry.country;
import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCompany.company;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QLab.lab;
import static com.ssafy.s10p31s102be.profile.infra.entity.education.QSchool.school;
import static com.ssafy.s10p31s102be.techmap.infra.entity.QTechMainCategory.techMainCategory;

//import static com.ssafy.s10p31s102be.member.infra.entity.QAuthority.*;
//import static com.ssafy.s10p31s102be.member.infra.entity.QDepartment.department;
//import static com.ssafy.s10p31s102be.member.infra.entity.QExecutive.executive;
//import static com.ssafy.s10p31s102be.member.infra.entity.QJobRank.jobRank;
//import static com.ssafy.s10p31s102be.member.infra.entity.QMember.member;
//import static com.ssafy.s10p31s102be.member.infra.entity.QRole.role;
//import static com.ssafy.s10p31s102be.member.infra.entity.QTeam.team;
//import static com.ssafy.s10p31s102be.profile.infra.entity.QCountry.country;
//import static com.ssafy.s10p31s102be.profile.infra.entity.career.QCompany.company;
//import static com.ssafy.s10p31s102be.profile.infra.entity.education.QLab.lab;
//import static com.ssafy.s10p31s102be.profile.infra.entity.education.QSchool.school;
//import static com.ssafy.s10p31s102be.techmap.infra.entity.QTechMainCategory.techMainCategory;

@Repository
@RequiredArgsConstructor
public class OptionsRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Authority> findAuthority(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = authority.authName.contains(word);
        }

        JPAQuery<Authority> query = jpaQueryFactory.selectFrom(authority)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Department> findDepartments(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = department.name.contains(word);
        }

        JPAQuery<Department> query = jpaQueryFactory.selectFrom(department)
                .where(condition);


        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Executive> findExecutives(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = executive.name.contains(word);
        }

        JPAQuery<Executive> query = jpaQueryFactory.selectFrom(executive)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<JobRank> findJobRanks(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = jobRank.description.contains(word);
        }

        JPAQuery<JobRank> query = jpaQueryFactory.selectFrom(jobRank)
                .where(condition);


        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Member> findMembers(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = member.name.contains(word);
        }

        JPAQuery<Member> query = jpaQueryFactory.selectFrom(member)
                .where(condition);


        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    // 멤버의 조건을 동적으로 생성하는 메서드
    private BooleanExpression nameContains(String word) {
        if (word != null) {
            return member.name.contains(word);
        } else {
            return null;  // word가 null일 경우, 이 조건은 무시됩니다.
        }
    }

    private BooleanExpression departmentIs(Integer departmentId) {
        return member.department.id.eq(departmentId);
    }

    public List<Member> findMembersByDepartmentId(String word, Integer departmentId, int limitSize){
        BooleanExpression condition = departmentIs(departmentId);
        if (word != null){
            condition = condition.and(nameContains(word));
        }

        JPAQuery<Member> query = jpaQueryFactory.selectFrom(member)
                .where(condition);


        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Role> findRoles(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = role.description.contains(word);
        }

        JPAQuery<Role> query = jpaQueryFactory.selectFrom(role)
                .where(condition);


        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Team> findTeams(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = team.name.contains(word);
        }

        JPAQuery<Team> query = jpaQueryFactory.selectFrom(team)
                .where(condition);


        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Company> findCompany(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = company.name.contains(word);
        }

        JPAQuery<Company> query = jpaQueryFactory.selectFrom(company)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Lab> findLabs(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = lab.labName.contains(word);
        }

        JPAQuery<Lab> query = jpaQueryFactory.selectFrom(lab)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<School> findSchools(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = school.schoolName.contains(word);
        }

        JPAQuery<School> query = jpaQueryFactory.selectFrom(school)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<Country> findCountry(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = country.name.contains(word);
        }

        JPAQuery<Country> query = jpaQueryFactory.selectFrom(country)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }

    public List<TechMainCategory> findTechMainCategory(String word, int limitSize){
        BooleanExpression condition = null;
        if (word != null){
            condition = techMainCategory.techMainCategoryName.contains(word);
        }

        JPAQuery<TechMainCategory> query = jpaQueryFactory.selectFrom(techMainCategory)
                .where(condition);

        if (limitSize != -1){
            query = query.limit(limitSize);
        }

        return query.fetch();
    }
}