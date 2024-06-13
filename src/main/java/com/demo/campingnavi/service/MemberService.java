package com.demo.campingnavi.service;

import com.demo.campingnavi.dto.MemberVo;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    public boolean joinProcess(MemberVo vo);

    public boolean loginProcess(MemberVo vo);

    public boolean isUserName(MemberVo vo);

    public boolean isNickName(MemberVo vo);
}
