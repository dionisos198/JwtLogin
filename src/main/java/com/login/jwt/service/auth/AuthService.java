package com.login.jwt.service.auth;

import com.login.jwt.config.jwt.TokenProvider;
import com.login.jwt.domain.Member;
import com.login.jwt.domain.Role;
import com.login.jwt.dto.*;
import com.login.jwt.exception.MemberLoginFailureException;
import com.login.jwt.exception.MemberNotFoundException;
import com.login.jwt.exception.TokenNotCorrectException;
import com.login.jwt.repository.MemberRepository;
import com.login.jwt.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String,String> redisTemplate;
    private final RoleRepository roleRepository;

  /*  @Transactional
    public void normalMemberSignUp(SignUpDto signUpDto) {
      NormalMember member=getNormalMember(signUpDto);
      memberRepository.save(member);

    }
    private NormalMember getNormalMember(SignUpDto signUpDto){
      if(memberRepository.existsByUserID(signUpDto.getUserID())){
          throw new RuntimeException("중복 됩니다");
      }
      return new NormalMember(signUpDto.getName(), signUpDto.getPhone(), signUpDto.getUserID(), passwordEncoder.encode(signUpDto.getPassword()));
    }

    @Transactional
    public void VIPMemberSignUp(SignUpDto signUpDto) {
        VIPMember member=getVIPMember(signUpDto);
        member.addRole(roleRepository.findByName("VIP"));
        memberRepository.save(member);

    }
    private VIPMember getVIPMember(SignUpDto signUpDto){
        if(memberRepository.existsByUserID(signUpDto.getUserID())){
            throw new RuntimeException("중복 됩니다");
        }
        return new VIPMember(signUpDto.getName(), signUpDto.getPhone(), signUpDto.getUserID(), passwordEncoder.encode(signUpDto.getPassword()));
    }*/
    @Transactional
    public void MemberSignUp(SignUpDto signUpDto) {
       Member member=getMember(signUpDto);
       member.addRole(roleRepository.findById(signUpDto.getRoleId()).orElseThrow());
       memberRepository.save(member);

    }
    private Member getMember(SignUpDto signUpDto){
        if(memberRepository.existsByUserID(signUpDto.getUserID())){
            throw new RuntimeException("중복 됩니다");
        }
        return new Member(signUpDto.getName(), signUpDto.getPhone(), signUpDto.getUserID(), passwordEncoder.encode(signUpDto.getPassword()));
    }
    //여기서 부터는 로그인입니다.


    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Transactional
    public TokenResponseDto MemberSingIn(SignInDto signInDto){
        Member findMember=memberRepository.findMemberByUserID(signInDto.getUserID()).orElseThrow(MemberNotFoundException::new);
        if(!passwordEncoder.matches(signInDto.getPassword(),findMember.getPassword())){
            throw new MemberLoginFailureException();
        }
        UsernamePasswordAuthenticationToken authenticationToken=signInDto.getAuthenticationToken();
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto=tokenProvider.createToken(authentication);

        redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);
        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public TokenResponseDto reIssue(TokenRequestDto tokenRequestDto){
       String accessToken=tokenRequestDto.getAccessToken();
       String refreshToken=tokenRequestDto.getRefreshToken();
       Authentication authentication= tokenProvider.getAuthentication(accessToken);

       if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
           throw new TokenNotCorrectException();
       }
       TokenDto tokenDto=tokenProvider.createToken(authentication);
       redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);

       return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(TokenRequestDto tokenRequestDto){
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())){
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());
        redisTemplate.opsForValue().set(tokenRequestDto.getAccessToken(),"logout",expiration,TimeUnit.MILLISECONDS);
    }

    @Transactional
    public void saveAuthority(RoleDto roleDto){
        Role role=new Role(roleDto.getName());
        roleRepository.save(role);
    }
    public List<RoleShowDto> roleShow(){
        return roleRepository.findAll().stream().map(r->new RoleShowDto(r.getId(),r.getName())).collect(Collectors.toList());
    }

    @Transactional
    public TokenResponseDto addAuthority(TokenAddAuthorityDto tokenAddAuthorityDto){
        String accessToken=tokenAddAuthorityDto.getAccessToken();
        String refreshToken=tokenAddAuthorityDto.getRefreshToken();
       Authentication authentication= tokenProvider.getAuthentication(accessToken);


        if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
            throw new TokenNotCorrectException();
        }
        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken,"logout",expiration,TimeUnit.MILLISECONDS);
        Member findMember=memberRepository.findMemberByUserID(authentication.getName()).get();
        Role findRole=roleRepository.findById(tokenAddAuthorityDto.getAuthorityId()).get();
        findMember.addRole(findRole);


        authentication= tokenProvider.addRole(accessToken,findRole);
        TokenDto tokenDto=tokenProvider.createToken(authentication);
        redisTemplate.opsForValue().set(authentication.getName(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);

        return new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());
    }
}
