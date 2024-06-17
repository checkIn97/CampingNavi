package com.demo.campingnavi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.ReviewScanVo;
import com.demo.campingnavi.repository.jpa.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepo;

	@Override
	public void insertReview(Review vo) {
		reviewRepo.save(vo);
	}

	@Override
	public Review getReview(int vseq) {
		return reviewRepo.findById(vseq).get();
	}

	@Override
	public void editReview(Review vo) {
		reviewRepo.save(vo);
	}

	@Override
	public void deleteReview(int vseq) {
	    reviewRepo.deleteById(vseq); 
	}
	
	@Override
	@Transactional
	public int updateCnt(int vseq) {
		return reviewRepo.updateCount(vseq);
	}

	@Override
	public List<Review> getBestReviewList() {
		return reviewRepo.findBestList();
	}

	@Override
	public List<Review> getAuthorReviewList(int mseq) {
		return reviewRepo.findAuthorList(mseq);
	}

	@Override
	public void likePost(int vseq) {
		Review review = reviewRepo.findById(vseq).orElseThrow(() -> new RuntimeException("Review not found"));
		review.setLikes(review.getLikes() + 1);
		reviewRepo.save(review);
	}

	@Override
	public void unlikePost(int vseq) {
		Review review = reviewRepo.findById(vseq).orElseThrow(() -> new RuntimeException("Review not found"));
		review.setLikes(review.getLikes() - 1);
		reviewRepo.save(review);
	}

	@Override
	public List<Review> getReviewListByCseq(int cseq) {
		return reviewRepo.findCampReviewList(cseq);
	}


	@Override
	public Page<Review> findReviewList(ReviewScanVo reviewScanVo, int page, int size) {
	    Pageable pageable = null;
	    if (reviewScanVo.getSortDirection().equals("ASC")) {
	        pageable = PageRequest.of(page - 1, size, Direction.ASC, reviewScanVo.getSortBy());
	    } else {
	        pageable = PageRequest.of(page - 1, size, Direction.DESC, reviewScanVo.getSortBy());
	    }

	    String searchField = reviewScanVo.getSearchField();
	    String searchWord = reviewScanVo.getSearchWord();

	    Page<Review> resultPage = null;
	    switch (searchField) {
	        case "title":
	            resultPage = reviewRepo.findByTitleContaining(searchWord, pageable);
	            break;
	        case "content":
	            resultPage = reviewRepo.findByContentContaining(searchWord, pageable);
	            break;
	        case "writer":
	            resultPage = reviewRepo.findByMemberUsername(searchWord, pageable);
	            break;
	        case "titleContent":
	            resultPage = reviewRepo.findReviewList(searchWord, searchWord, pageable);
	            break;
	        default:
	            resultPage = reviewRepo.findAllByOrderByCreatedAtDesc(pageable);
	            break;
	    }


	    return resultPage;
	}

}
