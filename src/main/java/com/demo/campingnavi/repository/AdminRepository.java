package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}