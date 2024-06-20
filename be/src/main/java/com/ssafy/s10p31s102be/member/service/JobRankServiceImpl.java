package com.ssafy.s10p31s102be.member.service;

import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.repository.JobRankJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRankServiceImpl implements JobRankService {
    private final JobRankJpaRepository jobRankJpaRepository;
    @Override
    public List<JobRank> findAllJobRanks() {
        return jobRankJpaRepository.findAllNotDeleted();
    }
}
