package com.ssafy.s10p31s102be.common.util;

import com.ssafy.s10p31s102be.common.dto.request.AccountDto;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
    private final AccountDto accountDto;

    public UserDetailsImpl(AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return accountDto.getAuthorityName();
            }
        });
    }

    @Override
    public String getPassword() {
        return accountDto.getPassword();
    }

    @Override
    public String getUsername() {
        return accountDto.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Integer getMemberId(){
        return accountDto.getMemberId();
    }

    public Integer getAuthorityLevel(){
        return accountDto.getAuthorityLevel();
    }

    public Integer getDepartmentId(){
        return accountDto.getDepartmentId();
    }

    public Integer getTeamId(){
        return accountDto.getTeamId();
    }
}
