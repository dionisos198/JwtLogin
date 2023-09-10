package com.login.jwt.config.jwt;

import com.login.jwt.domain.Member;
import com.login.jwt.exception.MemberNotFoundException;
import com.login.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;



@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByUserID(username)
                .map(this::getUserDetails)
                .orElseThrow(MemberNotFoundException::new);
    }

    public UserDetails getUserDetails(Member member) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(member.getUserID(), member.getPassword(), Collections.singleton(authority));
    }
}
