package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

    Qna findById(int qseq);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1 AND q.useyn = 'y'")
    Page<Qna> findAllByType(String type, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1")
    Page<Qna> findAllByTypeExceptNone(String type, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1 AND q.member.username LIKE %?2%")
    Page<Qna> findAllByUsername(String type, String username, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1 AND q.member.name LIKE %?2%")
    Page<Qna> findAllByName(String type, String name, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1 AND q.title LIKE %?2%")
    Page<Qna> findAllByTitle(String type, String title, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.type = ?1 AND q.content LIKE %?2%")
    Page<Qna> findAllByContent(String type, String content, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.member = ?1 AND q.useyn = 'y'")
    Page<Qna> findAllByMember(Member member, Pageable pageable);
}