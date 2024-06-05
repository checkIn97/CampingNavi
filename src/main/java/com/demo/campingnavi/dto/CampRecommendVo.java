package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Camp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CampRecommendVo {
    private List<Camp> campList;
    private List<CampVo> campRecommendList;
}
