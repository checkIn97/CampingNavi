package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Qna;
import com.demo.campingnavi.repository.jpa.QnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QnaServiceImpl implements QnaService{

    @Autowired
    QnaRepository qnaRepository;

    @Override
    public void saveQna(Qna qna) {
        qnaRepository.save(qna);
    }

    @Override
    public Qna findById(int qseq) {
        return qnaRepository.findById(qseq);
    }

    @Override
    public Page<Qna> findAllByType(String type, Pageable pageable) {
        return qnaRepository.findAllByType(type, pageable);
    }
}
