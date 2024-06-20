package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.infra.repository.OptionsRepositoryImpl;
import com.ssafy.s10p31s102be.common.util.ExcelUtil;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.exception.LabNotFoundException;
import com.ssafy.s10p31s102be.profile.exception.SchoolNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.repository.LabJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.SchoolJpaRepository;
import com.ssafy.s10p31s102be.techmap.dto.response.TechLabReadDto;
import com.ssafy.s10p31s102be.techmap.exception.TechmapProjectNotFoundException;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectLab;
import com.ssafy.s10p31s102be.techmap.infra.repository.TechmapProjectJpaRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class TechLabServiceImpl implements TechLabService {
    private final LabJpaRepository labRepository;
    private final SchoolJpaRepository schoolRepository;
    private final TechmapProjectJpaRepository techmapProjectRepository;
    private final OptionsRepositoryImpl optionsRepository;

    private final ExcelUtil excelUtil;

    @Override
    public List<TechLabReadDto> searchTechLabs(String word) {
        int limitSize = 5;

        List<Lab> labs = optionsRepository.findLabs(word, limitSize);
        return labs.stream().map(TechLabReadDto::fromEntity).toList();
    }

    @Override
    public List<TechmapProjectLab> readTechLabs(Integer techmapProjectId) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        return techmapProject.getTechmapProjectLabs();
    }

    /*
        추가로 TechLab에 넣을 연구실 id 리스트를 받아와서 추가해준다.
     */
    @Override
    public TechmapProject updateTechLabs(Integer techmapProjectId, List<Integer> labs) {
        TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

        List<TechmapProjectLab> techmapProjectLab = techmapProject.getTechmapProjectLabs();
        techmapProjectLab.clear();

        for (Integer id : labs) {
            Lab lab = labRepository.findById(id)
                    .orElseThrow(() -> new LabNotFoundException(id, this));

            techmapProject.getTechmapProjectLabs().add(new TechmapProjectLab(techmapProject, lab));
        }

        techmapProjectRepository.save(techmapProject);

        return techmapProject;
    }

    /*
        ToDo : DB indexing 문제 해결 후  saveAll로 변경 예정(최적화)
     */
    @Override
    public void excelUploadTechLabs(UserDetailsImpl userDetails, List<Integer> techmapProjectIds, MultipartFile file) throws IOException {
        for (Integer techmapProjectId : techmapProjectIds) {
            TechmapProject techmapProject = techmapProjectRepository.findById(techmapProjectId)
                    .orElseThrow(() -> new TechmapProjectNotFoundException(techmapProjectId, this));

            List<TechmapProjectLab> TechmapProjectLabs = techmapProject.getTechmapProjectLabs();
            List<Integer> memoryLabId = new ArrayList<>();
            for (TechmapProjectLab techmapProjectLab : TechmapProjectLabs) {
                memoryLabId.add(techmapProjectLab.getLab().getLabId());
            }

            List<Map<String, String>> TechLabs = excelUtil.excelToMap(file);
            for (Map<String, String> map : TechLabs) {
                String labName = map.get("연구실명");

                // 학교 검색 -> 등록되지 않는 학교를 등록하려 한다면 Exception을 반환한다.
                String schoolName = map.get("학교명");
                School school = schoolRepository.findBySchoolName(schoolName)
                        .orElseThrow(() -> new SchoolNotFoundException(schoolName, this));

                // lab을 검색했을 때 없다면 새로운 lab을 만들어서 등록
                Lab lab = labRepository.findByLabName(labName)
                        .orElseGet(() -> {
                                    Lab newLab = Lab.builder()
                                            .school(school)
                                            .labName(map.get("연구실명"))
                                            .labProfessor(map.get("지도교수명"))
                                            .major(map.get("전공명"))
                                            .build();
                                    return labRepository.save(newLab);
                                }
                        );

                if (!memoryLabId.contains(lab.getLabId())) {
                    memoryLabId.add(lab.getLabId());
                    TechmapProjectLabs.add(new TechmapProjectLab(techmapProject, lab));
                }
            }

            techmapProjectRepository.save(techmapProject);
        }
    }
}
