package com.login.jwt.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@AllArgsConstructor
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public  class  Member  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @Column(name="name",nullable = false)
    protected String name;

    @Column(name = "phone",nullable = false)
    protected String phone;

    @Column(name = "userID",nullable = false)
    protected String userID;

    @Column(name = "password",nullable = false)
    protected String password;


    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    protected List<MemberRole> memberRoles=new ArrayList<>();

    public void addRole(Role role){
        MemberRole memberRole=MemberRole.createMemberRole(role);
        this.memberRoles.add(memberRole);
        memberRole.setMember(this);
    }
    public Member(String name,String phone,String userID,String password){
        this.name=name;
        this.phone=phone;
        this.userID=userID;
        this.password=password;
    }

}
