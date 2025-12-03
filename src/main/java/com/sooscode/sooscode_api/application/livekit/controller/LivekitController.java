package com.sooscode.sooscode_api.application.livekit.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class LivekitController {

    // 반드시 Cloud에서 복사한 값 그대로 넣기
    // 추후 env로 수정
    private final String apiKey = "API832zGJibjzCg";
    private final String apiSecret = "sj8hPwynJ30LzaL8fjGPsXBtZj6sRDT6aggpC18AlCZ";

    //Backend를 통해서 Token을 발행하는 코드
    @PostMapping("/token")
    public Map<String, String> createToken(@RequestBody Map<String, String> req) {

        //RequestBody를 통해서 RoomName과 사용자의 Identity를 JSON의 형태로 받는다
        String room = req.get("room");
        String identity = req.get("identity");

        //apiSecret키를 HMAC256 알고리즘을 설정해서 algorithm 객체에 넣음
        Algorithm algorithm = Algorithm.HMAC256(apiSecret);

        //토큰 길이를 위한 현재 시간 설정
        long now = System.currentTimeMillis() / 1000L;

        //Grant는 입장 권한 설정파트
        //grant는 hashMap을 통해서 객체를 담는다
        Map<String, Object> grant = new HashMap<>();
        grant.put("room", room); // 방의 이름
        grant.put("roomJoin", true); // 방에 입장 가능한지
        grant.put("canPublish", true); // 카메라/마이크 송출 권한
        grant.put("canSubscribe", true); // 다른 사람 영상 수신 가능

        String token = JWT.create()
                .withIssuer(apiKey)              // iss (livekit api key)
                .withSubject(identity)           // sub ( 사용자의 identity )
                .withClaim("nbf", now - 10)      // nbf ( 언제부터 유효한 토큰인지 )
                .withClaim("exp", now + 3600)    // exp ( 유효기간 )
                .withClaim("video", grant)       // grant ( grant 설정 정보 )
                .sign(algorithm); // sign ( algorithm 객체에 담긴 apiScretKey를 통해 인증값 설정 )

        // 위에서 설정한 token 객체를 json의 형태로 프론트에 return
        return Map.of("token", token);
    }


}