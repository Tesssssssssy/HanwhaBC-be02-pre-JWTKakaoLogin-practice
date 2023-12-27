package com.example.jwtpractice231222.service;

import com.example.jwtpractice231222.model.Member;
import com.example.jwtpractice231222.model.MemberLoginReq;
import com.example.jwtpractice231222.model.MemberSignupReq;
import com.example.jwtpractice231222.repository.MemberRepository;
import com.example.jwtpractice231222.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    /*
        이미 UserDetails에서 만들어 놓은 UserDetailsService가 있다.
        여기서 DB와 상관 없이 인증, 인가 기능이 있다.
        그런데 내 DB의 Member 테이블 사용하고 싶다면
        내가 직접 구현해서 사용하면 된다.
    */
    private final MemberRepository memberRepository;
    /*
    private HttpServletRequest httpServletRequest;
        -> Spring이 알아서 Session 관리를 해주기 때문에 굳이 설정해줄 필요가 없다.
    */

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private int expiredTimeMs;

    public Member getMemberByEmail(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public String login(MemberLoginReq memberLoginReq) {
        Member member = memberRepository.findByEmail(memberLoginReq.getEmail()).get();

        if (passwordEncoder.matches(memberLoginReq.getPassword(), member.getPassword())) {
            return JwtUtils.generateAccessToken(member.getEmail(), secretKey, expiredTimeMs);
        }

        return null;
    }

    public void kakaoSignUp(String userName) {      // userName을 email이라고 하고 password는 임의로.
        memberRepository.save(Member.builder()
                        .email(userName)
                        .password(passwordEncoder.encode("kakao"))  // kakao라는 비밀번호로 저장되도록 설정.
                        .authority("ROLE_USER")
                .build());
    }

    public String kakaoLogin(String userName) {
        return JwtUtils.generateAccessToken(userName, secretKey, expiredTimeMs);
    }

    public void signUp(String email, String password) {
        memberRepository.save(Member.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .authority("ROLE_USER")
                .build());
    }

}
