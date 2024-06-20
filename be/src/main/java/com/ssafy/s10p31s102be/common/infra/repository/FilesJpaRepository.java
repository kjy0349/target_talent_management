package com.ssafy.s10p31s102be.common.infra.repository;

import com.ssafy.s10p31s102be.common.infra.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesJpaRepository extends JpaRepository<Files, Long> {
}
