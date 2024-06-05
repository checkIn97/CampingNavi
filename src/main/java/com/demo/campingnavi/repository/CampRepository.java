package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Camp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampRepository extends JpaRepository<Camp, Integer> {
    public Camp findFirstByOrderByCseqDesc();
}