package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.techmap.dto.response.TechLabReadDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectLab;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TechLabService {
    List<TechLabReadDto> searchTechLabs(String word);

    List<TechmapProjectLab> readTechLabs(Integer techmapProjectId);

    TechmapProject updateTechLabs(Integer techmapProjectId, List<Integer> labs);

    void excelUploadTechLabs(UserDetailsImpl userDetails, List<Integer> techmapProjectIds, MultipartFile file) throws IOException;
}
