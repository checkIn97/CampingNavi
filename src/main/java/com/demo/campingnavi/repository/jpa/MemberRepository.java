package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}