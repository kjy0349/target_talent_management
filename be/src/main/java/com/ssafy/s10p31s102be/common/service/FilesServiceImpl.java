package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.dto.request.FilesUploadRequestDto;
import com.ssafy.s10p31s102be.common.exception.FileEmptyException;
import com.ssafy.s10p31s102be.common.exception.FileSizeOverException;
import com.ssafy.s10p31s102be.common.exception.FileTypeNotSupportedException;
import com.ssafy.s10p31s102be.common.exception.InternalServerException;
import com.ssafy.s10p31s102be.common.infra.entity.Files;
import com.ssafy.s10p31s102be.common.infra.repository.FilesJpaRepository;
import com.ssafy.s10p31s102be.common.util.FilesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {
    private final FilesJpaRepository filesRepository;
    private final FilesUtil filesUtil;
    private final long MAX_FILE_SIZE = 10 * 1024L * 1024L;

    @Override
    public Files save(MultipartFile file, String type) {
        //파일 저장
        writeFile(file, type);
        return filesRepository.save(Files.from(file, filesUtil.getImgPath(), filesUtil.getImageFolderName(file.getContentType())));
    }

    private void validateFile(MultipartFile multipartFile, String type) {
        if (multipartFile == null) {
            throw new FileEmptyException(this);
        }

        if (!isFileTypeAppropriate(multipartFile, type)) {
            throw new FileTypeNotSupportedException(this);
        }
        // 10MB가 최대 사이즈이다. 즉, 50KB는 이미지 사이즈 최대 크기이기에, 고려해야한다.
        if (multipartFile.getContentType().contains("image")) {
            if (!isFileSizeAppropriate(multipartFile)) {
                throw new FileSizeOverException(this);
            }
        }
    }

    private void writeFile(MultipartFile multipartFile, String type) {
        validateFile(multipartFile, type);
        String fileName = multipartFile.getOriginalFilename();
        String dirPath = createDirectoryPath(multipartFile);
        String filePath = dirPath + fileName;
        log.info(filePath + " 의 지정된 경로에 파일 생성 시도");

        File directory = createDirectory(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = createFile(filePath);
        try {
            FileOutputStream fos = createFileOutputStream(file);
            writeToFileOutputStream(fos, multipartFile);
            closeFileOutputStream(fos);
        } catch (IOException e) {
            log.warn("파일 쓰기간 에러 발생: " + e.getMessage());
            throw new InternalServerException("파일 쓰기간 에러가 발생했습니다.", this);
        }
    }

    protected File createDirectory(String dirPath) {
        return new File(dirPath);
    }

    protected File createFile(String filePath) {
        return new File(filePath);
    }

    protected String createDirectoryPath(MultipartFile multipartFile) {
        return filesUtil.getImgPath() + filesUtil.getImageFolderName(multipartFile.getContentType());
    }

    protected FileOutputStream createFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    protected void writeToFileOutputStream(FileOutputStream fos, MultipartFile multipartFile) throws IOException {
        fos.write(multipartFile.getBytes());
    }

    protected void closeFileOutputStream(FileOutputStream fos) throws IOException {
        fos.close();
    }

    private Boolean isFileTypeAppropriate(MultipartFile multipartFile, String fileType) {
        String type = multipartFile.getContentType();

        if (fileType.equals("image")) {
            return type.startsWith("image/");
        } else {
            return type.equals("application/vnd.ms-excel") ||
                    type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                    type.equals("application/pdf") ||
                    type.equals("application/x-zip-compressed") ||
                    type.startsWith("image/") ||
                    type.equals("application/zip") ||
                    type.equals("application/msword") ||
                    type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }
    }

    private Boolean isFileSizeAppropriate(MultipartFile multipartFile) {
        return multipartFile.getSize() <= MAX_FILE_SIZE;
    }
}
