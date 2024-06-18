package com.demo.campingnavi.controller;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println("isNickname = " + isNickname);
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
        System.out.println("마이페이지: " + recommList);
        // 뷰에 전송
        model.addAttribute("member", member);
        model.addAttribute("recommList", recommList);
        model.addAttribute("paging", paging);
        return "member/myPage";
    }

    @GetMapping("/mypage/edit")
    public String mypageEditP(Model model) {
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
        // 뷰에 전송
        model.addAttribute("member", member);
        return "member/editMypage";
    }

    @PostMapping("/mypage/edit/detail")
    public String myPageEdit(MemberVo vo, Model model) {
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
        member.setNickname(vo.getNickname());
        member.setSex(vo.getSex());
        member.setBirth(vo.getBirth());
        member.setPhone(vo.getPhone());
        member.setAddr1(vo.getAddr1());
        member.setAddr2(vo.getAddr2());

        memberRepository.save(member);
        model.addAttribute("member", member);

        return "member/myPage";
    }
}
