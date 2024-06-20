package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatRoomVo {
    private ChatRoom chatRoom;
    private float score;
    private String scoreView;

    public ChatRoomVo(ChatRoom chatRoom, float score) {
        this.chatRoom = chatRoom;
        this.score = score;
        this.scoreView = String.format("%.1f", score);
    }

}
