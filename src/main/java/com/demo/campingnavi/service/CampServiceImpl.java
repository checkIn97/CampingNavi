package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.repository.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampServiceImpl implements CampService {
    @Autowired
    private CampRepository campRepo;
    @Autowired
    private DataService dataService;

    @Override
    public Camp getCampByCseq(int cseq) {
        return campRepo.findById(cseq).get();
    }

    @Override
    public CampVo getCampVoByCseq(int cseq, Member member) {
        List<Camp> campList = new ArrayList<>();
        campList.add(getCampByCseq(cseq));
        return getCampRecommendList(campList, member).get(0);
    }

    @Override
    public List<Camp> getCampScanList(CampRecommendVo campRecommendVo) {
        String useyn = campRecommendVo.getUseyn();
        if (useyn.equals("a")) {
            useyn = "";
        }

        String searchWord = campRecommendVo.getSearchWord();



        return List.of();
    }

    @Override
    public List<CampVo> getCampRecommendList(List<Camp> filteredList, Member member) {
        dataService.filteredListOutToCsv(filteredList);
        String pyFile = "Recommend.py";
        String csvFile = "tmp_filtered.csv";
        pyFile = PathConfig.realPath(pyFile);
        csvFile = PathConfig.realPath(csvFile);
        List<CampVo> campRecommendList = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, String.valueOf(member.getMseq()));
        try {
            Process process = processBuilder.start();
            System.out.println("파이썬 프로그램 실행 성공!");
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파이썬 프로그램 실행 실패!");
        }

        File file = new File(csvFile);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println(csvFile+"삭제 완료");
            } else {
                System.out.println(csvFile+"삭제 실패");
            }
        }

        String recommendFile = PathConfig.realPath("tmp_recommendList.csv");
        file = new File(recommendFile);
        if (file.exists()) {
            System.out.println("tmp_recommendList.csv 파일 생성 성공");

            // 받아오기 프로세스 입력
            int check = -1;
            int count = 0;
            String text = "";

            try {
                FileReader fr = new FileReader(recommendFile);
                BufferedReader br = new BufferedReader(fr);

                while(true) {
                    text = br.readLine();
                    if (text == null)
                        break;

                    if (check != -1) {
                        String[] input = text.split(",");
                        Camp camp = getCampByCseq(Integer.parseInt(input[0]));
                        CampVo campVo = new CampVo(camp, Float.parseFloat(input[1]));
                        // campVo에 추가로 데이터 입력할 것

                        if (!campRecommendList.contains(campVo)) {
                            campRecommendList.add(campVo);
                        }
                        text = "";
                    } else {
                        check = 0;
                        text = "";
                    }
                }

                br.close();
                fr.close();

                file = new File(recommendFile);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("tmp_recommendList.csv 삭제 완료");
                    } else {
                        System.out.println("tmp_recommendList.csv 삭제 실패");
                    }
                }

            } catch (IOException e) {
                System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
                e.printStackTrace();
            }
        } else {
            System.out.println("tmp_recommendList.csv 파일 생성 실패");
        }

        return campRecommendList;
    }


}
