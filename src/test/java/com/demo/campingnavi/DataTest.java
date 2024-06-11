package com.demo.campingnavi;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.service.DataService;
import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DataTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DataService dataService;

    @Disabled
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

    @Disabled
    @Test
    public void CampInput () {
        String csvFile = "campingData.csv";
        csvFile = PathConfig.realPath(csvFile);
        String n = "all";
        List<Camp> campList = dataService.campInFromCsv(csvFile, n);
    }
}
