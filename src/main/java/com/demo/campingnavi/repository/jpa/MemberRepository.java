package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query("SELECT EXISTS(SELECT m FROM Member m WHERE m.username = :username)")
    public boolean existsByUsername(String username);

    @Query("SELECT m FROM Member m WHERE m.username = :username")
    public Member findByUsername(String username);

    @Query("SELECT EXISTS(SELECT m FROM Member m WHERE m.nickname = :nickname)")
    public boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(String email);
}