package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommnedRepository extends JpaRepository<Recommend, Integer> {
}