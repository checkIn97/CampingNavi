package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;
import com.demo.campingnavi.service.CampDetailService;
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

    @GetMapping("/go")
    public String go() {
        return "CampDetail/campDetail";
    }

    @GetMapping("/")
    public String campDetailView(@RequestParam("mapX") String mapX, @RequestParam("mapY") String mapY,@RequestParam("contentId") String contentId, @RequestParam("cseq") int cseq, Model model) {

        List<ApiResponse.Item> itemList = campDetailService.DataFromApi(mapX, mapY);
        List<ApiImageResponse.Item> imageList = campDetailService.DataFromApiImage((contentId));
        model.addAttribute("camps", itemList);
        model.addAttribute("imageUrls", imageList);
        model.addAttribute("cseq", cseq);

        return "CampDetail/campDetail";
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


}
