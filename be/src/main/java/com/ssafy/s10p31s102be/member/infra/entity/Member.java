package com.ssafy.s10p31s102be.member.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.MemberAdminUpdateDto;
import com.ssafy.s10p31s102be.board.infra.entity.Article;
import com.ssafy.s10p31s102be.board.infra.entity.Comment;
import com.ssafy.s10p31s102be.admin.infra.entity.Notification;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectMember;
import com.ssafy.s10p31s102be.techmap.infra.entity.Techmap;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProject;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectMember;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Builder
    public Member(String name, String profileImage, String knoxId, String password, String telephone, String authorizationCode, Integer visitCount, LocalDateTime enrollDate, LocalDateTime lastAccessDate, LocalDateTime lastModifiedDate, LocalDateTime lastProfileUpdateDate, Boolean isSecuritySigned, Boolean mustChangePassword, Department department, Team team, Role role, Authority authority, Notification receiverNotification, Notification senderNotification) {
        this.name = name;
        this.profileImage = profileImage;
        this.knoxId = knoxId;
        this.password = password;
        this.telephone = telephone;
        this.authorizationCode = authorizationCode;
        this.visitCount = visitCount;
        this.lastAccessDate = lastAccessDate;
        this.lastProfileUpdateDate = lastProfileUpdateDate;
        this.isSecuritySigned = isSecuritySigned;
        this.mustChangePassword = mustChangePassword;
        this.department = department;
        this.team = team;
        this.role = role;
        this.authority = authority;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String profileImage;

    @Column(nullable = false)
    private String knoxId;

    @Column(nullable = false)
    private String password;

    private String telephone;

    private String authorizationCode;

    private Integer visitCount;

    private LocalDateTime lastAccessDate;

    private LocalDateTime lastPasswordModifiedDate;

    private LocalDateTime lastProfileUpdateDate;

    private Boolean isSecuritySigned;

    private Boolean mustChangePassword;

    private LocalDateTime lastDeletedAt;

    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

//    @OneToMany(mappedBy = "member")
//    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<SecuritySignConfirm> securitySignConfirms = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Techmap> techmap = new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    private List<TechmapProject> techmapProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<TechmapProjectMember> techmapProjectMembers = new ArrayList<>();

    public void updateDepartment(Department department) {
        this.department = department;
    }

    public void updateAuthority(Authority authority) {
        this.authority = authority;
    }

    public void delete() {
        this.isDeleted = true;
        this.lastDeletedAt = LocalDateTime.now();
    }

    public void update(MemberAdminUpdateDto dto, Team team, Role role, Authority authority, Department department) {
        this.name = dto.getName();
        this.profileImage = dto.getProfileImage();
        this.knoxId = dto.getKnoxId();
        this.password = dto.getPassword();
        this.telephone = dto.getTelephone();
        this.visitCount = 0;
        this.lastProfileUpdateDate = LocalDateTime.now();
        this.isSecuritySigned = false;
        this.mustChangePassword = false;
        this.team = team;
        this.department = department;
        this.authority = authority;
        this.role = role;
    }

    public void updateAuthorizationCode(String code) {
        this.authorizationCode = code;
    }

    public void updatePassword(String password, Boolean mustChangePassword) {
        this.password = password;
        this.mustChangePassword = mustChangePassword;
    }

    public void updateLastAccessDate(LocalDateTime lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public void updateVisitCount() {
        if( visitCount == null ){
            visitCount = 1;
            return;
        }
        this.visitCount++;
    }
}



