package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Review;

import java.util.List;

public interface DataService {
    public List<Camp> campInFromCsv(String csvFile, String n);
    public void reviewListOutToCsv(List<Review> reviewList);
    public void campListOutToCsv(List<Camp> campList, String csvFile, String pyFile);
}
