package com.example.jwtpractice231222.controller;

import com.example.jwtpractice231222.model.Member;
import com.example.jwtpractice231222.model.MemberLoginReq;
import com.example.jwtpractice231222.service.KakaoService;
import com.example.jwtpractice231222.service.MemberService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;
    private KakaoService kakaoService;
    private AuthenticationManager authenticationManager;

    public MemberController(MemberService memberService, KakaoService kakaoService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity login(@RequestBody MemberLoginReq memberLoginReq){

        return ResponseEntity.ok().body(memberService.login(memberLoginReq));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kakao")
    public ResponseEntity kakao(String code) {
        System.out.println(code);

        // 인가 코드로 토큰 받아오는 코드
        String accessToken = kakaoService.getKakaoToken(code);

        // 토큰으로 사용자의 정보를 받아오는 코드
        String userName = kakaoService.getUserInfo(accessToken);

        // 가져온 사용자 정보로 db 확인
        Member member = memberService.getMemberByEmail(userName);
        if (member == null) {
            // 회원 가입이 되어 있지 않으면(db에 없으면) 회원 가입
            memberService.kakaoSignUp(userName);
        }
        // 로그인 처리(JWT 토큰 발급)
        return ResponseEntity.ok().body(memberService.kakaoLogin(userName));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signUp")
    public ResponseEntity signUp(String email, String password) {
        memberService.signUp(email, password);
        return ResponseEntity.ok().body("signUp success");
    }
}
