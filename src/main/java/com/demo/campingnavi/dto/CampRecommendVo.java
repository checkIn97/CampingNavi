package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CampRecommendVo {
    private List<Camp> campList = new ArrayList<>();
    private List<CampVo> campRecommendList = new ArrayList<>();;
    private List<CampVo> campRecommendListVisited = new ArrayList<>();;
    private List<CampVo> campRecommendListAll = new ArrayList<>();;
    private String useyn = "";
    private String[][] searchFieldArray = {{"name", "이름"}, {"locationB", "시도"}, {"locationS", "시군구"}};
    private String searchField = "";
    private String searchWord = "";
    private String[][] campTypeArray = {{"generalCamping", "일반야영장"}, {"autoCamping", "자동차야영장"}, {"glamping", "글램핑"}, {"caravan", "카라반"}};
    private String[] campType = new String[campTypeArray.length];

    private int totalPages = 0;
    private int page = 0;
    private int size = 5;
    private int pageMaxDisplay = 5;
    private String sortBy = "name";
    private String sortDirection = "ASC";
    private Map<String, String> addrMatch = null;

    public CampRecommendVo() {
        addrMatch = new HashMap<String, String>();
        addrMatch.put("경기", "경기도");
        addrMatch.put("경기도", "경기");
        addrMatch.put("강원도", "강원");
        addrMatch.put("강원", "강원도");
        addrMatch.put("충청북도", "충북");
        addrMatch.put("충북", "충청북도");
        addrMatch.put("충청남도", "충남");
        addrMatch.put("충남", "충청남도");
        addrMatch.put("전라북도", "전북");
        addrMatch.put("전북", "전라북도");
        addrMatch.put("전라남도", "전남");
        addrMatch.put("전남", "전라남도");
        addrMatch.put("경상북도", "경북");
        addrMatch.put("경북", "경상북도");
        addrMatch.put("경상남도", "경남");
        addrMatch.put("경남", "경상남도");
        addrMatch.put("", "");
    }

}
