package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.UpdateLog;
import com.demo.campingnavi.repository.jpa.UpdateLogRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UpdateLogRepository updateLogRepo;

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
    public void saveUpdateLog(UpdateLog updateLog) {
        updateLogRepo.save(updateLog);
    }

    @Override
    public List<UpdateLog> getUpdateLogList(String update) {
        return updateLogRepo.findByUpdateOrderByUpdateTimeDesc(update);
    }

}
