package com.login.jwt.config.jwt;

import com.login.jwt.domain.Member;
import com.login.jwt.domain.MemberRole;
import com.login.jwt.domain.Role;
import com.login.jwt.exception.MemberNotFoundException;
import com.login.jwt.repository.MemberRepository;
import com.login.jwt.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;



@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByUserID(username)
                .map(this::getUserDetails)
                .orElseThrow(MemberNotFoundException::new);
    }

    public UserDetails getUserDetails(Member member) {
        List<MemberRole> memberRoles =memberRoleRepository.findByMember_UserID(member.getUserID());



        // 권한 정보를 GrantedAuthority 객체로 변환
        Collection<? extends GrantedAuthority> authorities = memberRoles.stream()
                .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole().getName()))
                .collect(Collectors.toList());

        return new User(member.getUserID(), member.getPassword(), authorities);
        //user는 userDetails를 상속 받는다.즉 userDetails가 더 상위 개념
    }
}
