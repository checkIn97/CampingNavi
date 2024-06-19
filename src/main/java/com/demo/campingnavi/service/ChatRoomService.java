package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {

    public List<ChatRoom> findAllRoom();
    public ChatRoom findRoomById(String roomId);
    public ChatRoom createChatRoom(ChatRoom chatRoom);

    // 이름에 특정 문자열이 포함된 채팅방 찾기
    public List<ChatRoom> findByCampNameContaining(String campName);


}
