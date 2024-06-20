package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.repository.LabJpaRepository;
import com.ssafy.s10p31s102be.techmap.infra.entity.techmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectLab;
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
class TechLabServiceImplTest {
    @Mock
    TechmapProjectJpaRepository techmapProjectRepository;

    @Mock
    LabJpaRepository labRepository;

    @InjectMocks
    TechLabServiceImpl techLabService;

    private techmapProject mocktechmapProject;
    private Lab mockLab;
    private List<String> mocklabs;
    private TechmapProjectLab mocktechmapProjectLab;

    @BeforeEach
    void 초기_인재Pool_프로젝트_연관객체_등록하기(){
        mocktechmapProject = new techmapProject();
        mockLab = new Lab();
        mocklabs = new ArrayList<>();
        mocktechmapProjectLab = new TechmapProjectLab(mocktechmapProject, mockLab);
    }

    @Test
    void 인재Pool_프로젝트에_등록해놓은_모든_연구실들을_조회_가능하다() {
        //given
        Integer techmapProjectId = 1;

        when(techmapProjectRepository.findById(techmapProjectId)).thenReturn(Optional.of(mocktechmapProject));
        //when
        List<TechmapProjectLab> result = techLabService.readTechLabs(techmapProjectId);

        //then
        assertThat(result).isNotNull();

        verify(techmapProjectRepository).findById(techmapProjectId);
    }

    @Test
    void 연구실_목록을_업데이트가_가능하다() {
        //given
        Integer techmapProjectId = 1;
        List<Integer> ids = List.of(1,2,3);

        when(labRepository.findById(any())).thenReturn(Optional.of(mockLab));
        when(techmapProjectRepository.findById(techmapProjectId)).thenReturn(Optional.of(mocktechmapProject));

        //when
        techmapProject result = techLabService.updateTechLabs(techmapProjectId, ids);

        //then
        assertThat(result).isNotNull();
        assertThat(result.gettechmapProjectLabs().size()).isEqualTo(3);
        verify(techmapProjectRepository).save(any());
    }
}