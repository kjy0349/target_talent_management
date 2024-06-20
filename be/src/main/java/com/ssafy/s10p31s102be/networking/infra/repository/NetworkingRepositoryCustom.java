package com.ssafy.s10p31s102be.networking.infra.repository;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NetworkingRepositoryCustom {
    Page<Networking> getAllNetworkingsWithFilter(Integer departmentId, Integer targetYear, String category, Pageable pageable);
}
