package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Camp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CampVo {
    private Camp camp = null;
    private float score = 0f;
    private String scoreView = "";
    private String homepage = "";
    private String address = "";

    public CampVo(Camp camp) {
        this.camp = camp;
        this.address = camp.getAddr1() + " " + camp.getAddr2();
    }

    public CampVo(Camp camp, float score) {
        new CampVo(camp);
        this.score = score;
        this.scoreView = String.format("%.1f", score);
    }
}
