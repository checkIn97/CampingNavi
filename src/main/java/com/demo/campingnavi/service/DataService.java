package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Review;

import java.util.List;

public interface DataService {
    List<Camp> campInFromCsv(String csvFile, String n);
    void reviewListOutToCsv(List<Review> reviewList);
    void campListOutToCsv(List<Camp> campList, String csvFile, String pyFile);
    boolean checkFileExist(String file_name);
    String deleteFile(String filename);
    String createFile(String filename);
    String createDir(String dir);
}
