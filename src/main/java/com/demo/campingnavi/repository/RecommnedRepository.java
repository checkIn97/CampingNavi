package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Recommned;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommnedRepository extends JpaRepository<Recommned, Integer> {
}