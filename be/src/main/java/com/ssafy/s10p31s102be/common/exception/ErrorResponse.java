package com.ssafy.s10p31s102be.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String timeStamp;
    private HttpStatus httpStatus;
    private String errorName;
    private String errorMessage;
    private String path;


    @Override
    public String toString() {
        return "{"
                + "timeStamp="
                + timeStamp
                + ", httpStatus="
                + httpStatus
                + ", errorName='"
                + errorName
                + '\''
                + ", errorMessage='"
                + errorMessage
                + '\''
                + ", path='"
                + path
                + '\''
                + '}';
    }
}

