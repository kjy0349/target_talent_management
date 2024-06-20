package com.ssafy.s10p31s102be.common.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Getter
public class FilesUtil {
    private final String imgPath;

    public FilesUtil(){
        String pwd = System.getProperty("user.home");
        this.imgPath = pwd + File.separator + "files" + File.separator;
    }

    public String getImageFolderName( String type ){
        if( type.equals("application/vnd.ms-excel") ||
                type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ){
            return "excel" + File.separator;
        }else if( type.equals("application/pdf") ){
            return "pdf" + File.separator;
        }else{
            return "img" + File.separator;
        }
    }
}
