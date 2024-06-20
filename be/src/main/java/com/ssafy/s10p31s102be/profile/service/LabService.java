package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.profile.dto.request.LabCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;

public interface LabService {
    Lab createLab(LabCreateDto labCreateDto);
}
