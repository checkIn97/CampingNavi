package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.dto.MemberVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    public Member findById(int mseq);

    public void saveMember(Member member);

    public boolean joinProcess(MemberVo vo);

    public boolean loginProcess(MemberVo vo);

    public boolean isUserName(MemberVo vo);

    public boolean isNickName(MemberVo vo);

    public boolean isEmail(String email);

    public String getUsername(String name, String email, String birth, String phone, String provider);

    public Page<Recommend> getList(int page, Member member);

    public Page<Member> findAll(Pageable pageable);

    public Page<Member> findAllByUsername(String username, Pageable pageable);

    public Page<Member> findAllByName(String name, Pageable pageable);

    public Page<Member> findAllByProvider(String provider, Pageable pageable);

    public Page<Member> findAllByEmail(String eamil, Pageable pageable);
}
