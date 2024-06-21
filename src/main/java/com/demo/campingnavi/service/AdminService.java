package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.UpdateHistory;

import java.util.List;

public interface AdminService {
    String recommendModelUpdate();
    String campDataUpdate();
    int getCampingTotalCount();
    String getCampingDataFromApi(int page);
    String getCampingDataIntegration(int totalPage);
    void saveUpdateHistory(UpdateHistory updateHistory);
    List<UpdateHistory> getUpdateHistoryList(String kind);
}
