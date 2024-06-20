package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumn;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface ProfileColumnJpaRepository extends JpaRepository<ProfileColumn, String> {
    Set<ProfileColumn> findAllByRequiredTrue();

    Optional<ProfileColumn> findByName(String name);

    Optional<ProfileColumn> findByLabel(String label);

    @Query("select p from ProfileColumn p where p.label in :labels")
    Set<ProfileColumn> findAllByLabels(@Param("labels") Set<String> labels);
}
