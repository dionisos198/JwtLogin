package com.login.jwt.controller.Member;

import com.login.jwt.domain.Member;
import com.login.jwt.dto.AddAuthorityDto;
import com.login.jwt.dto.TokenAddAuthorityDto;
import com.login.jwt.dto.TokenResponseDto;
import com.login.jwt.repository.MemberRepository;
import com.login.jwt.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
     private final MemberRepository memberRepository;
     private final AuthService authService;
    @GetMapping("/normal")
    @ResponseStatus(HttpStatus.OK)
    public String normal(){
        return "normal";
    }

    @GetMapping("/vip")
    @ResponseStatus(HttpStatus.OK)
    public String vip(){
        return "vip";
    }

    @PostMapping("/add/authority")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDto addAuthority(@RequestBody TokenAddAuthorityDto tokenAddAuthorityDto){
        return authService.addAuthority(tokenAddAuthorityDto);
    }




}
