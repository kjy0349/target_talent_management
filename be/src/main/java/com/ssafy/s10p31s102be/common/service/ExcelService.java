package com.ssafy.s10p31s102be.common.service;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    void excelImport(MultipartFile multipartFile) throws IOException;
}
