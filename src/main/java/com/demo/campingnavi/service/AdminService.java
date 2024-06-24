package com.demo.campingnavi.service;

public interface AdminService {
    String recommendModelUpdate();
    String campDataUpdate();
    int getCampingTotalCount();
    String getCampingDataFromApi(int page);
    String getCampingDataIntegration(int totalPage);
    int getCrawlingNumber(String update_type);
    String getCrawlingData();
    String getCrawlingDataIntegration();

}
