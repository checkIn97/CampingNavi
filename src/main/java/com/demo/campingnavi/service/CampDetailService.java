package com.demo.campingnavi.service;

import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;

import java.util.List;

public interface CampDetailService {
    List<ApiResponse.Item> DataFromApi(String mapX, String mapY);

    List<ApiImageResponse.Item> DataFromApiImage(String contendId);
}
