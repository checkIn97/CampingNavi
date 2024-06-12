package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.service.CampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

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

        campRecommendVo.setPage(1);
        campRecommendVo.setSize(size);
        campRecommendVo.setSortBy(sortBy);
        campRecommendVo.setSortDirection(sortDirection);
        campRecommendVo.setPageMaxDisplay(pageMaxDisplay);
        campRecommendVo.setUseyn(useyn);
        campRecommendVo.setSearchField(searchField);
        campRecommendVo.setSearchWord(searchWord);

        List<String> campTypeList = Arrays.asList(campType);
        campType = new String[campRecommendVo.getCampTypeArray().length];
        for (int i = 0 ; i < campRecommendVo.getCampTypeArray().length ; i++) {
            String type = campRecommendVo.getCampTypeArray()[i][0];
            if (campTypeList.contains(type)) {
                campType[i] = type;
            } else {
                campType[i] = "";
            }
        }
        campRecommendVo.setCampType(campType);

        campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
        campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));
        session.setAttribute("campRecommendVo", campRecommendVo);
        model.addAttribute("campRecommendVo", campRecommendVo);

        return "search/searchPage";
    }

    @PostMapping("/reloadList")
    @ResponseBody
    public Map<String, Object> reloadList(HttpSession session,
                                          @RequestParam(value="page") int page,
                                          @RequestParam(value="sortBy") String sortBy,
                                          @RequestParam(value="sortDirection") String sortDirection) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginMember");
        if (member != null) {
            CampRecommendVo campRecommendVo = (CampRecommendVo) session.getAttribute("campRecommendVo");
            if (campRecommendVo.getPage() != page) {
                campRecommendVo.setPage(page);
                campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+ campRecommendVo.getSize()-1)/ campRecommendVo.getSize());
            } else if (!campRecommendVo.getSortBy().equals(sortBy)) {
                campRecommendVo.setSortBy(sortBy);
                campRecommendVo.setPage(1);
                campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
                campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));
                campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+ campRecommendVo.getSize()-1)/ campRecommendVo.getSize());
            } else if (!campRecommendVo.getSortDirection().equals(sortDirection)) {
                campRecommendVo.setSortDirection(sortDirection);;
                campRecommendVo.setPage(1);
                campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
                campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));
                campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+ campRecommendVo.getSize()-1)/ campRecommendVo.getSize());
            }
            
            // 테스트용 임시 데이터
            List<CampVo> campRecommendList = campRecommendVo.getCampRecommendList();
            if (campRecommendList.size() == 0) {
                for (int i = 0 ; i < 100 ; i++) {
                    campRecommendList.add(new CampVo(campService.getCampByCseq(i+1), ((int)(Math.random()*10)+1)/2f));
                }
                campRecommendVo.setCampRecommendList(campRecommendList);
                campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+ campRecommendVo.getSize()-1)/ campRecommendVo.getSize());
            }


            result.put("campRecommendList", campRecommendVo.getCampRecommendList());
            result.put("totalPages", campRecommendVo.getTotalPages());
            result.put("page", campRecommendVo.getPage());
            result.put("size", campRecommendVo.getSize());
            result.put("pageMaxDisplay", campRecommendVo.getPageMaxDisplay());
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/re_search")
    @ResponseBody
    public Map<String, Object> re_search(HttpSession session,
                                         @RequestParam(value="searchField") String searchField,
                                         @RequestParam(value="searchWord") String searchWord,
                                         @RequestParam(value="campType") String[] campType) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginMember");
        if (member != null) {
            CampRecommendVo campRecommendVo = new CampRecommendVo();
            campRecommendVo.setPage(1);
            campRecommendVo.setSortBy(((CampRecommendVo)session.getAttribute("campRecommendVo")).getSortBy());
            campRecommendVo.setSortDirection(((CampRecommendVo)session.getAttribute("campRecommendVo")).getSortDirection());
            campRecommendVo.setSize(((CampRecommendVo)session.getAttribute("campRecommendVo")).getSize());
            campRecommendVo.setPageMaxDisplay(((CampRecommendVo)session.getAttribute("campRecommendVo")).getPageMaxDisplay());
            campRecommendVo.setUseyn(((CampRecommendVo)session.getAttribute("campRecommendVo")).getUseyn());
            campRecommendVo.setSearchField(searchField);
            campRecommendVo.setSearchWord(searchWord);

            List<String> campTypeList = Arrays.asList(campType);
            campType = new String[campRecommendVo.getCampTypeArray().length];
            for (int i = 0 ; i < campRecommendVo.getCampTypeArray().length ; i++) {
                String type = campRecommendVo.getCampTypeArray()[i][0];
                if (campTypeList.contains(type)) {
                    campType[i] = type;
                } else {
                    campType[i] = "";
                }
            }
            campRecommendVo.setCampType(campType);

            campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
            campRecommendVo.setCampRecommendList(campService.getCampRecommendList(campRecommendVo.getCampList(), member));

            session.setAttribute("campRecommendVo", campRecommendVo);

            result.put("campRecommendVo", campRecommendVo);
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

}
