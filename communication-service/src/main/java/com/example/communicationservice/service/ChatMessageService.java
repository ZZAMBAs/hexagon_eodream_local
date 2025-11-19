package com.example.communicationservice.service;

import com.example.communicationservice.common.exception.ChatRoomException;
import com.example.communicationservice.common.status.ResponseDtoStatus;
import com.example.communicationservice.controller.dto.response.ChatMessageListReadResponse;
import com.example.communicationservice.controller.dto.response.ChatMessageReadResponse;
import com.example.communicationservice.controller.dto.response.PageInfo;
import com.example.communicationservice.entity.ChatMessage;
import com.example.communicationservice.entity.ChatRoom;
import com.example.communicationservice.repository.ChatMessageRepository;
import com.example.communicationservice.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 특정 채팅방의 메시지 목록을 페이징하여 조회합니다.
     * @param roomId 조회할 채팅방 아이디
     * @param currentMemberCode 현재 로그인한 회원의 코드
     * @param pageable 페이징 요청 정보
     * @return 해당 채팅방의 메시지 목록
     */
    public ChatMessageListReadResponse findMessagesByRoomId(String roomId, String currentMemberCode, Pageable pageable) {
        // 해당 채팅방이 존재하는지 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatRoomException(ResponseDtoStatus.CHATROOM_NOT_FOUND));

        // 현재 로그인한 회원이 해당 채팅방의 참여자인지 확인
        if (!chatRoom.getMemberCodes().contains(currentMemberCode)) {
            throw new ChatRoomException(ResponseDtoStatus.CHATROOM_UNAUTHORIZED);
        }

        Page<ChatMessage> messagePage = chatMessageRepository.findAllByRoomId(roomId, pageable);

        // 엔티티 -> DTO 변환
        List<ChatMessageReadResponse> messages = messagePage.getContent().stream()
            .map(ChatMessageReadResponse::from)
            .toList();

        // Page 정보 추출 및 DTO 생성
        PageInfo pageInfo = PageInfo.from(messagePage);

        return new ChatMessageListReadResponse(messages, pageInfo);
    }

}
