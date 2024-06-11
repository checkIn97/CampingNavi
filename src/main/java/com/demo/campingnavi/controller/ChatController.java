package com.demo.campingnavi.controller;

import com.demo.campingnavi.dto.ChatMessage;

import com.demo.campingnavi.repository.mongo.MongoChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final MongoChatMessageRepository mongoChatMessageRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 메시지 타입이 ENTER인 경우 알림 메시지를 설정하는 부분
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");

            // 새로 입장한 유저에게 기존 메시지를 전송
            List<ChatMessage> messagesInRoom = mongoChatMessageRepository.findByRoomId(message.getRoomId());
            messagesInRoom.forEach(msg -> messagingTemplate.convertAndSend("/user/" + message.getSender() + "/queue/messages", msg));

            // 입장 메시지를 설정하고 저장
            message.setCreatedAt(LocalDateTime.now());
            mongoChatMessageRepository.save(message);

            // 채팅방에 입장 메시지를 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } else {
            // 다른 타입의 메시지 처리
            message.setCreatedAt(LocalDateTime.now());
            mongoChatMessageRepository.save(message);
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }



}
