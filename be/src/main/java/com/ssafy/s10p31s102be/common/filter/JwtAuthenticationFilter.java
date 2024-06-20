package com.ssafy.s10p31s102be.common.filter;

import com.ssafy.s10p31s102be.common.service.JwtService;
import com.ssafy.s10p31s102be.common.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);

            // access token이 유효한 경우만 Authentication 객체 생성
            if (null != accessToken) {
                String knoxId = jwtService.isValidToken(accessToken);
                if (knoxId != null) {
                    // Load user details
                    UserDetails userDetails = userDetailService.loadUserByUsername(knoxId);

                    // Create authentication token using userDetails
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Set authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    throw new AuthenticationException();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
