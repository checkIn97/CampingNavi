package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Role;
import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean joinProcess(MemberVo vo) {
        boolean result = false;
        String PATTERN_ID = "^[a-z]{1}[a-z0-9]{5,10}+$";
        String PATTERN_PW = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}+$";
        boolean idPattern = Pattern.matches(PATTERN_ID, vo.getUsername());
        boolean pwPattern = Pattern.matches(PATTERN_PW, vo.getPw());

        // 존재여부확인
        boolean isMember = memberRepository.existsByUsername(vo.getUsername());

        if(/*idPattern && pwPattern && */!isMember){
            String encodedPassword = passwordEncoder.encode(vo.getPw());
            Member member = Member.builder()
                    .username(vo.getUsername())
                    .pw(encodedPassword)
                    .name(vo.getName())
                    .sex(vo.getSex())
                    .email(vo.getEmail())
                    .addr1(vo.getAddr1())
                    .phone(vo.getPhone())
                    .nickname(vo.getNickname())
                    .birth(vo.getBirth())
                    .useyn("y")
                    .role(Role.USER.getKey())
                    .build();
            memberRepository.save(member);
            result = true;
            return result;
        } else {
            return result;
        }
    }

    @Override
    public boolean loginProcess(MemberVo vo) {
        boolean isMember = memberRepository.existsByUsername(vo.getUsername());

        if(isMember){
            Member member = memberRepository.findByUsername(vo.getUsername());
            boolean checkPw = member.getPw().equals(vo.getPw());

            if(checkPw){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUserName(MemberVo vo) {

        return memberRepository.existsByUsername(vo.getUsername());
    }

    @Override
    public boolean isNickName(MemberVo vo) {

        return memberRepository.existsByNickname(vo.getNickname());
    }
}
