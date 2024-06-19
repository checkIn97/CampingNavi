package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;
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
    private String startDate;
    private String endDate;
    private Integer maxMem;
    private String campName;

    @ElementCollection
    private List<String> purpose;

    public static com.demo.campingnavi.domain.ChatRoom create(String name) {
        com.demo.campingnavi.domain.ChatRoom chatRoom = new com.demo.campingnavi.domain.ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}