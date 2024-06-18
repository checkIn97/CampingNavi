package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Recommend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendVo {
    private int totalPages = 0;
    private int page = 0;
    private int size = 5;
    private int pageMaxDisplay = 5;
    private String sortDirection = "DESC";
}
