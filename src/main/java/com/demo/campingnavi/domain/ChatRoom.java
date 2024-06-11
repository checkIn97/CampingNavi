package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name="cseq")
    private Camp camp;
    @Id
    private String roomId;
    private String name;

    public static com.demo.campingnavi.domain.ChatRoom create(String name) {
        com.demo.campingnavi.domain.ChatRoom chatRoom = new com.demo.campingnavi.domain.ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}