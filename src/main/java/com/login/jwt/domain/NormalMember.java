package com.login.jwt.domain;

import com.login.jwt.dto.SignUpDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;


@Getter
@Entity
@AllArgsConstructor
public class NormalMember extends Member {
    public NormalMember (String name,String phone,String userID,String password){
        this.name=name;
        this.phone=phone;
        this.userID=userID;
        this.password=password;
        this.authority=Authority.ROLE_NORMAL;
    }

}
