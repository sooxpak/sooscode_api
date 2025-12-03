package com.sooscode.sooscode_api.application.livekit.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/livekit")
public class LivekitController {

    @Value("${livekit.api-key}")
    private String apiKey;

    @Value("${livekit.api-secret}")
    private String apiSecret;

    @PostMapping("/token")
    public Map<String, String> createToken(@RequestBody Map<String, String> req) {

        String room = req.get("room");
        String identity = req.get("identity");

        // 현재 시간 (초 단위)
        long now = System.currentTimeMillis() / 1000L;

        // Grant 설정
        Map<String, Object> grant = new HashMap<>();
        grant.put("room", room);
        grant.put("roomJoin", true);
        grant.put("canPublish", true);
        grant.put("canSubscribe", true);

        // SecretKey 생성
        SecretKey key = Keys.hmacShaKeyFor(apiSecret.getBytes(StandardCharsets.UTF_8));

        // JJWT로 토큰 생성
        String token = Jwts.builder()
                .setIssuer(apiKey)              // iss
                .setSubject(identity)           // sub
                .claim("nbf", now - 10)         // nbf
                .setExpiration(new Date((now + 3600) * 1000))  // exp
                .claim("video", grant)          // grant
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Map.of("token", token);
    }
}