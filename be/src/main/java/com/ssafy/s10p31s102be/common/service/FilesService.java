package com.ssafy.s10p31s102be.common.service;


import com.ssafy.s10p31s102be.common.dto.request.FilesUploadRequestDto;
import com.ssafy.s10p31s102be.common.infra.entity.Files;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    Files save(MultipartFile dto, String type );
}
