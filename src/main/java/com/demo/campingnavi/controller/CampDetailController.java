package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.dto.ReviewVo;
import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;
import com.demo.campingnavi.service.CampDetailService;
import com.demo.campingnavi.service.CampService;
import com.demo.campingnavi.service.ReviewService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/go")
    public String go() {
        return "/campDetail/campDetail";
    }

    @GetMapping("/")
    public String campDetailView(@RequestParam("mapX") String mapX, @RequestParam("mapY") String mapY,
                                 @RequestParam("contentId") String contentId,
                                 @RequestParam("cseq") int cseq,
                                 Model model, HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }

        List<ApiResponse.Item> itemList = campDetailService.DataFromApi(mapX, mapY);
        List<ApiImageResponse.Item> imageList = campDetailService.DataFromApiImage((contentId));
        List<ReviewVo> reviewVoList = reviewService.getReviewVoListByCseq(cseq);

        CampVo campVo = campService.getCampVoByCseq(cseq, member);
        float score = Float.parseFloat(campVo.getScoreView());

        // 사용자가 찜한 상태인지 확인
        int mseq = member.getMseq();
        boolean jjimChecked = campDetailService.isCampJjimmedByUser(mseq, cseq);

        model.addAttribute("camps", itemList);
        model.addAttribute("imageUrls", imageList);
        model.addAttribute("cseq", cseq);
        model.addAttribute("mapX", mapX);
        model.addAttribute("mapY", mapY);
        model.addAttribute("starScore", score);
        model.addAttribute("jjimChecked", jjimChecked);
        model.addAttribute("reviewVoList", reviewVoList);

        return "/campDetail/campDetail";
    }

    @PostMapping("/jjim/{cseq}")
    @ResponseBody
    public ResponseEntity<?> addToJjimlist(@PathVariable("cseq") int cseq, HttpSession session){

        Member member = (Member) session.getAttribute("loginUser");
        if (member == null) {
            // Return a response entity with a message indicating that the user needs to log in
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용해주세요.");
        } else {
            try {
                // Create a new Camp and Recommend object
                Camp camp = new Camp();
                camp.setCseq(cseq);

                Recommend recommend = new Recommend();
                recommend.setCamp(camp);
                recommend.setMember(member);

                // Add to jjim list
                campDetailService.addToJjimlist(recommend);

                // Return success response
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                // Return error response in case of an exception
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜하기에 실패했습니다.");
            }
        }
    }

    @PostMapping("/jjim/delete/{cseq}")
    @ResponseBody
    public ResponseEntity<Void> removeFromJjimlist(@PathVariable("cseq") int cseq, HttpSession session) {

            try {

            Member member = (Member) session.getAttribute("loginUser");
            int mseq = member.getMseq();
            campDetailService.removeFromJjimlist(mseq, cseq);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
