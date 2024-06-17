package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewVo {
    private Review review;
    private int rcdCount;

    public ReviewVo(Review review, int rcdCount) {
        this.review = review;
        this.rcdCount = rcdCount;
    }

}
