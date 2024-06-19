package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.UpdateLog;

import java.util.List;

public interface AdminService {
    public String recommendModelUpdate();
    public String campDataUpdate();
    public void saveUpdateLog(UpdateLog updateLog);
    public List<UpdateLog> getUpdateLogList(String update);
}
