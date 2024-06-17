package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Query("update Review r set r.count = r.count+1 where r.vseq = :vseq")
    int updateCount(@Param("vseq") int vseq);

    @Query("SELECT r FROM Review r "
            + "WHERE r.title LIKE %:title% "
            + "or r.content LIKE %:content% OR r.content IS NULL ")
    Page<Review> findReviewList(String title, String content, Pageable pageable);

    Page<Review> findByTitleContaining(String title, Pageable pageable);

    Page<Review> findByMemberUsername(String username, Pageable pageable);

    Page<Review> findByContentContaining(String writer, Pageable pageable);

    @Query("SELECT r FROM Review r ORDER BY r.count DESC")
    List<Review> findBestList();

    @Query("SELECT r FROM Review r WHERE r.member.mseq = ?1 ORDER BY r.createdAt DESC")
    List<Review> findAuthorList(int mseq);

    @Query("SELECT r From Review r WHERE r.camp.cseq = ?1 ORDER BY r.createdAt DESC")
    List<Review> findCampReviewList(int cseq);

}