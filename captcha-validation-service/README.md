# captcha-validation-service

This service is used to validate the Captcha in the Login flow.

It supports factory pattern implementation of captcha providers. Any CaptchaProvider interface implementation can be added to the classpath and utilised.



# Supported captcha validators

- Google Recaptcha

# Configurations

Below is the config key name format to provide secret key for any module for each captcha provider.

`mosip.captcha.<provider>.secret.<modulename>=secret-key-generated-for-the-module`

NOTE: Provider and Module name shouldn't contain any `-` or `_` .

Captcha verify for each provider can be defined as follows.
`mosip.captcha.<provider>.verify-url=captcha-verify-url`

Captcha provider for each module can be configured as below.
`mosip.captcha.module.provider.mapping.<modulename>=ABCCatchaProvider`


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
