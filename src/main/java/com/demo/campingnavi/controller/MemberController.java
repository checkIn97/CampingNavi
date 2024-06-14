package com.demo.campingnavi.controller;

import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

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
}
