# captcha-validation-service

This service is used to validate the Captcha in the Login flow.

# Supported captcha validators

- Google Recaptcha

# Configurations

1. Google Recaptcha

Below is the config key name format to provide secret key for any modules.

`mosip.captcha.secret.<module-name>=secret-key-generated-for-the-module`

Eg: mosip.captcha.secret.preregistration=secret-key-generated-for-the-preregistration-module


# Example

Request:

```
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

Response:

```
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
