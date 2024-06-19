package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Integer> {
    public List<UpdateHistory> findByKindOrderByUpdateTimeDesc(String update);
}
