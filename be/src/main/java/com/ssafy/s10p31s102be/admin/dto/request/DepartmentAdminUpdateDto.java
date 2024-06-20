package com.ssafy.s10p31s102be.admin.dto.request;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAdminUpdateDto {
    private String name;
    private String description;
    private List<Integer> memberIds;
    private Integer managerId;
}
