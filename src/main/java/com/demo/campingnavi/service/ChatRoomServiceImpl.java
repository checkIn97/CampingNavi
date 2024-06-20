package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.repository.jpa.ChatRoomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Map<String, ChatRoom> chatRoomMap;
    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    @Override
    public List<ChatRoom> findAllRoom() {
        List chatRooms = chatRoomRepository.findAll();
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    @Override
    public ChatRoom findRoomById(String roomId) {

        return null;
    }

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    // 이름에 특정 문자열이 포함된 채팅방 찾기
    @Override
    public List<ChatRoom> findByCampNameContaining(String campName) {
        return chatRoomRepository.findByCampNameContaining(campName);
    }

    @Override
    public List<Camp> getCampListExistingChatRoom() {
        return chatRoomRepository.getCampListExistingChatRoom();
    }
}
