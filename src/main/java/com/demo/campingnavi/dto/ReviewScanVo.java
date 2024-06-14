package com.demo.campingnavi.dto;

import java.util.List;

import com.demo.campingnavi.domain.Review;
import org.springframework.data.domain.Page;

import com.demo.campingnavi.domain.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewScanVo {
	private String[][] searchType = {{"title", "제목"}, {"content", "내용"}};
	private String searchField = "";
	private String searchWord = "";
	private Page<Review> pageInfo = null;
	private List<Review> reviewList = null;
	private String sortBy = "";
	private String sortDirection = "";
	private int pageMaxDisplay = 0;
}
