package com.ssafy.s10p31s102be.member.controller;

import com.ssafy.s10p31s102be.member.dto.response.JobRankDetailDto;
import com.ssafy.s10p31s102be.member.service.JobRankService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobrank")
@RequiredArgsConstructor
public class JobRankController {
    private final JobRankService jobRankService;

    @GetMapping
    public ResponseEntity<List<JobRankDetailDto>> getAllJobRanks() {
        return ResponseEntity.ok().body(jobRankService.findAllJobRanks().stream().map(JobRankDetailDto::fromEntity).toList());
    }
}
