# Captcha Validation Service

## Overview

The **Captcha Services** module provides functionality to validate Captchas, primarily used in flow such as Login.

It supports a factory pattern implementation of captcha providers, allowing any `CaptchaProvider` interface implementation to be added to the classpath and utilized.

## Features

- **Captcha Generation**: Generates secure captcha challenges.
- **Captcha Validation**: Validates user responses against generated captchas.
- **Provider Agnostic**: Supports multiple captcha providers (e.g., Google reCAPTCHA) via a factory pattern.
- **Configurable**: extensive configuration options for site keys, secret keys, and verification URLs.
- **Security**: Protects APIs against bot attacks and automated abuse.

## supported Captcha Validators

- Google Recaptcha

## Services using this Service

- **Pre-registration Service**: Uses captcha for user verification during appointment booking and data entry.
- **Resident Services**: formatting and validation during login flows.

## Services

The Captcha module contains the following:

1. **Captcha Validation Service** - Service for validating captchas.

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

Below is the config key name format to provide secret key for any module for each captcha provider.

`mosip.captcha.<provider>.secret.<modulename>=secret-key-generated-for-the-module`

NOTE: Provider and Module name shouldn't contain any `-` or `_` .

Captcha verify for each provider can be defined as follows.
`mosip.captcha.<provider>.verify-url=captcha-verify-url`

Captcha provider for each module can be configured as below.
`mosip.captcha.module.provider.mapping.<modulename>=ABCCatchaProvider`

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

## API Usage Example

**Request:**

```json
{
    "id": "mosip.captcha.id.validate",
    "version": "0.1",
    "requesttime": "2024-08-08T06:07:45.345Z",
    "request": {
        "captchaToken": "",
        "moduleName": ""
    }
}
```

Based on the module name, the respective secret is fetched from `mosip.captcha.secret.<module-name>` property and used to validate the provided captcha token.

**Response:**

```json
{
    "id": "mosip.captcha.id.validate",
    "version": "0.1",
    "responsetime": "2024-08-08T06:07:46.345Z",
    "response": {
        "success": true,
        "message": ""
    },
    "errors": [
        {
            "errorCode": "",
            "message": ""
        }
    ]
}
```

## Deployment

### Kubernetes

To deploy captcha-service on a Kubernetes cluster, refer to the [Sandbox Deployment Guide](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## Documentation

- **API Documentation**: Available [here](https://mosip.github.io/documentation/1.2.0/pre-registration-captcha-service.html).
- **Service Documentation**: Refer to the [Captcha Validation Service README](captcha-validation-service/README.md) for more details.

## Contribution & Community

- To learn how you can contribute code to this application, [click here](https://docs.mosip.io/1.2.0/community/code-contributions).
- If you have questions or encounter issues, visit the [MOSIP Community](https://community.mosip.io/) for support.
- For any GitHub issues: [Report here](https://github.com/mosip/captcha/issues)

## License

This project is licensed under the [Mozilla Public License 2.0](LICENSE).