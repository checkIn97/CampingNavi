package com.demo.campingnavi.service;

import java.util.List;

import com.demo.campingnavi.dto.ReviewVo;
import org.springframework.data.domain.Page;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.ReviewScanVo;

public interface ReviewService {
	
	void insertReview(Review vo);
	
	Review getReview(int vseq);
	
	List<ReviewVo> findReviewVoList(ReviewScanVo reviewScanVo);

	void editReview(Review vo);
	
	void deleteReview(int vseq);
	
	int updateCnt(int vseq);

	List<ReviewVo> getBestReviewVoList();
	
	List<ReviewVo> getAuthorReviewVoList(int mseq);

	void likePost(int vseq);

	void unlikePost(int vseq);

	List<ReviewVo> getReviewVoListByCseq(int cseq);

	Review getLastReview();
}
