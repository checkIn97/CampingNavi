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
    private String addressFull = "";
    private String addressShort = "";

    public CampVo(Camp camp) {
        this.camp = camp;
        this.addressFull = camp.getAddr1() + " " + camp.getAddr2();
        this.addressShort = camp.getLocationB() + " " + camp.getLocationS();
    }

    public CampVo(Camp camp, float score) {
        new CampVo(camp);
        this.score = score;
        this.scoreView = String.format("%.1f", score);
    }
}
