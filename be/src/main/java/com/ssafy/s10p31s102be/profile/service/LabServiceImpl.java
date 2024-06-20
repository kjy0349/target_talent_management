package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.infra.enums.KeywordType;
import com.ssafy.s10p31s102be.profile.dto.request.LabCreateDto;
import com.ssafy.s10p31s102be.profile.exception.LabAlreadyExistException;
import com.ssafy.s10p31s102be.profile.exception.SchoolNotFoundException;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import com.ssafy.s10p31s102be.profile.infra.repository.LabJpaRepository;
import com.ssafy.s10p31s102be.profile.infra.repository.SchoolJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LabServiceImpl implements LabService {
    private final LabJpaRepository labRepository;
    private final SchoolJpaRepository schoolRepository;

    private final KeywordType KEYWORD_IDENTIFIER = KeywordType.MAJOR;

    @Override
    public Lab createLab(LabCreateDto labCreateDto) {
        School school = schoolRepository.findById(labCreateDto.getSchoolId())
                .orElseThrow(() -> new SchoolNotFoundException(labCreateDto.getSchoolId(), this));

        Optional<Lab> existingLab = labRepository.findByLabName(labCreateDto.getLabName());

        // 이미 존재하는 경우 예외 던지기
        if (existingLab.isPresent()) {
            throw new LabAlreadyExistException(labCreateDto.getLabName(), this);
        }

        return labRepository.save(Lab.builder()
                .school(school)
                .labName(labCreateDto.getLabName())
                .labProfessor(labCreateDto.getProfessor())
                .major(labCreateDto.getMajor())
                .build());
    }
}
