package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Qna;
import com.demo.campingnavi.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyService {

    public void saveReply(Reply reply);

    public Reply findById(int reply_id);

    public Page<Reply> findAllByQna(int qseq, Pageable pageable);
}
