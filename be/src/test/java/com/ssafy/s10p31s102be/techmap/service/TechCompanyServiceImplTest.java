package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import com.ssafy.s10p31s102be.profile.infra.repository.CompanyJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.entity.techmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectCompany;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechCompanyServiceImplTest {
    @Mock
    TechmapProjectJpaRepository techmapProjectRepository;

    @Mock
    CompanyJpaRepository companyRepository;

    @InjectMocks
    TechCompanyServiceImpl techCompanyService;

    private techmapProject mocktechmapProject;
    private Company mockCompany;
    private List<String> mockCompanies;
    private TechmapProjectCompany mocktechmapProjectCompany;

    @BeforeEach
    void 초기_인재Pool_프로젝트_연관객체_등록하기(){
        mocktechmapProject = new techmapProject();
        mockCompany = new Company();
        mockCompanies = new ArrayList<>();
        mocktechmapProjectCompany = new TechmapProjectCompany(mocktechmapProject, mockCompany);
    }

    @Test
    void 인재Pool_프로젝트에_등록해놓은_모든_회사들을_조회_가능하다() {
        //given
        Integer techmapProjectId = 1;

        when(techmapProjectRepository.findById(techmapProjectId)).thenReturn(Optional.of(mocktechmapProject));
        //when
        List<TechmapProjectCompany> result = techCompanyService.readTargetCompanies(techmapProjectId);

        //then
        assertThat(result).isNotNull();

        verify(techmapProjectRepository).findById(techmapProjectId);
    }

    @Test
    void 회사_목록을_업데이트가_가능하다() {
        //given
        Integer techmapProjectId = 1;
        List<Integer> ids = List.of(1,2,3);

        when(companyRepository.findById(any())).thenReturn(Optional.of(mockCompany));
        when(techmapProjectRepository.findById(techmapProjectId)).thenReturn(Optional.of(mocktechmapProject));

        //when
        techmapProject result = techCompanyService.updateTargetCompanies(techmapProjectId, ids);

        //then
        assertThat(result).isNotNull();
        assertThat(result.gettechmapProjectCompanies().size()).isEqualTo(3);
        verify(techmapProjectRepository).save(any());
    }
}