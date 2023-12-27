package com.example.jwtpractice231222.config.filter;

import com.example.jwtpractice231222.model.Member;
import com.example.jwtpractice231222.service.MemberService;
import com.example.jwtpractice231222.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    /*
        Spring Security에서 사용하는 필터는 이 방식으로 만들 수 있다.
        요청 1번 당 딱 1번만 check하는 Filter
    */
    private final MemberService memberService;

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*
            필터들을 연결할 수 있게 해주는 객체가 FilterChain
            token을 client에게 발급해주고 그 다움부터 토큰을 전달받을 것.
            전달받을 토큰을 매번 body에서 전달받게 하는 건 비효율적.
            그래서 token은 header로 대개 처리한다.
        */

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        /*
            client들이 서버에 보낼 때 일단 출력해보자. header가 어떻게 나오고 어떻게 받는지.
            System.out.println(header);
        */

        String token;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.split(" ")[1];
        } else {
            // header가 이 메소드만 하나 실행되는 것이 아니라 다른 filter들도 걔속 실행할 수 있게 해주어야 함.
            filterChain.doFilter(request, response);
            return;
        }

        String username = JwtUtils.getUsername(token, secretKey);

        /*
            인증 과정 수행해서 회원 엔티티를 받아온다.
            member.getUsername(); // 여기서는 가져왔다고 치고 하자.
        */

        Member member = memberService.getMemberByEmail(username);
        String memberUsername = member.getUsername();
        if (!JwtUtils.validate(token, memberUsername, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member, null,
                member.getAuthorities()
        );

        // 인가하는 코드
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
