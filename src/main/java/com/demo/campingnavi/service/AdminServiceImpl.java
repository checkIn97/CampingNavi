package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.UpdateHistory;
import com.demo.campingnavi.repository.jpa.UpdateHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UpdateHistoryRepository updateHistoryRepo;

    @Override
    public String recommendModelUpdate() {
        String pyFile = "ModelTraining";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            result = "success";
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }

        return result;
    }

    @Override
    public String campDataUpdate() {
        String pyFile = "campData.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            result = "success";
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }
        return result;
    }

    @Override
    public void saveUpdateHistory(UpdateHistory updateHistory) {
        updateHistoryRepo.save(updateHistory);
    }

    @Override
    public List<UpdateHistory> getUpdateHistoryList(String kind) {
        return updateHistoryRepo.findByKindOrderByUpdateTimeDesc(kind);
    }

}
