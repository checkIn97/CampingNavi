package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.repository.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private CampRepository campRepo;

    @Override
    public List<Camp> campInFromCsv(String csvFile, String n) {
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
                            camp.setContentId(Integer.parseInt(input[0]));
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

}
