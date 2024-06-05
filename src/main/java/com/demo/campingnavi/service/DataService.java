package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;

import java.util.List;

public interface DataService {
    public List<Camp> campInFromCsv(String csvFile, String n);
}
