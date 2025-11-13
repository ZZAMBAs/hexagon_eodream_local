package com.example.memberservice.common.redis.service;


import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRedisService implements RedisSingleDataService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String REDIS_KEY_PREFIX = "TOKEN:";

    @Override
    public int setSingleData(String key, Object value, long refreshTokenTTL) {
        Duration duration = Duration.ofMillis(refreshTokenTTL);

        return this.executeOperation(() -> valueOperations().set(buildKey(key), value, duration));
    }

    @Override
    public Optional<String> getSingleData(String key) {

        Object value = valueOperations().get(buildKey(key));

        return Optional.ofNullable(value).map(Object::toString);
    }

    @Override
    public boolean deleteSingleData(String key) {

        Boolean result = redisTemplate.delete(key);

        return Boolean.TRUE.equals(result);
    }

    private ValueOperations<String, Object> valueOperations() {
        return redisTemplate.opsForValue();
    }

    private ListOperations<String, Object> listOperations() {
        return redisTemplate.opsForList();
    }


    private int executeOperation(Runnable operation) {
        try {
            operation.run();
            log.info("redis에 정상 저장하였습니다.");
            return 1;
        } catch (Exception e) {
            log.info("Redis에 정상 저장되지 못했습니다.");
            return 0;
        }
    }

    private String buildKey(String key) {
        return String.format("%s%s", REDIS_KEY_PREFIX, key);
    }
}
