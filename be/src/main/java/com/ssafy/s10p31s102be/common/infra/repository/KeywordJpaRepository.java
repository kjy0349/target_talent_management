package com.ssafy.s10p31s102be.common.infra.repository;

import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordJpaRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByTypeAndData(KeywordType type, String data);

    @Query("select k from Keyword k " +
            "where k.type = :type " +
            "and k.count >= :count " +
            "and (:query is null or upper(k.data) like concat('%', upper(:query), '%')) " +
            "order by k.count desc " +
            "limit 5")
    List<Keyword> findDatasByType(@Param("type") KeywordType type, @Param("count") Long count,@Param("query") String query);
}
