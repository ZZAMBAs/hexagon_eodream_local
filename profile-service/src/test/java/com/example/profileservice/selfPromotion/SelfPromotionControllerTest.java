package com.example.profileservice.selfPromotion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.profileservice.common.model.vo.PaymentType;
import com.example.profileservice.common.model.vo.ResponseDto;
import com.example.profileservice.resume.model.entity.ResumeEntity;
import com.example.profileservice.resume.repository.ResumeRepository;
import com.example.profileservice.selfPromotion.model.dto.request.SelfPromotionCreateRequest;
import com.example.profileservice.selfPromotion.model.dto.request.SelfPromotionUpdateRequest;
import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionResponse;
import com.example.profileservice.selfPromotion.model.entity.SelfPromotionEntity;
import com.example.profileservice.selfPromotion.repository.SelfPromotionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SelfPromotionControllerTest {

    private static final String BASE_URL = "/api/self-promotions";
    private static final String TEST_MEMBER_CODE = "member-test-uuid-001";
    private static final String OTHER_MEMBER_CODE = "member-test-uuid-999";
    private static final String HEADER_X_CODE = "X-CODE";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SelfPromotionRepository selfPromotionRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    private SelfPromotionEntity initialPromotion;
    private SelfPromotionEntity otherPromotion;
    private ResumeEntity validResume;

    @BeforeEach
    void setUp() {
        // 1. 유효한 이력서 (연결 가능하도록)
        validResume = ResumeEntity.builder()
                .memberCode(TEST_MEMBER_CODE)
                .title("테스트 이력서")
                .build();
        resumeRepository.save(validResume);

        // 2. 기본 프로모션 데이터 (TEST_MEMBER_CODE 소유)
        initialPromotion = SelfPromotionEntity.create(
                TEST_MEMBER_CODE,
                "기본 프로모션",
                "기본 내용",
                PaymentType.MONTHLY,
                5000000L,
                validResume.getCode()
        );
        selfPromotionRepository.save(initialPromotion);

        // 3. 다른 회원의 프로모션 데이터
        otherPromotion = SelfPromotionEntity.create(
                OTHER_MEMBER_CODE,
                "타인 프로모션",
                "타인 내용",
                PaymentType.PER_JOB,
                500000L,
                null
        );
        selfPromotionRepository.save(otherPromotion);
    }

    // --- 목록 및 상세 조회 테스트 ---

    @Test
    @DisplayName("GET /api/self-promotions - 전체 프로모션 목록 조회 성공 (활성만)")
    void getAllPromotions_Success() throws Exception {
        // given: 삭제된 프로모션 추가
        SelfPromotionEntity deletedPromotion = SelfPromotionEntity.create(
                TEST_MEMBER_CODE, "삭제 예정", "내용", PaymentType.MONTHLY, 1000L, null);
        deletedPromotion.delete();
        selfPromotionRepository.save(deletedPromotion);

        // when & then
        MvcResult result = mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.length()").value(2)) // 초기 2개 (initial, other)
                .andReturn();

        // 삭제된 게시글이 포함되지 않았는지 확인
        String responseJson = result.getResponse().getContentAsString();
        ResponseDto<List<SelfPromotionResponse>> responseDto = objectMapper.readValue(responseJson, new TypeReference<>() {});

        List<String> returnedCodes = responseDto.getData().stream()
                .map(SelfPromotionResponse::promotionCode)
                .toList();

        assertThat(returnedCodes).containsExactlyInAnyOrder(initialPromotion.getCode(), otherPromotion.getCode());
    }

    @Test
    @DisplayName("GET /api/self-promotions/me - 내 프로모션 목록 조회 성공")
    void getMyPromotions_Success() throws Exception {
        // when & then
        MvcResult result = mockMvc.perform(get(BASE_URL + "/me")
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.length()").value(1)) // 내 프로모션 1개
                .andReturn();

        // 결과 검증: 본인 프로모션만 조회되었는지 확인
        String responseJson = result.getResponse().getContentAsString();
        ResponseDto<List<SelfPromotionResponse>> responseDto = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertThat(responseDto.getData().get(0).promotionCode()).isEqualTo(initialPromotion.getCode());
    }

    @Test
    @DisplayName("GET /api/self-promotions/{promotionCode} - 프로모션 상세 조회 성공")
    void getPromotionDetail_Success() throws Exception {
        // when & then
        mockMvc.perform(get(BASE_URL + "/{promotionCode}", initialPromotion.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(initialPromotion.getTitle()))
                .andExpect(jsonPath("$.data.resumeCode").value(validResume.getCode()));
    }

    @Test
    @DisplayName("GET /api/self-promotions/{promotionCode} - 존재하지 않는 프로모션 조회 시 404 Not Found")
    void getPromotionDetail_NotFound_Failure() throws Exception {
        // when & then
        mockMvc.perform(get(BASE_URL + "/{promotionCode}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(3401)); // PROMOTION_NOT_FOUND
    }

    // --- 등록 (CREATE) 테스트 ---

    @Test
    @DisplayName("POST /api/self-promotions - 프로모션 등록 성공 (이력서 연결 O)")
    void createPromotion_WithResume_Success() throws Exception {
        // given
        SelfPromotionCreateRequest request = new SelfPromotionCreateRequest(
                "새 프로모션 제목", "어필 내용", PaymentType.PER_JOB, 100000L, validResume.getCode());

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(request.title()))
                .andExpect(jsonPath("$.data.resumeCode").value(validResume.getCode()))
                .andExpect(jsonPath("$.data.memberCode").value(TEST_MEMBER_CODE));
    }

    @Test
    @DisplayName("POST /api/self-promotions - 프로모션 등록 성공 (이력서 연결 X)")
    void createPromotion_WithoutResume_Success() throws Exception {
        // given
        SelfPromotionCreateRequest request = new SelfPromotionCreateRequest(
                "연결 없는 프로모션", "내용", PaymentType.MONTHLY, 3000000L, null);

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.resumeCode").doesNotExist());
    }

    @Test
    @DisplayName("POST /api/self-promotions - 존재하지 않는 이력서 연결 시 400 Bad Request")
    void createPromotion_InvalidResume_Failure() throws Exception {
        // given
        String invalidResumeCode = UUID.randomUUID().toString();
        SelfPromotionCreateRequest request = new SelfPromotionCreateRequest(
                "잘못된 연결", "내용", PaymentType.MONTHLY, 3000000L, invalidResumeCode);

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(3403)); // INVALID_RESUME_CODE_LINK
    }

    // --- 수정 (UPDATE) 테스트 ---

    @Test
    @DisplayName("PATCH /api/self-promotions/{promotionCode} - 프로모션 수정 성공")
    void updatePromotion_Success() throws Exception {
        // given
        SelfPromotionUpdateRequest request = new SelfPromotionUpdateRequest(
                "수정된 제목", "수정된 내용", PaymentType.PER_JOB, 100000L, null);

        // when & then
        mockMvc.perform(patch(BASE_URL + "/{promotionCode}", initialPromotion.getCode())
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("수정된 제목"))
                .andExpect(jsonPath("$.data.content").value("수정된 내용"))
                .andExpect(jsonPath("$.data.paymentType").value("PER_JOB"))
                .andExpect(jsonPath("$.data.unitAmount").value(100000L))
                .andExpect(jsonPath("$.data.resumeCode").doesNotExist()); // 기존에 연결된 이력서 코드가 null로 업데이트되어야 함
    }

    @Test
    @DisplayName("PATCH /api/self-promotions/{promotionCode} - 일부 필드만 수정 성공 (기존 값 유지)")
    void updatePromotion_Partial_Success() throws Exception {
        // given: title만 수정 요청 (나머지는 null)
        SelfPromotionUpdateRequest request = new SelfPromotionUpdateRequest(
                "부분 수정 제목", null, null, null, null);

        // when & then
        mockMvc.perform(patch(BASE_URL + "/{promotionCode}", initialPromotion.getCode())
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("부분 수정 제목")) // 수정됨
                .andExpect(jsonPath("$.data.content").value(initialPromotion.getContent())) // 기존 값 유지
                .andExpect(jsonPath("$.data.paymentType").value(initialPromotion.getPaymentType().name())) // 기존 값 유지
                .andExpect(jsonPath("$.data.unitAmount").value(initialPromotion.getUnitAmount())) // 기존 값 유지
                .andExpect(jsonPath("$.data.resumeCode").value(initialPromotion.getResumeCode())); // 기존 값 유지
    }

    @Test
    @DisplayName("PATCH /api/self-promotions/{promotionCode} - 타인 프로모션 수정 시 403 Forbidden")
    void updatePromotion_Unauthorized_Failure() throws Exception {
        // given
        SelfPromotionUpdateRequest request = new SelfPromotionUpdateRequest("수정 제목", "내용", PaymentType.PER_JOB, 1000L, null);

        // when & then: 타인 프로모션에 본인 코드로 수정 시도
        mockMvc.perform(patch(BASE_URL + "/{promotionCode}", otherPromotion.getCode())
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(3402)); // UNAUTHORIZED_PROMOTION_ACCESS
    }

    // --- 삭제 (DELETE) 테스트 ---

    @Test
    @DisplayName("DELETE /api/self-promotions/{promotionCode} - 프로모션 삭제 성공 (Soft Delete)")
    void deletePromotion_Success() throws Exception {
        // when
        mockMvc.perform(delete(BASE_URL + "/{promotionCode}", initialPromotion.getCode())
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        // DB에서 Soft Delete 확인 (isDeleted = true)
        assertThat(selfPromotionRepository.findByCodeAndIsDeletedFalse(initialPromotion.getCode())).isEmpty();
        assertThat(selfPromotionRepository.findById(String.valueOf(initialPromotion.getId())).get().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("DELETE /api/self-promotions/{promotionCode} - 타인 프로모션 삭제 시 403 Forbidden")
    void deletePromotion_Unauthorized_Failure() throws Exception {
        // when & then
        mockMvc.perform(delete(BASE_URL + "/{promotionCode}", otherPromotion.getCode())
                        .header(HEADER_X_CODE, TEST_MEMBER_CODE))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(3402)); // UNAUTHORIZED_PROMOTION_ACCESS
    }
}
