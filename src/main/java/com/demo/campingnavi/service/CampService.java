package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;

import java.util.List;

public interface CampService {
    public Camp getCampByCseq(int cseq);
    public CampVo getCampVoByCseq(int cseq, Member member);
    public List<Camp> getCampScanList(CampRecommendVo campRecommendVo);
    public void saveCampRecommendList(List<Camp> filteredList, Member member, CampRecommendVo campRecommendVo);

    List<Camp> searchCamps(String keyword);
    public Camp getCampByName(String campName);
}
