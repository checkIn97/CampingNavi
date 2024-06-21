package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.jpa.ChatRoomRepository;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Transactional
@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Map<String, ChatRoom> chatRoomMap;
    @Autowired
    private MemberRepository memberRepository;

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


    @Override
    public void addUser(String roomId, int mseq) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        Member user = memberRepository.findById(mseq).get();

        chatRoom.upUserCount();
        chatRoom.addUser(String.valueOf(user.getMseq()));
        chatRoomRepository.save(chatRoom);

    }
    @Override
    public void delUser(String roomId, int mseq) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        Member user = memberRepository.findById(mseq).get();

        chatRoom.downUserCount();
        chatRoom.removeUser(String.valueOf(user.getMseq()));
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public String getUserName(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).get();

        return room.getUserList().toString();
    }

    @Override
    public List<String> getUserList(String roomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();
        List<String> users = chatRoom.getUserList();

        return users;
    }
}
