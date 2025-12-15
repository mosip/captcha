# MOSIP Captcha Services
[![Maven Package upon a push](https://github.com/mosip/captcha/actions/workflows/push-trigger.yml/badge.svg?branch=master)](https://github.com/mosip/captcha/actions/workflows/push-trigger.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=develop&project=mosip_captcha&id=mosip_packet-manager2&metric=alert_status)](https://sonarcloud.io/summary/overall?id=mosip_captcha&branch=develop)

## Overview

The **Captcha Services** module provides functionality to validate Captchas, primarily used in flow such as Login.

It supports a factory pattern implementation of captcha providers, allowing any `CaptchaProvider` interface implementation to be added to the classpath and utilized.

## Services

The Captcha module contains the following:

1. **[Captcha Validation Service](captcha-validation-service/README.md)** - Service for validating captchas.

## Local Setup

The project can be set up in two ways:

1. [Local Setup (for Development or Contribution)](#local-setup-for-development-or-contribution)
2. [Local Setup with Docker (Easy Setup for Demos)](#local-setup-with-docker-easy-setup-for-demos)

### Prerequisites

- **JDK**: 21.0.3
- **Maven**: 3.9.6
- **Docker**: Latest stable version

### Configuration

- Check the captcha properties: [mosip-config](https://github.com/mosip/mosip-config/tree/master).

## Installation

### Local Setup (for Development or Contribution)

1. Clone the repository:

```text
git clone <repo-url>
cd captcha
```

2. Build the project:

```text
mvn clean install -Dgpg.skip=true
```

3. Start the application:
    - Run `CaptchaServiceApplication.java` from your IDE.
    - Or run the jar from the target directory:
      ```text
      java -jar captcha-validation-service/target/captcha-validation-service-*.jar
      ```

4. Verify the service is accessible at: `http://localhost:9089/v1/captcha`

### Local Setup with Docker (Easy Setup for Demos)

1. Build Docker image:

```text
cd captcha-validation-service
docker build -f Dockerfile .
```

2. Run the service using Docker. Ensure you have configured the module-wise secrets in `application-default.properties` or passed them as environment variables.

## Deployment

### Kubernetes

To deploy captcha-service on a Kubernetes cluster, refer to the [Sandbox Deployment Guide](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## Documentation

- **API Documentation**: Available [here](https://mosip.github.io/documentation/1.2.0/1.2.0.html).
- **Service Documentation**: Refer to the [Captcha Validation Service README](captcha-validation-service/README.md) for more details.

## Contribution & Community

- To learn how you can contribute code to this application, [click here](https://docs.mosip.io/1.2.0/community/code-contributions).
- If you have questions or encounter issues, visit the [MOSIP Community](https://community.mosip.io/) for support.
- For any GitHub issues: [Report here](https://github.com/mosip/captcha/issues)

## License

This project is licensed under the [Mozilla Public License 2.0](LICENSE).
