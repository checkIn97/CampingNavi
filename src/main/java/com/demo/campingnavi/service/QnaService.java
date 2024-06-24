package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaService {

    public void saveQna(Qna qna);

    public Qna findById(int qseq);

    public Page<Qna> findAllByType(String type, Pageable pageable);

    public Page<Qna> findAllByTypeExceptNone(String type, Pageable pageable);

    public Page<Qna> findAllByMember(Member member, Pageable pageable);
}
