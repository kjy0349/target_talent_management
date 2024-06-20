package com.ssafy.s10p31s102be.common.infra.entity;


import com.ssafy.s10p31s102be.common.util.FilesUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Files extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String source;

    public Files(String name, String source, String imgUrl, String type) {
        this.name = name;
        this.source = source;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    private String imgUrl;

    private String type;

    private Boolean isDeleted = false;

    public static Files from(MultipartFile multipartFile, String filePath, String folderName){
        Files file = new Files();
        file.name = multipartFile.getName();
        file.source = filePath + multipartFile.getContentType() + File.separator + multipartFile.getOriginalFilename();
        file.type = multipartFile.getContentType();
        file.imgUrl = "upload"+File.separator + "files"+File.separator+ folderName + multipartFile.getOriginalFilename();
        return file;
    }

}
