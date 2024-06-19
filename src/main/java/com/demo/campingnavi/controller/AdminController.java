package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.UpdateLog;
import com.demo.campingnavi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @ResponseBody
    @PostMapping("/recommend_update")
    public Map<String, String> recommendModelUpdate(@RequestParam("update") String update) {
        Map<String, String> result = new HashMap<>();
        if (update.equals("model")) {
            result.put("result", adminService.recommendModelUpdate());
        } else {
            result.put("result", adminService.campDataUpdate());
        }

        UpdateLog updateLog = new UpdateLog();
        updateLog.setUpdate(update);
        updateLog.setResult(result.get("result"));
        adminService.saveUpdateLog(updateLog);
        String updateTime = "없음";
        List<UpdateLog> updateLogList = adminService.getUpdateLogList(update);
        if (!updateLogList.isEmpty()) {
            updateTime = String.format(updateLogList.get(0).getUpdateTime().toString(), "yyyy-MM-dd HH:mm:ss").substring(0, 19);
        }
        result.put("updateTime", updateTime);

        return result;
    }
}
