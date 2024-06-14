package com.demo.campingnavi.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.ReviewScanVo;

public interface ReviewService {
	
	void insertReview(Review vo);
	
	Review getReview(int vseq);
	
	Page<Review> findReviewList(ReviewScanVo reviewScanVo, int page, int size);

	void editReview(Review vo);
	
	void deleteReview(int vseq);
	
	int updateCnt(int vseq);

	List<Review> getBestReviewList();
	
	List<Review> getAuthorReviewList(int mseq);

	void likePost(int vseq);

	void unlikePost(int vseq);

}
