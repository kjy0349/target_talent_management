package com.ssafy.s10p31s102be.networking.infra.entity;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import com.ssafy.s10p31s102be.networking.dto.request.NetworkingCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Networking extends BaseTimeEntity {

    @Builder
    public Networking(String category, NetworkingStatus networkingStatus, Member member, Executive executive) {
//        this.selectDescription = selectDescription;
        this.category = category;
        this.networkingStatus = networkingStatus;
        this.member = member;
        this.executive = executive;
        this.category = category;
    }

    @Id
    @GeneratedValue
    private Integer id;

//    private String selectDescription; //선정 이유

    private String category; // 분야 // 추가페이지 네트워킹 현황 참조.

    private NetworkingStatus networkingStatus;

//    private Boolean isDeleted = false;


//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "profile_id")
//    private Profile profile;
    //생성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executive_id")
    private Executive executive;

//    @OneToMany(mappedBy = "networking", fetch = FetchType.LAZY)
//    private List<Profile> networkingProfiles = new ArrayList<>();
    @OneToMany(mappedBy = "networking", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkingProfile> networkingProfiles = new ArrayList<>();

    //Post Create용
    public Networking(NetworkingCreateDto dto, Member member, Executive executive) {

        this.category=dto.getCategory();
        this.member = member;
        this.executive = executive;
        this.networkingStatus = NetworkingStatus.READY_NETWORKING;
//        this.selectDescription = dto.getSelectDescription();
    }
    public void update( Executive executive ){
        this.executive = executive;
    }
    public void update(String category, Member member,NetworkingStatus status, Executive executive){
        this.category = category;
//        this.selectDescription = selectDescription;
        this.executive = executive;
        this.networkingStatus = status;
        this.networkingProfiles = networkingProfiles;
        this.member = member;
    }
    public void removeExecutive(){
        this.executive.getNetworkings().stream().filter( n -> !n.getId().equals(this.id)).toList();
        this.executive = null;

    }
    public void removeMember(){
        this.member=null;
    }

    public void updateStatus(NetworkingStatus networkingStatus) {
        this.networkingStatus = networkingStatus;
    }

//    public void delete(){
//        this.isDeleted = true;
//    }
}
