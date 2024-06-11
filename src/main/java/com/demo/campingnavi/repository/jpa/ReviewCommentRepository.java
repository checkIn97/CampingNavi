package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {
}