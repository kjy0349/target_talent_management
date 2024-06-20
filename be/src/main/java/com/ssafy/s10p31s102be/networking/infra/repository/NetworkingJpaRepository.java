package com.ssafy.s10p31s102be.networking.infra.repository;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.networking.infra.enums.NetworkingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface NetworkingJpaRepository extends JpaRepository<Networking,Integer> {

    @Query("select distinct n from Networking n join fetch n.networkingProfiles np join fetch np.profile p join fetch p.careers join fetch p.educations where n.member.id = :memberId")
    List<Networking> findNetworkingsByMemberIdWithProfile(@Param("memberId") Integer memberId);

    @Query("select distinct n from Networking n " +
            "join fetch n.networkingProfiles np " +
            "where n.id = :networkId")
    Optional<Networking> findNetworkingByIdWithNetworkingProfiles( @Param("networkId") Integer networkId);

    @Query("select n from Networking n " +
            "join fetch n.networkingProfiles np " +
            "join fetch n.executive " +
            "where n.id = :networkId")
    Optional<Networking> findNetworkingByIdWithNetworkingProfilesAndExecutive( @Param("networkId") Integer networkId );

    @Query("select count(n.id) from Networking n where n.member.id = :memberId and n.networkingStatus = :status")
    Integer getCountByMemberIdAndNetworkingStatus(@Param("memberId") Integer memberId, @Param("status") NetworkingStatus networkingStatus);

    @Query("select distinct n from Networking n " +
            "where n.member.id = :memberId")
    List<Networking> findNetworkingsByMemberId(@Param("memberId") Integer memberId);
}
