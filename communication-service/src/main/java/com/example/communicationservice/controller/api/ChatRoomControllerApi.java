package com.example.communicationservice.controller.api;

import com.example.communicationservice.common.response.ResponseDto;
import com.example.communicationservice.controller.dto.request.ChatRoomCreateRequest;
import com.example.communicationservice.controller.dto.response.ChatRoomCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "ChatRoom API", description = "채팅방 CRUD")
public interface ChatRoomControllerApi {

    @Operation(
        summary = "채팅방 생성",
        description = "채팅방 이름과 참여할 회원 코드 목록으로 새로운 채팅방을 생성합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "채팅방 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<ResponseDto<ChatRoomCreateResponse>> createRoom(
        @RequestBody(
            description = "채팅방 생성 요청 DTO",
            required = true
        )
        ChatRoomCreateRequest request,

        @Parameter(
            name = "X-CODE",
            description = "현재 로그인한 회원 코드",
            required = true,
            in = ParameterIn.HEADER,
            example = "abc-12345-ABC"
        )
        String currentMemberCode
    );

}
