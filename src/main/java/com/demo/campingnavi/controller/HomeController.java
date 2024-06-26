package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CustomOauth2UserDetails;
import com.demo.campingnavi.dto.CustomSecurityUserDetails;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class HomeController {
    @RequestMapping("/")
    public String goindex() {
        return "member/loginPage";
    }

    @RequestMapping("/main")
    public String gomain(Model model) {
        return "mainPage";
    }

    @ResponseBody
    @PostMapping("/get_nickname")
    public Map<String, Object> getNickname(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String nickname = null;
        if (session.getAttribute("loginUser") != null) {
            nickname = ((Member)session.getAttribute("loginUser")).getNickname();
        }
        result.put("nickname", nickname);
        System.out.println(nickname);
        return result;
    }
}
