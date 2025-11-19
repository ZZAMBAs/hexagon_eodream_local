package com.example.contractservice.contract.service.batch.reader;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.entity.ContractEntity;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class ContractPaidReader extends JpaCursorItemReader<ContractEntity> { // JpaPagingItemReader와 비교 필요

    public ContractPaidReader(EntityManagerFactory emFactory,
            @Value("#{jobParameters['dateStr']}") String dateStr,
            @Value("${batch.contract.size}") int fetchSize) {
        setEntityManagerFactory(emFactory);

        setQueryString("""
            SELECT c
            FROM ContractEntity c
            WHERE c.status = :status AND c.startedAt <= :time
        """);

        setHintValues(Map.of("org.hibernate.fetchSize", fetchSize));
        Instant todayMidnight = Instant.parse(dateStr);

        log.info("시각: {}", todayMidnight);

        setParameterValues(Map.of("time", todayMidnight, "status", ContractStatus.PAID));
    }
}
