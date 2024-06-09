package com.demo.campingnavi.repository;

import com.demo.campingnavi.domain.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampRepository extends JpaRepository<Camp, Integer> {
    public Camp findFirstByOrderByCseqDesc();

    @Query("SELECT camp FROM Camp camp " +
            "WHERE camp.useyn LIKE %:useyn% " +
            "AND camp.name LIKE %:name% " +
            "AND camp.campType LIKE %:campType% " +
            "AND camp.addr1 LIKE %:addrL% " +
            "OR camp.addr1 LIKE %:addrS%")
    public List<Camp> getCampList(String useyn, String name, String campType, String addrL, String addrS);
}