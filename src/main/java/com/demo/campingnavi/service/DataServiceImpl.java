package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.repository.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private CampRepository campRepo;

    @Override
    public List<Camp> campInFromCsv(String csvFile, String n) {
        // CSV 파일에서 camp 데이터를 읽어 데이터베이스에 저장하는 메소드
        List<Camp> camps = new ArrayList<Camp>();
        List<String> errors = new ArrayList<String>();

        int num = -1;
        int check = -1;
        int count = 0;
        String text = "";

        if (!n.equals("all")) {
            try {
                num = Integer.parseInt(n);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

        if (num >= 0 || n.equals("all")) {
            try {
                FileReader fr = new FileReader(csvFile);
                BufferedReader br = new BufferedReader(fr);

                while(true) {
                    if (!n.equals("all")) {
                        if (count == num) {
                            break;
                        }
                    }

                    text = br.readLine();

                    if (text == null)
                        break;

                    if (check != -1) {
                        String[] input = text.split(",");
                        Camp camp = new Camp();

                        try {
                            camp.setContentId(input[0]);
                            camp.setName(input[1].isEmpty() ? "" : input[1]);
                            camp.setCreatedAt(input[2].isEmpty() ? null : LocalDate.parse(input[2]));
                            camp.setAddr1(input[3].isEmpty() ? "" : input[3]);
                            camp.setAddr2(input[4].isEmpty() ? "" : input[4]);
                            camp.setMapY(input[5]);
                            camp.setMapX(input[6]);
                            camp.setCampType(input[7]);
                            camp.setUseyn("y");
                            campRepo.save(camp);
                            camp = campRepo.findFirstByOrderByCseqDesc();
                            camps.add(camp);
                            count++;
                        } catch(Exception e) {
                            e.printStackTrace();
                            String tmp = "";
                            for (int i = 0 ; i < input.length ; i++) {
                                tmp += i+" : "+input[i]+"\n";
                            }
                            tmp += "\n";
                            errors.add(tmp);
                        }
                        text = "";
                    } else {
                        check = 0;
                        text = "";
                    }
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
                e.printStackTrace();
            }
        } else {
            System.out.println("데이터 입력 실패!");
        }

        System.out.println(camps.get(0).getName());
        return camps;
    }

    @Override
    public void reviewListOutToCsv(List<Review> reviewList) {
        // 리뷰가 작성될 때 Review.csv 파일로 평점정보를 내보내는 메소드
        String csvFile = "tmp_review.csv";
        String pyFile = "ReviewListToCsv.py";
        csvFile = PathConfig.realPath(csvFile);
        pyFile = PathConfig.realPath(pyFile);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("mseq,")
                .append("cseq,")
                .append("rate\n");

        for (Review review : reviewList) {
            stringBuilder
                    .append(review.getMember().getMseq()).append(",")
                    .append(review.getCamp().getCseq()).append(",")
                    .append(review.getLikes()).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(stringBuilder.toString());
            }
            fileWriter.close();
            ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, csvFile);
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("review 데이터 내보내기 성공");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("review 데이터 내보내기 실패");
        }

        File file = new File(csvFile);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("임시파일 삭제 완료");
            } else {
                System.out.println("임시파일 삭제 실패");
            }
        }
    }

    @Override
    public void filteredListOutToCsv(List<Camp> filteredList) {
        // 평점정보를 가져올 캠프 리스트를 내보내는 메소드
        String csvFile = "tmp_filtered.csv";
        csvFile = PathConfig.realPath(csvFile);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("cseq,")
                .append("mapX,")
                .append("mapY\n");

        for (Camp camp : filteredList) {
            stringBuilder
                    .append(String.valueOf(camp.getCseq())).append(",")
                    .append(camp.getMapX()).append(",")
                    .append(camp.getMapY()).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(stringBuilder.toString());
            }
            fileWriter.close();
            System.out.println("filtered 캠프 데이터 내보내기 성공");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("filtered 캠프 데이터 내보내기 실패");
        }
    }

}
