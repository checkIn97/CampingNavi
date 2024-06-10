package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.service.CampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;

@Controller
@RequestMapping("/camp")
public class CampController {

    @Autowired
    private CampService campService;

    @RequestMapping("/search")
    public String search(HttpSession session, Model model, CampRecommendVo campRecommendVo,
                         @RequestParam(value="page", defaultValue="0") int page,
                         @RequestParam(value="size", defaultValue="5") int size,
                         @RequestParam(value="sortBy", defaultValue="name") String sortBy,
                         @RequestParam(value="sortDirection", defaultValue="ASC") String sortDirection,
                         @RequestParam(value="pageMaxDisplay", defaultValue="5") int pageMaxDisplay,
                         @RequestParam(value="searchField", defaultValue="name") String searchField,
                         @RequestParam(value="searchWord", defaultValue="") String searchWord,
                         @RequestParam(value="campType", defaultValue="") String[] campType,
                         @RequestParam(value="useyn", defaultValue="y") String useyn) {

        // 테스트를 위한 임시 Member
        Member member_init = new Member();
        member_init.builder()
                .mseq(1)
                .sex("m")
                .useyn("y")
                .birth("2000-01-01")
                .id("test")
                .name("testuser")
                .nickname("테스트유저")
                .phone("010-1234-5678")
                .pw("1234")
                .email("test@camp.com")
                .addr1("서울특별시 관악구 신림동 1번지")
                .build();
        session.setAttribute("loginMember", member_init);

        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/index";
        }

        if (page == 0) {
            campRecommendVo.setPage(1);
            campRecommendVo.setSize(size);
            campRecommendVo.setSortBy(sortBy);
            campRecommendVo.setSortDirection(sortDirection);
            campRecommendVo.setPageMaxDisplay(pageMaxDisplay);
            campRecommendVo.setUseyn(useyn);
            campRecommendVo.setSearchField(searchField);

            String[] searchWordInit = new String[campRecommendVo.getSearchWord().length];
            for (int i = 0 ; i < campRecommendVo.getSearchWord().length ; i++) {
                searchWordInit[i] = "";
            }
            campRecommendVo.setSearchWord(searchWordInit);

            String[] campTypeInit = new String[campRecommendVo.getCampType().length];
            for (int i = 0 ; i < campRecommendVo.getCampType().length ; i++) {
                campTypeInit[i] = "";
            }
            campRecommendVo.setCampType(campTypeInit);
            campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
            campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));
            session.setAttribute("campRecommendVo", campRecommendVo);
        } else {
            campRecommendVo = (CampRecommendVo)session.getAttribute("campRecommendVo");
            campRecommendVo.setPage(page);
            campRecommendVo.setSize(size);
            campRecommendVo.setSortBy(sortBy);
            campRecommendVo.setSortDirection(sortDirection);
            campRecommendVo.setPageMaxDisplay(pageMaxDisplay);
            session.setAttribute("campRecommendVo", campRecommendVo);
        }
        campRecommendVo = (CampRecommendVo)session.getAttribute("campRecommendVo");
        model.addAttribute("campRecommendVo", campRecommendVo);

        return "search/searchPage";
    }

    @ResponseBody
    @GetMapping
    public Map<String, Object> reloadList(HttpSession session, CampRecommendVo campRecommendVo,
                                          @RequestParam(value="page") int page,
                                          @RequestParam(value="sortBy") String sortBy,
                                          @RequestParam(value="sortDirection") String sortDirection) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginMember");
        if (member != null) {
            result.put("result", "success");
            campRecommendVo = (CampRecommendVo)session.getAttribute("campRecommendVo");
            if (campRecommendVo.getPage() != page) {
                campRecommendVo.setPage(page);
            } else if (!campRecommendVo.getSortBy().equals(sortBy)) {
                campRecommendVo.setSortBy(sortBy);
                campRecommendVo.setPage(1);
                campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
                campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));
                campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+campRecommendVo.getSize()-1)/campRecommendVo.getSize());
            } else if (!campRecommendVo.getSortDirection().equals(sortDirection)) {
                campRecommendVo.setSortDirection(sortDirection);;
                campRecommendVo.setPage(1);
                campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
                campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));
                campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+campRecommendVo.getSize()-1)/campRecommendVo.getSize());
            }
            result.put("campRecommendList", campRecommendVo.getCampRecommendList());
            result.put("totalPages", campRecommendVo.getTotalPages());
            result.put("page", campRecommendVo.getPage());
            result.put("size", campRecommendVo.getSize());
            result.put("pageMaxDisplay", campRecommendVo.getPageMaxDisplay());
        } else {
            result.put("result", "fail");
        }
        return result;
    }

}
