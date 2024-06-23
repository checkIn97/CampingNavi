package com.demo.campingnavi.controller;

import com.demo.campingnavi.service.MailService;
import com.demo.campingnavi.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private int number;
    private final MemberService memberService;

    // 인증 이메일 전송
    @PostMapping ("/mailSend")
    public HashMap<String, Object> mailSend(String mail, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        if (!memberService.isEmail(mail)) {
            try {
                number = mailService.sendMail(mail);
                String num = String.valueOf(number);
                System.out.println("num: " + num);

                map.put("success", Boolean.TRUE);
                map.put("result", "인증번호 전송 성공");
                map.put("number", num);
                session.setAttribute("number", num);
            } catch (Exception e) {
                map.put("success", Boolean.FALSE);
                map.put("result", "인증번호 전송 실패");
            }
        } else {
            map.put("success", Boolean.FALSE);
            map.put("result", "이미 존재하는 메일입니다.");
        }


        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping ("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber, HttpSession session) {
        String strNumber = (String) session.getAttribute("number");
        number = Integer.parseInt(strNumber);
        boolean isMatch = userNumber.equals(String.valueOf(number));
        System.out.println("userNumber: " + userNumber);
        System.out.println("isMatch: " + isMatch);
        return ResponseEntity.ok(isMatch);
    }
}
