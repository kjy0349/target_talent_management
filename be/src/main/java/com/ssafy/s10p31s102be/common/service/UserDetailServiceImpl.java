package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.dto.request.AccountDto;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {
    private final MemberJpaRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String knoxId) throws UsernameNotFoundException {
        Member member = memberRepository.findByKnoxId(knoxId)
                .orElseThrow(() -> new MemberNotFoundException(knoxId, this));

        return new UserDetailsImpl(new AccountDto(member.getName(), member.getPassword(), member.getAuthority().getAuthName(), member.getId(), member.getAuthority().getAuthLevel(), member.getDepartment().getId(), member.getTeam().getId()));
    }
}
