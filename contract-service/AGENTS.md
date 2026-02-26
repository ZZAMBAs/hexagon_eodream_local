# Repository Guidelines

## Project Structure & Module Organization
This repository contains the `contract-service` Spring Boot application and depends on a sibling `../core` module declared in `settings.gradle`.
- Source: `src/main/java/com/example/contractservice`
- Tests: `src/test/java/com/example/contractservice`
- Config: `src/main/resources/application.yml`, `application-prod.yml`
- Build outputs: `build/`

Code is organized by domain modules: `contract`, `deposit`, `settlement`, with shared concerns under `common`. Keep controllers in `*/controller`, business logic in `*/service` or `*/application`, persistence in `*/repository`, and DTOs under `*/dto`.

## Build, Test, and Development Commands
Use the Gradle wrapper from this directory.
- `./gradlew clean build` (Windows: `gradlew.bat clean build`): compile, test, and package.
- `./gradlew test`: run JUnit 5 test suite only.
- `./gradlew bootRun`: start the service locally with default profile.
- `./gradlew bootJar`: build runnable jar in `build/libs/`.

If build fails due to missing `:core`, ensure the sibling `core` project is checked out at `../core`.

## Coding Style & Naming Conventions
- Java 17, 4-space indentation, UTF-8 source files.
- Follow standard Spring naming:
  - Classes: `PascalCase` (`ContractService`)
  - Methods/fields: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- Keep package names lowercase and feature-scoped (`contract.service.mapper`).
- Prefer constructor injection and immutable DTOs where practical.

## Testing Guidelines
- Frameworks: JUnit 5, Spring Boot Test, Mockito (`@MockitoBean`).
- Test class naming: `<TargetClass>Test` (for example, `SettlementServiceTest`).
- Test method naming: descriptive behavior-style names (existing tests use `success_*` patterns and `@DisplayName`).
- Run `./gradlew test` before opening a PR. No strict coverage threshold is configured; add tests for each behavior change and regression fix.

## Commit & Pull Request Guidelines
Recent history follows Conventional Commit style (`feat(...)`, `fix(...)`) with optional issue references like `(#238)`. Use:
- `type(scope): short imperative summary`

PRs should include:
- What changed and why
- Linked issue/ticket
- Test evidence (command + result)
- API contract changes (request/response examples) when relevant
