package com.ssafy.s10p31s102be.techmap.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectCreateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectDuplicateDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectFindDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectPageDto;
import com.ssafy.s10p31s102be.techmap.dto.request.TechmapProjectProfileFindDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapProjectProfilePageDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface TechmapProjectService {
    TechmapProject create(UserDetailsImpl userDetails, TechmapProjectCreateDto techmapProjectCreateDto);

    Integer excelUpload(UserDetailsImpl userDetails, Integer techmapId, MultipartFile file) throws IOException;

    ByteArrayResource excelDownload(UserDetailsImpl userDetails, List<Integer> techmapProjectIds) throws IOException;

    TechmapProjectPageDto findtechmapProjects(UserDetailsImpl userDetails, TechmapProjectFindDto techmapProjectFindDto);

    TechmapProject update(UserDetailsImpl userDetails, Integer techmapProjectId, TechmapProjectCreateDto techmapProjectCreateDto);

    TechmapProject updateManager(UserDetailsImpl userDetails, Integer techmapProjectId, Integer managerId);

    void duplicatetechmapProject(UserDetailsImpl userDetails, TechmapProjectDuplicateDto techmapProjectDuplicateDto);

    TechmapProjectProfilePageDto findProfiles(Integer techmapProjectId, TechmapProjectProfileFindDto techmapProjectProfileFindDto);

    TechmapProject updateProfiles(UserDetailsImpl userDetails, Integer techmapProjectId, List<Integer> profileIds);

    TechmapProject deleteProfiles(UserDetailsImpl userDetails, Integer techmapProjectId, List<Integer> profileIds);

    void delete(UserDetailsImpl userDetails, List<Integer> techmapProjectIds);
}
