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
public class ChatPT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cptseq; // 채팅 번호

    @ManyToOne
    @JoinColumn(name="mseq", nullable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="chseq", nullable=false)
    private ChatRoom chatRoom;

    private Date inTime; // 채팅방 들어온 시간
    private Date outTime; // 채팅방 나간 시간
}