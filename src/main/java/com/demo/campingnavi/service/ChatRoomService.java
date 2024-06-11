package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {

    public List<ChatRoom> findAllRoom();
    public ChatRoom findRoomById(String roomId);
    public ChatRoom createChatRoom(String name);

}
