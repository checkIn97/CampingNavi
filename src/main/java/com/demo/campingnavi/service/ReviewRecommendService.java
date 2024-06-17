package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Review;

import java.util.List;

public interface ReviewRecommendService {
    public int rcdStatus(Member member, Review review);
    public int rcdUpdate(Member member, Review review);
    public int getRcdCountByReview(Review review);
    public List<Review> getRcdReviewListByMember(Member member);
}
