package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

    Qna findById(int qseq);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1 AND q.useyn = 'y'")
    Page<Qna> findAllByType(String type, Pageable pageable);
}