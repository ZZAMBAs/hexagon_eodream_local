package com.example.communicationservice.controller.dto.request;

public record ChatMessageSendRequest(
    String roomId,
    String senderId,
    String content
) {
}
