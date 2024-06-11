package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}