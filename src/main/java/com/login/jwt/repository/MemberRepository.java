package com.login.jwt.repository;

import com.login.jwt.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByUserID(String userID);
    Optional<Member> findMemberByUserID(String userID);
}
