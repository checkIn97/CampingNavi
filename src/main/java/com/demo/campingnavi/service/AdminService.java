package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.UpdateHistory;

import java.util.List;

public interface AdminService {
    public String recommendModelUpdate();
    public String campDataUpdate();
    public void saveUpdateHistory(UpdateHistory updateHistory);
    public List<UpdateHistory> getUpdateHistoryList(String kind);
}
