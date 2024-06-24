package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.UpdateHistory;
import com.demo.campingnavi.repository.jpa.UpdateHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UpdateHistoryRepository updateHistoryRepo;

    @Override
    public String recommendModelUpdate() {
        String pyFile = "ModelTraining.py";
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
        String pyFile = "campingData.py";
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
    public int getCampingTotalCount() {
        String pyFile = "campingTotalCount.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        int result = -1;
        try {
            Process process = processBuilder.start();
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            result = Integer.parseInt(reader.readLine());
            System.out.println(pyFile + " 실행 성공");
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
        }
        return result;
    }

    @Override
    public String getCampingDataFromApi(int page) {
        String pyFile = "campingDataSearch.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, String.valueOf(page));
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            String filename = "tmp_campingData" + page + ".csv";
            filename = PathConfig.realPath(filename);
            File file = new File(filename);
            if (file.exists()) {
                result = "success";
            } else {
                result = "fail";
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }
        return result;
    }

    @Override
    public String getCampingDataIntegration(int totalPage) {
        String pyFile = "campingDataIntegration.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, String.valueOf(totalPage));
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
