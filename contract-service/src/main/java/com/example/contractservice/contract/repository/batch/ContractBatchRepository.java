package com.example.contractservice.contract.repository.batch;

import com.example.contractservice.contract.domain.exception.ContractErrorCode;
import com.example.contractservice.contract.domain.exception.ContractException;
import com.example.contractservice.contract.repository.batch.dto.ContractBatchUpdateTarget;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContractBatchRepository {

    private static final int EXPECTED_UPDATED_NUM = 1;

    private final JdbcTemplate jdbcTemplate;

    public void updateAllStatusInBatch(List<ContractBatchUpdateTarget> targets) {
        String sql = """
                UPDATE contracts
                SET status = ?, updated_at = ?
                WHERE code = ? AND updated_at = ?
                """;

        List<Object[]> args = targets.stream()
                .map(target -> new Object[]{
                        target.nextStatus().name(),
                        target.newUpdatedAt(),
                        target.code(),
                        target.previousUpdatedAt()
                })
                .toList();

        int[] updatedCounts = jdbcTemplate.batchUpdate(sql, args);
        boolean allUpdated = Arrays.stream(updatedCounts).allMatch(i -> i == EXPECTED_UPDATED_NUM);

        if (!allUpdated) {
            throw new ContractException(ContractErrorCode.CONTRACT_BATCH_UPDATE_FAILED);
        }
    }
}
