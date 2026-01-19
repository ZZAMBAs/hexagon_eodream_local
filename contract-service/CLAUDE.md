# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew build          # Build the project
./gradlew test           # Run all tests
./gradlew bootRun        # Run the application locally
./gradlew bootJar        # Create executable JAR
```

Run a single test class:
```bash
./gradlew test --tests "com.example.contractservice.contract.service.ContractServiceTest"
```

## Project Overview

Contract Service is a Spring Boot 3.5.7 microservice (Java 17) that manages contracts, deposits, and settlements for the 이어드림 (hexagon_eodream) platform. It runs on port 8083.

### Dependencies

- **Core module**: Shared code at `../core` (included via `settings.gradle`)
- **Service discovery**: Eureka client
- **Database**: MySQL (prod), H2 (dev), with JPA and QueryDSL 5.0.0
- **Messaging**: Kafka for event-driven communication
- **Batch processing**: Spring Batch for scheduled settlements
- **Inter-service calls**: OpenFeign clients

## Architecture

The codebase follows Domain-Driven Design with three main modules:

### Contract Module (`contract/`)
Core business logic for contract lifecycle (create, pay, cancel). Uses cursor-based pagination for list queries.

- REST endpoints at `/api/contracts`
- User identified via `X-CODE` header
- Kafka handler processes `CartItemDeletedEvent` for cancellations

### Deposit Module (`deposit/`)
Manages user deposits and payment reserves.

- REST endpoints at `/api/deposits`

### Settlement Module (`settlement/`)
Calculates and processes monthly settlements to freelancers.

- Spring Batch job runs at 3 AM on the 15th of each month
- Default settlement rate: 12.5% (`batch.settlement.settlement-rate`)
- Uses `@OptimisticRetry` AOP for handling concurrent updates

### Common (`common/`)
Cross-cutting concerns:
- `GlobalExceptionHandler` / `DomainExceptionHandler` for centralized error handling
- Feign clients: `MemberClient`, `CommissionClient`
- `QueryDslConfig` for type-safe queries
- Base exception hierarchy: `DomainException` → `ContractException`, `DepositException`, `SettlementException`

## Kafka Topics

Producer:
- `contract-topic`: Contract events
- `commission-open-close-topic`: Commission status events

Consumer:
- `contract-topic`: Cart item deletion events
- `member-create-topic`: Member creation events

## Key Patterns

- **Domain vs Entity separation**: Pure domain objects in `domain/`, JPA entities in `entity/`
- **QueryDSL**: Used for complex queries in `*RepositoryCustom` implementations
- **Batch processing**: Reader/Processor/Writer pattern in `service/batch/` directories
- **Optimistic locking**: `@OptimisticRetry` annotation for retry on concurrent modifications

## Environment Variables

Required for production:
- `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`
- `ADMIN_MEMBER_CODE`, `ADMIN_DEPOSIT_CODE`

## Code Style
- google style sheet를 만족하되, 들여쓰기는 4칸 단위로 수행
- DTO 명은 앞에 Domain을 명시 (e.g. ContractRequest)
