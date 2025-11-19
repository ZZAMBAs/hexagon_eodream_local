package com.example.contractservice.deposit.repository;

import static com.example.contractservice.deposit.domain.exception.DepositErrorCode.NO_DEPOSIT_ENTITY;

import com.example.contractservice.deposit.domain.exception.DepositException;
import com.example.contractservice.deposit.entity.DepositEntity;
import com.example.contractservice.deposit.entity.DepositHistoryEntity;
import com.example.contractservice.deposit.entity.QDepositHistoryEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepositRepository {
    private final DepositJpaRepository depositJpaRepository;
    private final DepositHistoryJpaRepository depositHistoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    public DepositEntity findDepositByMemberCode(String memberCode) {
        return depositJpaRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> new DepositException(NO_DEPOSIT_ENTITY));
    }

    public DepositEntity saveDeposit(DepositEntity depositEntity) {
        return depositJpaRepository.save(depositEntity);
    }

    public DepositHistoryEntity saveDepositHistory(DepositHistoryEntity depositHistoryEntity) {
        return depositHistoryJpaRepository.save(depositHistoryEntity);
    }

    public boolean existMemberDeposit(String memberCode) {
        return depositJpaRepository.existsByMemberCode(memberCode);
    }

    public List<DepositHistoryEntity> findAllBy(String depositCode, Instant cursorDate, String cursorCode, int limit) {
        QDepositHistoryEntity history = QDepositHistoryEntity.depositHistoryEntity;

        BooleanExpression predicate = history.depositCode.eq(depositCode);

        if (cursorDate != null && cursorCode != null) {
            BooleanExpression cursorPredicate = history.createdAt.lt(cursorDate)
                    .or(history.createdAt.eq(cursorDate)
                            .and(history.code.gt(cursorCode))
                    );
            predicate = predicate.and(cursorPredicate);
        }

        return queryFactory.selectFrom(history)
                .where(predicate)
                .orderBy(history.createdAt.desc(), history.code.asc())
                .limit(limit + 1L) // hasNext 판별
                .fetch();
    }
}
