package com.example.jwtpractice231222.controller;

import com.example.jwtpractice231222.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Integer expiredTimeMs;

    @RequestMapping("/create")
    public ResponseEntity create() {
        /*
            Claims claims = Jwts.claims();
            claims.put("abc", "qwer");
            claims.put("key1", "value1");
            claims.put("key2", "value2");

            // 서버만 알고 있어야 하는 값.
            // 클라이언트에게 없기 때문에 이 값을 기준으로 다시 같은 알고리즘으로 복호화해서
            // 토큰 값과 비교해서 정확하게 검증할 수 있다.

            byte[] secretBytes = secretKey.getBytes();

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    // 토큰 발급 시간

                    .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                    // db 시간 아니고 서버의 현재 시간을 받아와서 이를 기준으로 저장해야 함.
                    // 만료 시간은 30분 후로 설정한다.
                    // 대개 숫자로 설정하지 않고 변수에 값을 설정해 놓고 하는 게 좋다.

                    .signWith(Keys.hmacShaKeyFor(secretBytes), SignatureAlgorithm.HS256)
                    .compact();

        */

        return ResponseEntity.ok().body(JwtUtils.generateAccessToken("test01", secretKey, expiredTimeMs));
    }

    @RequestMapping("/valid")
    public ResponseEntity valid(String username, String token) {
        /*
            byte[] secretBytes = secretKey.getBytes();

            Date expiredTime = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretBytes))
                    .build()
                    .parseClaimsJwt(token)
                    .getBody().getExpiration();

            Boolean result = expiredTime.before(new Date(System.currentTimeMillis()));
        */

        return ResponseEntity.ok().body(JwtUtils.validate(token, username, secretKey));
    }
}
