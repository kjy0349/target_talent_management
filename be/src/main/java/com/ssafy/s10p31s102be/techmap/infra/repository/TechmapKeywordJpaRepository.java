package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechmapKeywordJpaRepository extends JpaRepository<TechmapKeyword, Long> {
}
