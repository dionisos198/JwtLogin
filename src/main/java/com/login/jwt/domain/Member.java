package com.login.jwt.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class  Member {
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

    @Enumerated(value = EnumType.STRING)
    protected Authority authority;


}
