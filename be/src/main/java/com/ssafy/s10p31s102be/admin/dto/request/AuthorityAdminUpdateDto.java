package com.ssafy.s10p31s102be.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityAdminUpdateDto {
    public String authName;
    public Integer authLevel;
    public List< Integer > memberIds;
    public String authDescription;
}
