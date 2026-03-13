package com.example.contractservice.common.logging;

import com.example.contractservice.common.logging.dto.SlackErrorInfo;
import com.example.contractservice.common.util.StringUtil;
import com.slack.api.SlackConfig;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.util.json.GsonFactory;
import com.slack.api.webhook.Payload;
import java.time.Duration;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class SlackSender {
    private static final String COLOR = "#ff0000";
    private static final String EXCEPTION_TITLE = "예외 발생";
    private static final String ROOT_CAUSE_FORMAT = "root cause: {}";

    private final String webhookUrl;
    private final RestClient restClient;

    public SlackSender(String webhookUrl, int connectionTimeoutMillis, int readTimeoutMillis) {
        this.webhookUrl = webhookUrl;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(connectionTimeoutMillis));
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMillis));

        restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    public void send(SlackErrorInfo info) {
        String payload = getPayload(info);

        String res = restClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve().body(String.class);

        if (!"ok".equalsIgnoreCase(res)) {
            throw new IllegalStateException(StringUtil.format("Slack 알림이 제대로 전달되지 않았거나 타임아웃이 발생했습니다. 응답: {}", res));
        }
    }

    private static String getPayload(SlackErrorInfo info) {
        List<Field> fields = List.of(
                Field.builder()
                        .title(resolveTitle(info))
                        .value(StringUtil.format("발생 클래스: {} \n예외: {}\n{}",
                                info.loggingClass(),
                                info.exceptionSummary(),
                                StringUtil.format(ROOT_CAUSE_FORMAT, info.rootCause())))
                        .build()
        );
        List<Attachment> attachments = List.of(
                Attachment.builder()
                        .color(COLOR)
                        .fields(fields)
                        .build()
        );
        Payload payload = Payload.builder()
                .text("에러가 발생했습니다! 빠른 처리가 필요합니다!")
                .attachments(attachments)
                .build();

        return GsonFactory
                .createSnakeCase(SlackConfig.DEFAULT)
                .toJson(payload);
    }

    private static String resolveTitle(SlackErrorInfo info) {
        if (isBlank(info.httpMethod()) && isBlank(info.httpPath())) {
            return EXCEPTION_TITLE;
        }

        return StringUtil.format("{} {} 요청에서 에러 발생", info.httpMethod(), info.httpPath());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
