package com.example.contractservice.common.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.example.contractservice.common.logging.dto.SlackErrorInfo;
import com.example.contractservice.common.util.StringUtil;
import java.util.Map;

public class SlackErrorAppender extends AppenderBase<ILoggingEvent> {
    private static final String NO_VAL = "none";
    private static final int LIMIT = 3;

    private String webhookUrl;
    private int ttlMillis;
    private int connectionTimeoutMillis;
    private int readTimeoutMillis;

    private SlackErrorDeduplicator errorDeduplicator;
    private SlackSender slackSender;

    @Override
    public void start() {
        if (ttlMillis <= 0 || connectionTimeoutMillis <= 0 || readTimeoutMillis <= 0)
            return;

        super.start();
        errorDeduplicator = new CaffeineErrorDeduplicator(ttlMillis);
        slackSender = new SlackSender(webhookUrl, connectionTimeoutMillis, readTimeoutMillis);
    }

    // 오케스트레이터. 캐시 검증, MDC와 이벤트로부터 데이터 조합 후, 슬랙 센더에 전달
    @Override
    protected void append(ILoggingEvent event) {
        Map<String, String> mdcMap = event.getMDCPropertyMap();
        String key = assembleVal(mdcMap, event);

        synchronized (this) {
            if (errorDeduplicator.isDuplicate(key))
                return;
            errorDeduplicator.add(key);
        }

        SlackErrorInfo dataForSlack = createDataForSlack(mdcMap, event);

        try {
            slackSender.send(dataForSlack);
        } catch (Exception e) { // 전송 실패 예외 시
            errorDeduplicator.remove(key);
        }
    }

    // method + path + callerName + ExceptionName
    private String assembleVal(Map<String, String> mdcMap, ILoggingEvent event) {
        return StringUtil.format("{}:{}:{}:{}",
                mdcMap.getOrDefault("httpMethod", NO_VAL),
                mdcMap.getOrDefault("httpPath", NO_VAL),
                event.getLoggerName(),
                event.getThrowableProxy().getClassName()
        );
    }

    private SlackErrorInfo createDataForSlack(Map<String, String> mdcMap, ILoggingEvent event) {
        return new SlackErrorInfo(
                mdcMap.getOrDefault("httpMethod", NO_VAL),
                mdcMap.getOrDefault("httpPath", NO_VAL),
                event.getLoggerName(),
                getBriefException(event.getThrowableProxy())
        );
    }

    // 중첩 예외 최대 3개까지 최상단부터 출력
    private String getBriefException(IThrowableProxy proxy) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LIMIT; i++) {
            if (proxy == null)
                break;

            if (i > 0)
                sb.append("at ");

            sb.append(proxy.getClassName()).append(":").append(proxy.getMessage()).append("\n");
            proxy = proxy.getCause();
        }

        return sb.toString();
    }

    // setter
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setTtlMillis(int ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }
}
