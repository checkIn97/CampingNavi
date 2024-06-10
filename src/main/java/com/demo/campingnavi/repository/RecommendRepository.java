package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Integer> {
}