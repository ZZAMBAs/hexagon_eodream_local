package com.example.communicationservice.service;

import com.example.communicationservice.common.exception.ChatRoomException;
import com.example.communicationservice.common.status.ResponseDtoStatus;
import com.example.communicationservice.controller.dto.response.ChatMessageListReadResponse;
import com.example.communicationservice.entity.ChatMessage;
import com.example.communicationservice.entity.ChatRoom;
import com.example.communicationservice.repository.ChatMessageRepository;
import com.example.communicationservice.repository.ChatRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    private static final String ROOM_ID = "test-room-id-123";
    private static final String MY_CODE = "USER_A";
    private static final String PARTNER_CODE = "USER_B";
    private static final String ROOM_NAME = "채팅방";

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Test
    void 채팅방_메시지_목록을_페이징하여_조회한다() {
        // given
        ChatRoom chatRoom = createMockChatRoom(List.of(MY_CODE, PARTNER_CODE));
        Pageable pageable = PageRequest.of(0, 10);

        ChatMessage message1 = createMockMessage("안녕하세요", MY_CODE);
        ChatMessage message2 = createMockMessage("반갑습니다", PARTNER_CODE);

        List<ChatMessage> messages = List.of(message1, message2);

        Page<ChatMessage> mockPage = new PageImpl<>(
            messages,
            pageable,
            10L
        );

        given(chatRoomRepository.findById(eq(ROOM_ID)))
            .willReturn(Optional.of(chatRoom));
        given(chatMessageRepository.findAllByRoomId(eq(ROOM_ID), eq(pageable)))
            .willReturn(mockPage);

        // when
        ChatMessageListReadResponse response = chatMessageService.findMessagesByRoomId(ROOM_ID, MY_CODE, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.messages()).hasSize(messages.size());
        assertThat(response.messages().get(0).senderCode()).isEqualTo(MY_CODE);

        assertThat(response.pageInfo().totalElements()).isEqualTo(mockPage.getTotalElements());
        assertThat(response.pageInfo().totalPages()).isEqualTo(1);
        assertThat(response.pageInfo().page()).isEqualTo(pageable.getPageNumber());

        verify(chatRoomRepository, times(1)).findById(eq(ROOM_ID));
        verify(chatMessageRepository, times(1)).findAllByRoomId(eq(ROOM_ID), eq(pageable));
    }

    @Test
    void 채팅방이_존재하지_않으면_예외가_발생한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        given(chatRoomRepository.findById(any(String.class)))
            .willReturn(Optional.empty());

        // when & then
        ChatRoomException exception = assertThrows(ChatRoomException.class,
            () -> chatMessageService.findMessagesByRoomId(ROOM_ID, MY_CODE, pageable));

        assertThat(exception.getStatus()).isEqualTo(ResponseDtoStatus.CHATROOM_NOT_FOUND);

        verify(chatRoomRepository, times(1)).findById(eq(ROOM_ID));
    }

    @Test
    void 현재_로그인한_회원이_채팅방_참여자가_아니면_예외가_발생한다() {
        // given
        ChatRoom chatRoom = createMockChatRoom(List.of(PARTNER_CODE, "USER_C")); // 참여자에 MY_CODE 없음
        Pageable pageable = PageRequest.of(0, 10);

        given(chatRoomRepository.findById(eq(ROOM_ID)))
            .willReturn(Optional.of(chatRoom));

        // when & then
        ChatRoomException exception = assertThrows(ChatRoomException.class,
            () -> chatMessageService.findMessagesByRoomId(ROOM_ID, MY_CODE, pageable));

        assertThat(exception.getStatus()).isEqualTo(ResponseDtoStatus.CHATROOM_UNAUTHORIZED);

        verify(chatMessageRepository, times(0)).findAllByRoomId(any(String.class), any(Pageable.class));
    }

    private ChatRoom createMockChatRoom(List<String> memberCodes) {
        ChatRoom chatRoom = ChatRoom.builder()
            .name(ROOM_NAME)
            .memberCodes(memberCodes)
            .build();

        ReflectionTestUtils.setField(chatRoom, "id", ROOM_ID);

        return chatRoom;
    }

    private ChatMessage createMockMessage(String content, String senderCode) {
        ChatMessage message = ChatMessage.builder()
            .roomId(ROOM_ID)
            .senderCode(senderCode)
            .content(content)
            .build();

        ReflectionTestUtils.setField(message, "id", "msg-" + Instant.now().getNano());
        ReflectionTestUtils.setField(message, "sentAt", Instant.now());

        return message;
    }

}
