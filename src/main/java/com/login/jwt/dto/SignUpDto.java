package com.login.jwt.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SignUpDto {

    @NotBlank(message = "이름을 적어주세요")
    private String name;

    @NotBlank(message="핸드폰 번호를 입력해주세요")
    private String phone;

    @NotBlank(message="ID를 입력해주세요")
    private String userID;

    @NotBlank(message ="비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "권한을 입력해주세요")
    private Long roleId;

}
