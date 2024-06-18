package com.demo.campingnavi.service;

import java.util.List;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.repository.jpa.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.campingnavi.domain.ReviewComment;
import com.demo.campingnavi.repository.jpa.ReviewCommentRepository;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

	@Autowired
	ReviewRepository reviewRepo;
	@Autowired
	ReviewCommentRepository ReviewCommentRepo;

	@Override
	public void saveComment(ReviewComment vo) {
		ReviewCommentRepo.save(vo);
		updateCommentCount(vo.getReview().getVseq());
	}

	@Override
	public List<ReviewComment> getCommentList(int vseq) {
		return ReviewCommentRepo.findParentReviewCommentByVseq(vseq);
	}



	@Override
	public List<ReviewComment> getReplyCommentList(int parentCmseq) {
		return ReviewCommentRepo.findRepliesByParentCommentCmseq(parentCmseq);
	}


	@Override
	public void deletComment(int cmseq) {
		ReviewComment comment = ReviewCommentRepo.findById(cmseq).orElse(null);
		if (comment != null) {
			ReviewCommentRepo.deleteByParentComment_Cmseq(cmseq);
			ReviewCommentRepo.deleteById(cmseq);
			updateCommentCount(comment.getReview().getVseq());
		}
	}

	@Override
	public void deletAllComment(int vseq) {
		ReviewCommentRepo.deleteByReviewVseq(vseq);
		updateCommentCount(vseq);
	}

	@Override
	public void updateCommentCount(int vseq) {
		Review review = reviewRepo.findById(vseq).orElse(null);
		if (review != null) {
			review.setCommentCount(ReviewCommentRepo.countByReviewVseq(vseq));
			reviewRepo.save(review);
		}

	}

	@Override
	public List<ReviewComment> getCommentMemberList(int mseq) {
		return ReviewCommentRepo.findReviewCommentByMseq(mseq);
	}
}
