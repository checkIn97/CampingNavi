package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.ChatRoom;
import com.demo.campingnavi.dto.ChatMessage;
import com.demo.campingnavi.repository.jpa.ChatRoomRepository;
import com.demo.campingnavi.repository.mongo.MongoChatMessageRepository;
import com.demo.campingnavi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final MongoChatMessageRepository mongoChatMessageRepository;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomService.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               @RequestParam int maxMem,
                               @RequestParam String[] purpose) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(UUID.randomUUID().toString());
        chatRoom.setName(name);
        chatRoom.setStartDate(startDate);
        chatRoom.setEndDate(endDate);
        chatRoom.setMaxMem(maxMem);
        chatRoom.setPurpose(List.of(purpose));
        chatRoomService.createChatRoom(chatRoom);
        return chatRoomService.createChatRoom(chatRoom);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomService.findRoomById(roomId);
    }

    // 특정 채팅방의 모든 메시지 조회
    @GetMapping("/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessage> roomMessages(@PathVariable String roomId) {
        return mongoChatMessageRepository.findByRoomId(roomId);
    }

    @RequestMapping("/create")
    public String chatCreate() {
        return "chat/create";
    }


}
