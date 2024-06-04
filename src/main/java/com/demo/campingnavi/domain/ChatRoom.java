package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int crseq; // 채팅방 번호

    @ManyToOne
    @JoinColumn(name="cseq", nullable=false)
    private Camp camp;
}