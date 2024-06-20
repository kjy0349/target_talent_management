package com.ssafy.s10p31s102be.member.service;

import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import java.util.List;

public interface JobRankService {
    List<JobRank> findAllJobRanks();
}
