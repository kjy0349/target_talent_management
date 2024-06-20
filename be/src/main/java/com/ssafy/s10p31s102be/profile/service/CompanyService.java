package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.profile.dto.request.CompanyCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;

public interface CompanyService {
    Company companyCreate(CompanyCreateDto companyCreateDto);
}
