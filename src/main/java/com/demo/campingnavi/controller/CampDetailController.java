package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;
import com.demo.campingnavi.service.CampDetailService;
import com.demo.campingnavi.service.CampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/camp/detail")
public class CampDetailController {

    @Autowired
    private CampDetailService campDetailService;
    @Autowired
    private CampService campService;

    @GetMapping("/go")
    public String go() {
        return "/campDetail/campDetail";
    }

    @GetMapping("/")
    public String campDetailView(@RequestParam("mapX") String mapX, @RequestParam("mapY") String mapY,@RequestParam("contentId") String contentId, @RequestParam("cseq") int cseq, Model model) {

        List<ApiResponse.Item> itemList = campDetailService.DataFromApi(mapX, mapY);
        List<ApiImageResponse.Item> imageList = campDetailService.DataFromApiImage((contentId));

        Member member = new Member();

        CampVo campVo = campService.getCampVoByCseq(cseq, member);
        float score = Float.parseFloat(campVo.getScoreView());

        // 사용자가 찜한 상태인지 확인
        int mseq = 1; // 예: 현재 사용자의 ID (로그인 사용자의 경우 세션에서 가져옴)
        boolean jjimChecked = campDetailService.isCampJjimmedByUser(mseq, cseq);

        System.out.println("jjimChecked = " + jjimChecked);

        model.addAttribute("camps", itemList);
        model.addAttribute("imageUrls", imageList);
        model.addAttribute("cseq", cseq);
        model.addAttribute("mapX", mapX);
        model.addAttribute("mapY", mapY);
        model.addAttribute("starScore", score);
        model.addAttribute("jjimChecked", jjimChecked);

        return "/campDetail/campDetail";
    }

    @PostMapping("/jjim/{cseq}")
    @ResponseBody
    public ResponseEntity<?> addToJjimlist(@PathVariable("cseq") int cseq){

        // 사용자 mseq와 cseq를 사용하여 찜하기 기능 구현
        try {
            Member member = new Member();
            member.setMseq(1);
            Camp camp = new Camp();
            camp.setCseq(cseq);

            Recommend recommend = new Recommend();
            recommend.setCamp(camp);
            recommend.setMember(member);
            campDetailService.addToJjimlist(recommend);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜하기에 실패했습니다.");
        }
    }
    @PostMapping("/jjim/delete/{cseq}")
    @ResponseBody
    public ResponseEntity<Void> removeFromJjimlist(@PathVariable("cseq") int cseq) {
        try {
            // 실제 사용자 ID를 세션에서 가져와서 설정
            int mseq = 1; // 예: 세션에서 사용자 ID를 가져옴

            campDetailService.removeFromJjimlist(mseq, cseq);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
