package com.login.jwt.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
@Getter
public class TokenAddAuthorityDto {
    @NotBlank(message = "을 위하여 accessToken의 값을 입력해주세요.")
    private String accessToken;

    @NotBlank(message = "위하여 refreshToken의 값을 입력해주세요.")
    private String refreshToken;

    @NotBlank(message = "어떤 authority를 추가할건지")
    private Long authorityId;
}
