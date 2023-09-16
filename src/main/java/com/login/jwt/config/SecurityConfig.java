package com.login.jwt.config;

import com.login.jwt.config.jwt.JwtAccessDeniedHandler;
import com.login.jwt.config.jwt.JwtAuthenticationEntryPointHandler;
import com.login.jwt.config.jwt.JwtSecurityConfig;
import com.login.jwt.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
   private final JwtAccessDeniedHandler accessDeniedHandler;
   private final TokenProvider tokenProvider;
   private final RedisTemplate<String,String> redisTemplate;


   @Bean
   public BCryptPasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
   }

   @Bean
   public SecurityFilterChain configure(HttpSecurity http) throws Exception {
      return http.formLogin().disable()
              .csrf().disable()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .exceptionHandling()
              .authenticationEntryPoint(authenticationEntryPointHandler)
              .accessDeniedHandler(accessDeniedHandler)
              .and()
              .authorizeRequests()
              .antMatchers("/auth/**").permitAll()
              .antMatchers("/normal").access("hasRole('NORMAL')")
              .antMatchers("/vip").hasRole("VIP")
              .anyRequest().authenticated()
              .and()

              .apply(new JwtSecurityConfig(tokenProvider,redisTemplate))
              .and().build();


   }

}
