package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class QnaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qcseq; // 문의 답변 번호

    @ManyToOne
    @JoinColumn(name = "qseq", nullable = false)
    private Qna qna; // 문의

    @Column(length = 2000, nullable = false)
    private String content; // 내용

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    @Column(updatable=false)
    private Date createdAt; // 생성일
}
