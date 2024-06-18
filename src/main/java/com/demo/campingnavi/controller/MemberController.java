package com.demo.campingnavi.controller;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.dto.CustomOauth2UserDetails;
import com.demo.campingnavi.dto.CustomSecurityUserDetails;
import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.repository.jpa.RecommendRepository;
import com.demo.campingnavi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RecommendRepository recommendRepository;

    @GetMapping("/login")
    public String loginP(Model model) {
        return "member/loginPage";
    }

//    @PostMapping("/loginProc")
//    public String loginProcess(MemberVo vo) {
//        boolean login = memberService.loginProcess(vo);
//        if (login) {
//            System.out.println("로그인 성공");
//            return "search/searchPage";
//        } else {
//            System.out.println("로그인 실패");
//            return "member/loginPage";
//        }
//    }

    @GetMapping("/join")
    public String joinP() {
        return "member/membership";
    }

    @PostMapping("/joinProc")
    public String joinProcess(MemberVo vo, Model model) {

        boolean result = memberService.joinProcess(vo);
        if(result) {
            return "redirect:login";
        } else {
            return "redirect:joinAlertView";
        }
    }

    @GetMapping("/joinAlertView")
    public String joinAlertView() {
        return "member/joinAlert";
    }

    @PostMapping("/validateUser")
    @ResponseBody
    public Map<String, Object> validateUser(@RequestParam(value = "username") String username) {
        Map<String, Object> result = new HashMap<>();
        boolean isUserName = memberRepository.existsByUsername(username);
        if (!isUserName) {
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/validateNickname")
    @ResponseBody
    public Map<String, Object> validateNickname(@RequestParam(value = "nickname") String nickname) {
        Map<String, Object> result = new HashMap<>();
        boolean isNickname = memberRepository.existsByNickname(nickname);
        if (!isNickname) {
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @RequestMapping("/membershipAgree")
    public String membershipAgree() {
        return "member/membershipAgree";
    }

    @GetMapping("/mypage")
    public String mypageP(Model model, @RequestParam(defaultValue = "0") int page) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }

        // 추출된 아이디로 회원 객체 생성
        Member member = memberRepository.findByUsername(username);
        // 찜목록 객체 생성
        Page<Recommend> paging = this.memberService.getList(page, member);
        List<Recommend> recommList = recommendRepository.getAllListByMember(member.getMseq());
        // 뷰에 전송
        model.addAttribute("member", member);
        model.addAttribute("recommList", recommList);
        model.addAttribute("paging", paging);
        model.addAttribute("img", member.getImg());
        return "member/myPage";
    }

    @GetMapping("/mypage/oauth")
    public String oauthMypageP(Model model, @RequestParam(defaultValue = "0") int page) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }

        // 추출된 아이디로 회원 객체 생성
        Member member = memberRepository.findByUsername(username);
        // 찜목록 객체 생성
        Page<Recommend> paging = this.memberService.getList(page, member);
        List<Recommend> recommList = recommendRepository.getAllListByMember(member.getMseq());
        // 뷰에 전송
        model.addAttribute("member", member);
        model.addAttribute("recommList", recommList);
        model.addAttribute("paging", paging);
        return "member/myPageOAuth";
    }

    @PostMapping("/mypage/edit/detail")
    @ResponseBody
    public Map<String, Object> myPageEdit(Model model,
                                          @RequestParam("nickname") String nickname,
                                          @RequestParam("sex") String sex,
                                          @RequestParam("birth") String birth,
                                          @RequestParam("phone") String phone,
                                          @RequestParam("addr1") String addr1,
                                          @RequestParam("addr2") String addr2,
                                          @RequestParam("img") String img) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }
        // 추출된 아이디로 회원 객체 생성
        Map<String, Object> data = new HashMap<>();
        Member member = memberRepository.findByUsername(username);
        member.setImg(img);
        member.setNickname(nickname);
        member.setSex(sex);
        member.setBirth(birth);
        member.setPhone(phone);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        memberRepository.save(member);

        data.put("nickname", nickname);
        data.put("sex", sex);
        data.put("birth", birth);
        data.put("phone", phone);
        data.put("addr1", addr1);
        data.put("addr2", addr2);
        data.put("img", img);
        return data;
    }
}
