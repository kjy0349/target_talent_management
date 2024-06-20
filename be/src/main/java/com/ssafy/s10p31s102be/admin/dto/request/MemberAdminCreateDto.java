package com.ssafy.s10p31s102be.admin.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAdminCreateDto {

    private Integer id;

    private String name;

    private String profileImage;

    private String knoxId;

    private String password;

    private String telephone;

    private LocalDateTime lastAccessDate;

    private LocalDateTime lastProfileUpdateDate;

    private Boolean isSecuritySigned;

    private Boolean mustChangePassword;

    private Integer departmentId;

    private Integer teamId;

    private Integer roleId;

    private Integer authorityId;

}
