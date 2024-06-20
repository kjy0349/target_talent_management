package com.ssafy.s10p31s102be.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

//    private String timeStamp;
    private String errorMessage;
//    private String path;


    @Override
    public String toString() {
        return "{"
//                + "timeStamp="
//                + timeStamp
                + ", errorMessage='"
                + errorMessage
                + '\''

//                + ", path='"
//                + path
//                + '\''
                + '}';
    }
}

