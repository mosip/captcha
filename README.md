# MOSIP Captcha Services

[![Maven Package upon a push](https://github.com/mosip/captcha/actions/workflows/push-trigger.yml/badge.svg?branch=master)](https://github.com/mosip/captcha/actions/workflows/push-trigger.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?branch=master)](https://sonarcloud.io/summary/overall?id=mosip_captcha&branch=master)

This repository provides services for generating and validating CAPTCHA challenges within the MOSIP platform. The Captcha module supports automated bot protection for APIs such as send-OTP and credential validation, ensuring secure user interactions.

## Overview

The Captcha project includes one primary service:
- **captcha-validation-service** — handles captcha generation, validation APIs, and integration with MOSIP authentication flows.

For full details on building, configuring, and using this service [check here.](captcha-validation-service/README.md)

## Contribution & Community

- To learn how you can contribute code to this application, [click here](https://docs.mosip.io/1.2.0/community/code-contributions).
- If you have questions or encounter issues, visit the [MOSIP Community](https://community.mosip.io/) for support.
- For any GitHub issues: [Report here](https://github.com/mosip/captcha/issues)

## License

This project is licensed under the [Mozilla Public License 2.0](LICENSE).