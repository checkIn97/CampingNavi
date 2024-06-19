package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.UpdateHistory;
import com.demo.campingnavi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @ResponseBody
    @GetMapping("/recommend_update")
    public Map<String, String> recommendModelUpdate(@RequestParam("kind") String kind) {
        Map<String, String> result = new HashMap<>();
        if (kind.equals("model")) {
            result.put("result", adminService.recommendModelUpdate());
        } else {
            result.put("result", adminService.campDataUpdate());
        }

        UpdateHistory updateHistory = new UpdateHistory();
        updateHistory.setKind(kind);
        updateHistory.setResult(result.get("result"));
        Date date = new Date();
        updateHistory.setUpdateTime(date);
        adminService.saveUpdateHistory(updateHistory);
        List<UpdateHistory> updateHistoryList = adminService.getUpdateHistoryList(kind);
        System.out.println(kind);
        System.out.println(updateHistoryList.size());
        if (!updateHistoryList.isEmpty()) {
            String updateTime = String.format(updateHistoryList.get(1).getUpdateTime().toString(), "yyyy-MM-dd HH:mm:ss").substring(0, 19);
            result.put("updateTime", updateTime);
        }


        return result;
    }
}
