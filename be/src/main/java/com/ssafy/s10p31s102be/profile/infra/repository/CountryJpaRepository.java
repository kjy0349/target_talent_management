package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.profile.infra.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryJpaRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByName(String countryName);
}