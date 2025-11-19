package com.example.contractservice.contract.service.batch.reader;

import com.example.contractservice.contract.common.ContractStatus;
import com.example.contractservice.contract.entity.ContractEntity;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.util.Map;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ContractInProgressReader extends JpaCursorItemReader<ContractEntity> { // JpaPagingItemReader와 비교 필요

    public ContractInProgressReader(EntityManagerFactory emFactory,
            @Value("#{jobParameters['dateStr']}") String dateStr,
            @Value("${batch.contract.size}") int fetchSize) {
        setEntityManagerFactory(emFactory);

        setQueryString("""
            SELECT c
            FROM ContractEntity c
            WHERE c.status = :status AND c.endedAt < :time
        """);

        setHintValues(Map.of("org.hibernate.fetchSize", fetchSize));
        Instant todayMidnight = Instant.parse(dateStr);

        setParameterValues(Map.of("time", todayMidnight, "status", ContractStatus.IN_PROGRESS));
    }
}
