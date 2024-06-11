package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampRepository extends JpaRepository<Camp, Integer> {
    public Camp findFirstByOrderByCseqDesc();

    @Query("SELECT camp FROM Camp camp " +
            "WHERE camp.useyn LIKE %:useyn% " +
            "AND camp.name LIKE %:name% " +
            "AND camp.locationB LIKE %:locationB% " +
            "AND camp.locationS LIKE %:locationS% " +
            "INTERSECT " +
            "SELECT camp FROM Camp camp " +
            "WHERE camp.campType LIKE %:campType1% " +
            "OR camp.campType LIKE %:campType2% " +
            "OR camp.campType LIKE %:campType3% " +
            "OR camp.campType LIKE %:campType4% ")
    public List<Camp> getCampList(String useyn, String name, String locationB, String locationS,
                                  String campType1, String campType2, String campType3, String campType4);
}