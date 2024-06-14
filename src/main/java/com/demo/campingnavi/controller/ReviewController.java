package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.dto.ReviewScanVo;
import com.demo.campingnavi.service.ReviewCommentService;
import com.demo.campingnavi.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewCommentService reviewCommentService;


    //게시글 작성으로 이동
    @GetMapping("/review_insert_form")
    public String showWriteForm(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
       // MemberVo memberVo = new MemberVo();
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        } else {
           // model.addAttribute("memberVo", memberVo);
            return "review/reviewInsert"; //게시글 작성페이지로 이동.
        }

    }

    // 게시글 작성
    @PostMapping("/review_insert")
    public String saveReview(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            HttpSession session,
                            HttpServletRequest request,
                            Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        MemberVo memberVo = new MemberVo();
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        Review vo = new Review();

        if (title.isEmpty()) {
            vo.setTitle("제목 없음");
        }else {
            vo.setTitle(title);
        }
        vo.setContent(content);
        vo.setMember(member); // 사용자 정보 설정
        model.addAttribute("memberVo", memberVo);
        reviewService.insertReview(vo);

        return "redirect:/review_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }


    // 게시글 리스트 보기
    @GetMapping("/review_list")
    public String showReviewList(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                @RequestParam(value = "sortBy", defaultValue = "vseq") String sortBy,
                                @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
                                @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
                                @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                ReviewScanVo reviewScanVo,
                                HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        if (page == 0) {
            page = 1;
            reviewScanVo = new ReviewScanVo(); // 새로운 객체로 초기화
            reviewScanVo.setSearchField(searchField);
            reviewScanVo.setSearchWord(searchWord);
            reviewScanVo.setSortBy(sortBy);
            reviewScanVo.setSortDirection(sortDirection);
            reviewScanVo.setPageMaxDisplay(pageMaxDisplay);

        } else {
            reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
        }
        Page<Review> reviewData = reviewService.findReviewList(reviewScanVo, page, size);

        reviewScanVo.setPageInfo(reviewData);
        reviewScanVo.setReviewList(reviewData.getContent());

        session.setAttribute("reviewScanVo", reviewScanVo);
        model.addAttribute("reviewScanVo", reviewScanVo);
        model.addAttribute("reviewList", reviewScanVo.getReviewList());
        model.addAttribute("pageInfo", reviewScanVo.getPageInfo());
        model.addAttribute("reviewBestList", reviewService.getBestReviewList());
        // model.addAttribute("memberVo", memberVo);
        return "review/reviewList";
    }


    // 게시글 검색 보기
    @GetMapping("/review_list_search")
    public String searchReviewList(Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @RequestParam(value = "sortBy", defaultValue = "vseq") String sortBy,
                                  @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
                                  @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
                                  @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                  @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                  ReviewScanVo reviewScanVo,
                                  HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        if (page == 0) {
            page = 1;
            reviewScanVo = new ReviewScanVo(); // 새로운 객체로 초기화
            reviewScanVo.setSearchField(searchField);
            reviewScanVo.setSearchWord(searchWord);
            reviewScanVo.setSortBy(sortBy);
            reviewScanVo.setSortDirection(sortDirection);
            reviewScanVo.setPageMaxDisplay(pageMaxDisplay);


        } else {
            reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");

        }

        Page<Review> reviewData = reviewService.findReviewList(reviewScanVo, page, size);

        if (reviewData.isEmpty()) {
            model.addAttribute("msg", "검색 결과가 없습니다.");
            model.addAttribute("redirectTo", "/review_list");
            return "review/review_alert";
        } else {

            reviewScanVo.setPageInfo(reviewData);
            reviewScanVo.setReviewList(reviewData.getContent());
            session.setAttribute("reviewScanVo", reviewScanVo);
            model.addAttribute("reviewScanVo", reviewScanVo);
            model.addAttribute("reviewList", reviewScanVo.getReviewList());
            model.addAttribute("pageInfo", reviewScanVo.getPageInfo());
            model.addAttribute("reviewBestList", reviewService.getBestReviewList());
            // model.addAttribute("memberVo", memberVo);

            return "review/reviewList";
        }
    }



    // 게시글 상세보기
    @GetMapping("/review_detail/{vseq}")
    public String reviewDetail(@PathVariable("vseq") int vseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Review review = reviewService.getReview(vseq);
        reviewService.updateCnt(vseq);
        int useq = review.getMember().getMseq();
        // 모델에 게시글 추가
        model.addAttribute("review", review);
        model.addAttribute("authorList", reviewService.getAuthorReviewList(useq));

        // 게시글의 작성자와 현재 사용자가 같은지 확인하여 모델에 추가
        model.addAttribute("isAuthor", review.getMember().getMseq() == member.getMseq());

        ReviewScanVo reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
        model.addAttribute("reviewScanVo", reviewScanVo);
        model.addAttribute("reviewList", reviewScanVo.getReviewList());
        model.addAttribute("pageInfo", reviewScanVo.getPageInfo());
        // model.addAttribute("memberVo", memberVo);
        // 게시글 상세보기 페이지로 이동
        return "review/reviewDetail";
    }


    // 게시글 삭제하기
    @PostMapping("/review_delete/{vseq}")
    public String reviewDelete(@PathVariable("vseq") int vseq, HttpSession session, HttpServletRequest request,
                              Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }
        // model.addAttribute("memberVo", memberVo);
        reviewCommentService.deletAllComment(vseq);
        reviewService.deleteReview(vseq);

        return "redirect:/review_list";

    }


    // 게시글 수정화면으로 이동하기
    @GetMapping("/review_edit_form/{vseq}")
    public String reviewEditGo(@PathVariable("vseq") int vseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Review review = reviewService.getReview(vseq);
        // 모델에 게시글 추가
        // model.addAttribute("memberVo", memberVo);
        model.addAttribute("review", review);
        // 게시글 수정화면으로 이동
        return "review/reviewEdit";
    }

    //게시글 수정하기
    @PostMapping("/review_edit")
    public String reviewEdit(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("vseq") int vseq,
                            @RequestParam("likes") int likes,
                            @RequestParam("cnt") int cnt,
                            HttpSession session, HttpServletRequest request, Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        Review vo = new Review();
        vo.setVseq(vseq);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setMember(member);
        vo.setLikes(likes);
        vo.setCount(cnt);
        // model.addAttribute("memberVo", memberVo);

        reviewService.editReview(vo);
        reviewCommentService.updateCommentCount(vseq);
        return "redirect:/review_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }

    @GetMapping("/review_memberList/{mseq}")
    public String showReviewList(Model model,
                                @PathVariable(value = "mseq") int mseq,
                                HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginMember");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "review/review_alert";
        }

        model.addAttribute("authorList", reviewService.getAuthorReviewList(mseq));
        model.addAttribute("commentList", reviewCommentService.getCommentMemberList(mseq));
        // model.addAttribute("memberVo", memberVo);
        return "reviewMemberList";
    }


    @PostMapping("review_like/{vseq}")
    @ResponseBody
    public ResponseEntity<String> likePost(@PathVariable("vseq") int vseq) {
        reviewService.likePost(vseq);
        return ResponseEntity.ok("Liked");
    }

    @PostMapping("review_unlike/{vseq}")
    @ResponseBody
    public ResponseEntity<String> unlikePost(@PathVariable("vseq") int vseq){
        reviewService.unlikePost(vseq);
        return ResponseEntity.ok("Liked");
    }
}
