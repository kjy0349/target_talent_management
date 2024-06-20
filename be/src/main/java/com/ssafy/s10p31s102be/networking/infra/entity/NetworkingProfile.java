package com.ssafy.s10p31s102be.networking.infra.entity;


import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class NetworkingProfile {
    @Id
    @GeneratedValue
    private Integer networkingProfileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "networking_id")
    private Networking networking;

    public NetworkingProfile( Networking n, Profile p ){
        this.networking = n;
        this.profile = p;
    }
}

