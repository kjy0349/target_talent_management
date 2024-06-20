package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectCompany;
import java.util.List;

public interface TechCompanyService {
    List<TechmapProjectCompany> readTechCompanies(Integer techmapProjectId);

    TechmapProject updateTechCompanies(Integer techmapProjectId, List<Integer> companies);
}
