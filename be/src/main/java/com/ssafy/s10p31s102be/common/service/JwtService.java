package com.ssafy.s10p31s102be.common.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;

public interface JwtService {
    String isValidToken(String token);

    List<String> generateToken(String id);
}
