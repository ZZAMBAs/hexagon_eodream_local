package com.example.communicationservice.controller;

import com.example.communicationservice.controller.dto.request.ChatMessageSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("chat.send")
    public void handleChatMessage(ChatMessageSendRequest request) {
        String destinationPrefix = "/queue/room/";
        messagingTemplate.convertAndSend(destinationPrefix + request.roomId(), request);
    }

}
