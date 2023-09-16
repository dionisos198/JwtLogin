package com.login.jwt.controller.auth;

import com.login.jwt.domain.Member;
import com.login.jwt.dto.*;
import com.login.jwt.service.auth.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

  /*  @PostMapping("/save/normal")
    @ResponseStatus(HttpStatus.CREATED)
    public void memberSignUp(@RequestBody SignUpDto signUpDto){
        authService.normalMemberSignUp(signUpDto);
    }

    @PostMapping("/save/vip")
    @ResponseStatus(HttpStatus.CREATED)
    public void nonMemberSignUp(@RequestBody SignUpDto signUpDto){
        authService.VIPMemberSignUp(signUpDto);
    }
    */
    @PostMapping("/save/member")
    @ResponseStatus(HttpStatus.CREATED)
    public void MemberSignUp(@RequestBody SignUpDto signUpDto){
        authService.MemberSignUp(signUpDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TokenResponseDto login(@RequestBody SignInDto signInDto){
        return authService.MemberSingIn(signInDto);
    }

    @PostMapping("/reIssue")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto reIssue(@RequestBody TokenRequestDto tokenRequestDto){
        return authService.reIssue(tokenRequestDto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestBody TokenRequestDto tokenRequestDto){
       authService.logout(tokenRequestDto);
    }

    @PostMapping("/save/authority")
    @ResponseStatus(HttpStatus.OK)
    public void saveAuthority(@RequestBody RoleDto roleDto){
        authService.saveAuthority(roleDto);
    }
    @GetMapping("/show/authority")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleShowDto> showAuthority(){
        return authService.roleShow();
    }




}
