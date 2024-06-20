package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileJpaRepository extends JpaRepository<Profile,Integer> {
    @Query("SELECT p FROM Profile p " +
            "join fetch p.manager pf " +
            "join fetch p.manager.department " +
            "join fetch p.profileColumnDatas pcd " +
            "join fetch pcd.profileColumn pc " +
            "where p.id=:profileId"
    )
    Optional<Profile> findProfileByIdWithProfileColumnDatas(@Param("profileId") Integer profileId);

    @Query("SELECT p FROM Profile p " +
            "join fetch p.manager pf " +
            "join fetch p.manager.department " +
            "join fetch p.profileColumnDatas pcd " +
            "join fetch pcd.profileColumn pc"
    )
    List<Profile> findAllProfilesWithProfileColumnDatas();

    @Query("SELECT p FROM Profile p " +
            "join fetch p.manager pf " +
            "join fetch p.manager.department " +
            "join fetch p.profileColumnDatas pcd " +
            "join fetch pcd.profileColumn pc" +
            " where p.networkings is empty "
    )
    List<Profile> findAllProfilesWithProfileColumnDatasAndNotAndNetworkingNot();

    @Query("SELECT p FROM Profile p " +
            "join fetch p.manager pf " +
            "join fetch p.manager.department " +
//            "join fetch p.profileColumnDatas pcd " +
//            "join fetch pcd.profileColumn pc" +
            " where p.manager.id = :memberId "
    )
    List<Profile> findAllProfilesByMemberId(@Param("memberId") Integer memberId);

    Integer countByManagerId( Integer managerId );

    @Query("SELECT p FROM Profile p " +
            "join fetch p.manager pf " +
            "join fetch p.manager.department d " +
            "join fetch p.profileColumnDatas pcd "  +
            "join fetch pcd.profileColumn pc " +
            "where d.id = :departmentId "
    )
    List<Profile> findAllProfilesByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query("SELECT count(p) FROM Profile p " +
//            "join fetch p.manager pf " +
//            "join fetch pf.department d " +
            "where p.manager.department.id = :departmentId "
    )
    Long findCountsProfilesByDepartmentId(@Param("departmentId") Integer departmentId);

}
