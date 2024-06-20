package com.ssafy.s10p31s102be.networking.infra.repository;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkingProfileJpaRepository extends JpaRepository<NetworkingProfile,Integer> {

    @Modifying
    @Query("delete from NetworkingProfile np where np.networking = :networking")
    void deleteByNetworking(@Param("networking")Networking networking);

    @Modifying
    @Query("delete from NetworkingProfile np where np.networkingProfileId =:networkingProfileId")
    void deleteByNetworkingProfileId(@Param("networkingProfileId")Integer networkingProfileId);

    @Query("select np from NetworkingProfile np where np.profile =: profile")
    List<NetworkingProfile> findAllByProfile(@Param("profile")Profile profile);
}
