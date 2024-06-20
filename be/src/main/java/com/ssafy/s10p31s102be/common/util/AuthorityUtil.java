package com.ssafy.s10p31s102be.common.util;

import com.ssafy.s10p31s102be.common.exception.InvalidAuthorizationException;
import com.ssafy.s10p31s102be.member.infra.entity.Member;

public class AuthorityUtil {

    // 관리자 or 운영진이거나, 파라미터로 지정된 최소 권한이 넘고 서로 부서가 같다면 Exception이 발생하지 않는다.
    // "본인 부서의 데이터를 볼 수 있는" 일반적인 권한체크에 사용 가능

    public static void validateAuthority(Member owner, UserDetailsImpl userDetails, int minimumAuthority) {
        boolean isAuthorized = userDetails.getAuthorityLevel() <= 2
                || (userDetails.getAuthorityLevel() <= minimumAuthority
                && owner.getDepartment().getId().equals(userDetails.getDepartmentId()));

        if (!isAuthorized) {
            throw new InvalidAuthorizationException(userDetails.getMemberId());
        }
    }
}
