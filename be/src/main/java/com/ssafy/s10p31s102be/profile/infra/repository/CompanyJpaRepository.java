package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyJpaRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByName(String name);

    @Query("select distinct c.name from Company c " +
            "where upper(c.name) like concat('%', upper(:word), '%')")
    List<String> findCompaniesByCompanyName(@Param("word") String word);
}
