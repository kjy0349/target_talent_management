package com.ssafy.s10p31s102be.techmap.infra.repository;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechMainCategoryJpaRepository extends JpaRepository<TechMainCategory,Integer> {
    Optional<TechMainCategory> findByTechMainCategoryName(String name);
}
