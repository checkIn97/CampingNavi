package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Integer> {
}