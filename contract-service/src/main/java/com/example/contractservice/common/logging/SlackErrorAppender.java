package com.example.contractservice.common.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.example.contractservice.common.logging.dto.SlackErrorInfo;
import com.example.contractservice.common.util.StringUtil;
import java.util.Map;

public class SlackErrorAppender extends AppenderBase<ILoggingEvent> {
    private static final String NO_VAL = "none";
    private static final String NO_THROWABLE = "NO_THROWABLE";
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
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        String rootCauseClass = getRootCauseClass(throwableProxy);
        String key = assembleDedupKey(mdcMap, event, rootCauseClass);

        if (!errorDeduplicator.acquire(key))
            return;

        SlackErrorInfo dataForSlack = createDataForSlack(mdcMap, event, throwableProxy, rootCauseClass);

        try {
            slackSender.send(dataForSlack);
        } catch (Exception e) { // 전송 실패 예외 시
            errorDeduplicator.remove(key);
        }
    }

    private String assembleDedupKey(Map<String, String> mdcMap, ILoggingEvent event, String rootCauseClass) {
        return StringUtil.format("method={}||path={}||logger={}||rootCause={}||msg={}",
                getNormalizedValue(mdcMap.get("httpMethod")),
                getNormalizedValue(mdcMap.get("httpPath")),
                getNormalizedValue(event.getLoggerName()),
                getNormalizedValue(rootCauseClass),
                getNormalizedValue(event.getMessage())
        );
    }

    private SlackErrorInfo createDataForSlack(Map<String, String> mdcMap, ILoggingEvent event,
                                              IThrowableProxy throwableProxy, String rootCauseClass) {
        return new SlackErrorInfo(
                mdcMap.get("httpMethod"),
                mdcMap.get("httpPath"),
                event.getLoggerName(),
                throwableProxy == null ? getNormalizedValue(event.getFormattedMessage()) : getBriefException(throwableProxy),
                throwableProxy == null ? NO_VAL : buildRootCauseSummary(throwableProxy, rootCauseClass)
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

    private String buildRootCauseSummary(IThrowableProxy proxy, String rootCauseClass) {
        IThrowableProxy rootCause = getRootCauseProxy(proxy);
        return StringUtil.format("{}: {}", rootCauseClass, getNormalizedValue(rootCause.getMessage()));
    }

    private String getRootCauseClass(IThrowableProxy proxy) {
        if (proxy == null)
            return NO_THROWABLE;

        return getRootCauseProxy(proxy).getClassName();
    }

    private IThrowableProxy getRootCauseProxy(IThrowableProxy proxy) {
        IThrowableProxy current = proxy;

        while (current.getCause() != null) {
            current = current.getCause();
        }

        return current;
    }

    private String getNormalizedValue(String value) {
        if (value == null || value.isBlank())
            return NO_VAL;

        return value
                .replace("\r", " ")
                .replace("\n", " ")
                .trim();
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
