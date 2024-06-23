package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query("SELECT EXISTS(SELECT m FROM Member m WHERE m.username = ?1 AND m.useyn = 'y')")
    public boolean existsByUsername(String username);

    @Query("SELECT m FROM Member m WHERE m.username = ?1 AND m.useyn = 'y'")
    public Member findByUsername(String username);

    @Query("SELECT EXISTS(SELECT m FROM Member m WHERE m.nickname = ?1 AND m.useyn = 'y')")
    public boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Member m WHERE m.email = ?1 AND m.useyn = 'y'")
    boolean existsByEmail(String email);

    @Query("SELECT m.username FROM Member m WHERE m.name = ?1 AND m.email = ?2 AND m.birth = ?3 AND m.phone = ?4 AND m.provider = ?5 AND m.useyn = 'y'")
    public String getUsername(String name, String email, String birth, String phone, String provider);

    @Query("SELECT m FROM Member m WHERE m.role = 'ROLE_USER'")
    Page<Member> findAll(Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.role = 'ROLE_USER' AND m.username LIKE %?1%")
    Page<Member> findAllByUsername(String username, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.role = 'ROLE_USER' AND m.name LIKE %?1%")
    Page<Member> findAllByName(String name, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.role = 'ROLE_USER' AND m.provider LIKE %?1%")
    Page<Member> findAllByProvider(String provider, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.role = 'ROLE_USER' AND m.email LIKE %?1%")
    Page<Member> findAllByEmail(String email, Pageable pageable);
}