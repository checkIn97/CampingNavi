package com.demo.campingnavi;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void memberInputTest() {
        Member m = Member.builder()
                .id("123")
                .pw("123")
                .addr1("123")
                .addr2("123")
                .email("123")
                .name("123")
                .phone("123")
                .sex("m")
                .birth("123")
                .useyn("y")
                .nickname("123")
                .build();
        memberRepository.save(m);
    }
}
