# AGENTS.md

This file provides guidance to AI agents when working with code in this repository.

## Project Overview

MOSIP Captcha Services — a Spring Boot microservice for validating CAPTCHA challenges (primarily Google reCAPTCHA v2) within the MOSIP platform. Used by Pre-registration and Resident Services for bot protection during login and appointment flows.

## Build & Run

```bash
# Build (skip GPG signing required for local builds)
mvn clean install -Dgpg.skip=true

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=CaptchaServiceImplTest -pl captcha-validation-service

# Run the application (after building)
java -jar captcha-validation-service/target/captcha-validation-service-*.jar

# Docker build (run from captcha-validation-service/)
docker build -f Dockerfile .
```

The service runs on port `9089` with context path `/v1/captcha`. The validation endpoint is `POST /v1/captcha/validatecaptcha`.

## Architecture

The service uses a **factory + SPI pattern** for captcha providers:

- `CaptchaProvider` (SPI interface) — any new provider implements `getProviderName()` and `verifyCaptcha(moduleName, token)`
- `CaptchaProviderFactory` — resolves the correct `CaptchaProvider` bean for a given module name via config-driven mapping (`mosip.captcha.module.provider.mapping.<modulename>`)
- `CaptchaServiceImpl` — delegates to the factory, wraps the response in MOSIP's `ResponseWrapper`
- `CaptchaController` — single `POST /validatecaptcha` endpoint accepting `RequestWrapper<CaptchaRequestDTO>`

All responses (including errors) use MOSIP's envelope format with `id`, `version`, `responsetime`, `response`, and `errors` fields. Exceptions are handled globally in `CaptchaExceptionHandler`.

## Configuration

Configuration is loaded from **Spring Cloud Config** (MOSIP's centralized config server). The `bootstrap.properties` sets `spring.cloud.config.name=captcha` and connects to the config server at runtime.

For local development, override properties in `application-default.properties`:

```properties
# Map module names to provider names
mosip.captcha.module.provider.mapping.default=GoogleReCaptchaV2
mosip.captcha.module.provider.mapping.preregistration=GoogleReCaptchaV2

# Provider secrets — one per module per provider
# Format: mosip.captcha.<provider>.secret.<modulename>=<secret>
mosip.captcha.googlerecaptchav2.secret.preregistration=<your-secret>

# Provider verification URL
mosip.captcha.googlerecaptchav2.verify-url=https://www.google.com/recaptcha/api/siteverify
```

**Important naming constraint**: provider names and module names must not contain `-` or `_`.

## Adding a New Captcha Provider

1. Create a class implementing `io.mosip.captcha.spi.CaptchaProvider`
2. Annotate it with `@Component` and `@ConfigurationProperties(prefix = "mosip.captcha.<providername>")`
3. Implement `getProviderName()` returning the provider's string identifier
4. Add secrets config: `mosip.captcha.<providername>.secret.<modulename>=<secret>`
5. Map modules to the new provider: `mosip.captcha.module.provider.mapping.<modulename>=<ProviderName>`

The factory auto-discovers all `CaptchaProvider` beans via Spring's `@Autowired List<CaptchaProvider>`.

## Helm / Kubernetes

Helm chart is under `helm/captcha/`. CI publishes to `mosip/mosip-helm` (gh-pages). Deployment docs: https://docs.mosip.io/1.2.0/deploymentnew/v3-installation

## Key Config Properties Reference

| Property | Purpose |
|---|---|
| `mosip.captcha.api.id` | API ID in response envelope |
| `mosip.captcha.api.version` | API version in response envelope |
| `mosip.captcha.default.module-name` | Fallback module name when not provided in request |
| `mosip.captcha.module.provider.mapping.<module>` | Maps a module name to a provider name |
| `mosip.captcha.<provider>.secret.<module>` | Per-module secret for a provider |
| `mosip.captcha.<provider>.verify-url` | Verification URL for a provider |