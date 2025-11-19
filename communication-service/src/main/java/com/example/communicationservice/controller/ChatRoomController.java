package com.example.communicationservice.controller;

import com.example.communicationservice.common.response.ResponseDto;
import com.example.communicationservice.controller.api.ChatRoomControllerApi;
import com.example.communicationservice.controller.dto.request.ChatRoomCreateRequest;
import com.example.communicationservice.controller.dto.response.ChatRoomCreateResponse;
import com.example.communicationservice.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomController implements ChatRoomControllerApi {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성 API
    @PostMapping
    @Override
    public ResponseEntity<ResponseDto<ChatRoomCreateResponse>> createRoom(
        @Valid @RequestBody ChatRoomCreateRequest request,
        @RequestHeader(name = "X-CODE") String currentMemberCode
    ) {
        ChatRoomCreateResponse response = chatRoomService.createChatRoom(
            request.name(), request.memberCodes(),
            currentMemberCode
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseDto.success(response));
    }

}
