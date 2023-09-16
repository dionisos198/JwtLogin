package com.login.jwt.repository;

import com.login.jwt.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {
    List<MemberRole> findByMember_UserID(String userID);

}
