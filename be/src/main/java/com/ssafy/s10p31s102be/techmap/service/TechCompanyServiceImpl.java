package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.profile.exception.CompanyNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.repository.CompanyJpaRepository;
import com.ssafy.s10p31s102be.techmap.exception.TechmapProjectNotFoundException;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectCompany;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TechCompanyServiceImpl implements TechCompanyService {
    private final CompanyJpaRepository companyRepository;
    private final TechmapProjectJpaRepository techmapProjectRepository;

    @Override
    public List<TechmapProjectCompany> readTechCompanies(Integer techmapProjectId) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        return techmapProject.getTechmapProjectCompanies();
    }

    /*
        추가로 TechCompany에 넣을 회사 id 리스트를 받아와서 추가해준다.
     */
    @Override
    public TechmapProject updateTechCompanies(Integer techmapProjectId, List<Integer> companies) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        List<TechmapProjectCompany> techmapProjectCompanies = techmapProject.getTechmapProjectCompanies();
        techmapProjectCompanies.clear();

        for (Integer id : companies) {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new CompanyNotFoundException(id, this));

            techmapProject.getTechmapProjectCompanies().add(new TechmapProjectCompany(techmapProject, company));
        }

        techmapProjectRepository.save(techmapProject);

        return techmapProject;
    }
}
