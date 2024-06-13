package com.demo.campingnavi;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void insertMemberTest() {
        Member member = Member.builder()
                .id("123")
                .password("123")
                .name("123")
                .email("123@123")
                .phone("010-1234-1234")
                .addr1("서울시 동작구")
                .nickname("닉")
                .sex("m")
                .birth("1999-11-11")
                .useyn("y")
                .build();

        memberRepository.save(member);
    }
}
