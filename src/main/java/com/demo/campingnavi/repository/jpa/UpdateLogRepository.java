package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.UpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdateLogRepository extends JpaRepository<UpdateLog, Integer> {
    public List<UpdateLog> findByUpdateOrderByUpdateTimeDesc(String update);
}
