package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.querydsl.core.BooleanBuilder;
import com.ssafy.s10p31s102be.techmap.infra.entity.QTechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechCompanyRelativeLevel;
import com.ssafy.s10p31s102be.techmap.infra.enums.TechStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TechmapProjectJpaRepository extends JpaRepository<TechmapProject,Integer>, QuerydslPredicateExecutor<TechmapProject> {
    List<TechmapProject> findtechmapProjectsByDepartmentIdAndtechmap(Integer departmentId, Techmap techmap);

    default Page<TechmapProject> findtechmapProjectsByDepartmentId(Integer departmentId, Pageable pageable){
        QTechmapProject techmapProject = QTechmapProject.techmapProject;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(techmapProject.departmentId.eq(departmentId));

        return findAll(builder, pageable);
    }

    default Page<TechmapProject> findtechmapProjects(Pageable pageable,
                                                     Integer techmapId,
                                                     TechCompanyRelativeLevel techCompanyRelativeLevel,
                                                     Integer targetYear,
                                                     TechStatus techStatus,
                                                     Boolean targetStatus,
                                                     Integer departmentId) {
        QTechmapProject techmapProject = QTechmapProject.techmapProject;

        BooleanBuilder builder = new BooleanBuilder();

        if(techmapId != null){
            builder.and(techmapProject.techmap.id.eq(techmapId));
        }

        if(techCompanyRelativeLevel != null){
            builder.and(techmapProject.techCompanyRelativeLevel.eq(techCompanyRelativeLevel));
        }
        if(targetYear != null){
            builder.and(techmapProject.targetYear.eq(targetYear));
        }
        if(techStatus != null){
            builder.and(techmapProject.techStatus.eq(techStatus));
        }
        if(targetStatus != null){
            builder.and(techmapProject.targetStatus.eq(targetStatus));
        }
        if(departmentId != null){
            builder.and(techmapProject.departmentId.eq(departmentId));
        }

        return findAll(builder, pageable);
    }
}
