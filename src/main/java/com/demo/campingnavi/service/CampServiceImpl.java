package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.repository.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CampServiceImpl implements CampService {
    @Autowired
    private CampRepository campRepo;
    @Autowired
    private DataService dataService;

    @Override
    public Camp getCampByCseq(int cseq) {
        return campRepo.findById(cseq).get();
    }

    @Override
    public CampVo getCampVoByCseq(int cseq, Member member) {
        List<Camp> campList = new ArrayList<>();
        campList.add(getCampByCseq(cseq));
        dataService.filteredListOutToCsv(campList);
        return getCampRecommendList(member).get(0);
    }

    @Override
    public List<Camp> getCampScanList(CampRecommendVo campRecommendVo) {

        return List.of();
    }

    @Override
    public List<CampVo> getCampRecommendList(Member member) {

        return List.of();
    }


}
