package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.querydsl.core.BooleanBuilder;
import com.ssafy.s10p31s102be.techmap.infra.entity.QTechmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TechmapJpaRepository extends JpaRepository<Techmap,Integer>, QuerydslPredicateExecutor<Techmap> {
    @Query("select distinct r from Techmap r " +
            "join fetch r.TechmapKeywords rk " +
            "join fetch rk.keyword k " +
            "join fetch r.techmapDepartments rd " +
            "join fetch rd.department " +
            "where r.member.id = :memberId")
    List<Techmap> findtechmapsByMemberId(@Param("memberId") Integer memberId);

    @Query("select distinct r.targetYear as rt, count(r.targetYear) from Techmap r " +
            "join r.techmapDepartments rd " +
            "join rd.department d " +
            "where :departmentId is NULL OR d.id = :departmentId " +
            "group by rt")
    List<Object[]> findTargetYear(@Param("departmentId") Integer departmentId);

    @Query("select r.id from Techmap r " +
            "where r.targetYear = :targetYear")
    List<Long> findtechmapIdsByTargetYear(@Param("targetYear") Integer year);

    default Page<Techmap> findtechmaps(Pageable pageable,
                                       Integer targetYear,
                                       Integer departmentId){
        QTechmap techmap = QTechmap.techmap;

        BooleanBuilder builder = new BooleanBuilder();
        if(targetYear != null){
            builder.and(techmap.targetYear.eq(targetYear));
        }
        if(departmentId != null){
            builder.and(techmap.techmapDepartments.any().department.id.eq(departmentId));
        }

        return findAll(builder, pageable);
    }

}
