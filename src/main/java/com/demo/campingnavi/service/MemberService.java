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

    public boolean joinProcess(MemberVo vo);

    public boolean loginProcess(MemberVo vo);

    public boolean isUserName(MemberVo vo);

    public boolean isNickName(MemberVo vo);

    public Page<Recommend> getList(int page, Member member);

    public Page<Member> findAll(Pageable pageable);
}
