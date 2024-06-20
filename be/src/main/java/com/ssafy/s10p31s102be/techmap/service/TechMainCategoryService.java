package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechMainCategory;
import java.util.List;

public interface TechMainCategoryService {
    List<TechMainCategory> findTechMainCategories();
}
