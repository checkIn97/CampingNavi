package com.demo.campingnavi;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.repository.CampRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class CampRepositoryTest {
    @Autowired
    private CampRepository campRepository;

    @Disabled
    @Test
    public void getCampList() {
        String useyn = "y";
        String name = "";
        String campType = "";
        String addrL = "";
        String addrS = "|";
        List<Camp> campList = campRepository.getCampList(useyn, name, campType, addrL, addrS);
        for (Camp camp : campList) {
            System.out.println(camp.getName());
        }
        System.out.println(campList.size());
    }
}
