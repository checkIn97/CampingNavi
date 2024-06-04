package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
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
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chseq; // 채팅 번호

    @ManyToOne
    @JoinColumn(name="mseq", nullable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="crseq", nullable=false)
    private ChatRoom chatRoom;

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    @Column(updatable=false)
    private Date messageTime; // 채팅 보낸 시간

    @Column(length = 50, nullable=false)
    private String messageType; // 메세지 타입
}