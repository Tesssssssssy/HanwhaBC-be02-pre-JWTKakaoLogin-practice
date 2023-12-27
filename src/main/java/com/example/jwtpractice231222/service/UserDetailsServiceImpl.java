package com.example.jwtpractice231222.service;

import com.example.jwtpractice231222.model.Member;
import com.example.jwtpractice231222.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Optional<Member> result = memberRepository.findByEmail(username);
        Member member = null;
        if(result.isPresent()) {
            member = result.get();
        }

        return member;
    }
}
