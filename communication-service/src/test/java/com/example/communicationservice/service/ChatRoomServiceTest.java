package com.example.communicationservice.service;

import com.example.communicationservice.client.MemberServiceClient;
import com.example.communicationservice.client.dto.MemberExistOutput;
import com.example.communicationservice.common.exception.ChatRoomException;
import com.example.communicationservice.common.response.ResponseDto;
import com.example.communicationservice.common.status.ResponseDtoStatus;
import com.example.communicationservice.controller.dto.response.ChatRoomCreateResponse;
import com.example.communicationservice.entity.ChatRoom;
import com.example.communicationservice.repository.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MemberServiceClient memberServiceClient;

    @Test
    void 채팅방_생성에_성공한다() {
        // given
        String roomName = "채팅방";
        String myCode = "USER_A";
        String partnerCode = "USER_B";
        String roomId = "generated-mongodb-id-123";
        List<String> memberCodes = List.of(myCode, partnerCode);

        MemberExistOutput mockOutput = new MemberExistOutput(memberCodes, List.of());

        given(memberServiceClient.getMemberExistences(anyList()))
            .willReturn(ResponseDto.success(mockOutput));

        given(chatRoomRepository.existsByMemberCodes(anyList(), anyInt()))
            .willReturn(false);

        ChatRoom savedChatRoom = ChatRoom.builder()
            .name(roomName)
            .memberCodes(memberCodes)
            .build();

        ReflectionTestUtils.setField(savedChatRoom, "id", roomId); // 리플렉션으로 아이디 추가

        given(chatRoomRepository.save(any(ChatRoom.class)))
            .willReturn(savedChatRoom);

        // when
        ChatRoomCreateResponse response = chatRoomService.createChatRoom(roomName, memberCodes, myCode);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(roomId);

        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    void 채팅방_참여자_수가_2명이_아니면_채팅방_생성에_실패한다() {
        // given
        String myCode = "USER_A";
        List<String> memberCodes = List.of(myCode);

        // when & then
        ChatRoomException exception = assertThrows(ChatRoomException.class,
            () -> chatRoomService.createChatRoom("채팅방", memberCodes, myCode));

        assertThat(exception.getStatus()).isEqualTo(ResponseDtoStatus.CHATROOM_INVALID_MEMBER_COUNT);
    }

    @Test
    void 채팅방_생성_요청자가_채팅방_참여자_목록에_없으면_채팅방_생성에_실패한다() {
        // given
        String myCode = "USER_A";
        List<String> memberCodes = List.of("USER_B", "USER_C");

        // when & then
        ChatRoomException exception = assertThrows(ChatRoomException.class,
            () -> chatRoomService.createChatRoom("채팅방", memberCodes, myCode));

        assertThat(exception.getStatus()).isEqualTo(ResponseDtoStatus.CHATROOM_NOT_INCLUDE_SELF);
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 회원이 포함됨")
    void 채팅방_참여자_목록에_유효하지_않은_회원이_있으면_채팅방_생성에_실패한다() {
        // given
        String myCode = "USER_A";
        String unknownCode = "USER_UNKNOWN";
        List<String> memberCodes = List.of(myCode, unknownCode);

        MemberExistOutput mockOutput = new MemberExistOutput(List.of(myCode), List.of(unknownCode));

        given(memberServiceClient.getMemberExistences(anyList()))
            .willReturn(ResponseDto.success(mockOutput));

        // when & then
        ChatRoomException exception = assertThrows(ChatRoomException.class,
            () -> chatRoomService.createChatRoom("채팅방", memberCodes, myCode));

        assertThat(exception.getStatus()).isEqualTo(ResponseDtoStatus.CHATROOM_INVALID_MEMBER);
    }

    @Test
    void 채팅방_참여자_사이에_이미_채팅방이_존재하면_채팅방_생성에_실패한다() {
        // given
        List<String> memberCodes = List.of("USER_A", "USER_B");

        MemberExistOutput mockOutput = new MemberExistOutput(memberCodes, List.of());

        given(memberServiceClient.getMemberExistences(anyList()))
            .willReturn(ResponseDto.success(mockOutput));

        given(chatRoomRepository.existsByMemberCodes(anyList(), anyInt()))
            .willReturn(true);

        // when & then
        ChatRoomException exception = assertThrows(ChatRoomException.class,
            () -> chatRoomService.createChatRoom("채팅방", memberCodes, "USER_A"));

        assertThat(exception.getStatus()).isEqualTo(ResponseDtoStatus.CHATROOM_ALREADY_EXISTS);
    }

}
