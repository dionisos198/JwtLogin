package com.login.jwt.controller.auth;

import com.login.jwt.dto.SignInDto;
import com.login.jwt.dto.SignUpDto;
import com.login.jwt.dto.TokenRequestDto;
import com.login.jwt.dto.TokenResponseDto;
import com.login.jwt.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/save/normal")
    @ResponseStatus(HttpStatus.CREATED)
    public void memberSignUp(@RequestBody SignUpDto signUpDto){
        authService.normalMemberSignUp(signUpDto);
    }

    @PostMapping("/save/vip")
    @ResponseStatus(HttpStatus.CREATED)
    public void nonMemberSignUp(@RequestBody SignUpDto signUpDto){
        authService.VIPMemberSignUp(signUpDto);
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
}
