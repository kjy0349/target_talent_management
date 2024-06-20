package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechMainCategoryJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TechMainCategoryServiceImpl implements TechMainCategoryService{
    private final TechMainCategoryJpaRepository techMainCategoryRepository;

    @Override
    public List<TechMainCategory> findTechMainCategories() {
        return techMainCategoryRepository.findAll();
    }
}
