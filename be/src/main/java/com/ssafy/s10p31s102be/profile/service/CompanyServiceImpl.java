package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.profile.dto.request.CompanyCreateDto;
import com.ssafy.s10p31s102be.profile.exception.CompanyAlreadyExistException;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.repository.CompanyJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyJpaRepository companyRepository;

    @Override
    public Company companyCreate(CompanyCreateDto companyCreateDto) {
        Optional<Company> existingCompany = companyRepository.findByName(companyCreateDto.getName());

        // 이미 존재하는 경우 예외 던지기
        if (existingCompany.isPresent()) {
            throw new CompanyAlreadyExistException(companyCreateDto.getName(), this);
        }

        return companyRepository.save(Company.builder()
                .name(companyCreateDto.getName())
                .build());
    }
}
