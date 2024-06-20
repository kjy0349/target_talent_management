package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechmapDepartmentJpaRepository extends JpaRepository<TechmapDepartment,Integer> {
}
