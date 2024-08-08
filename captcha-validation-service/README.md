# captcha-validation-service

This service is used to validate the Captcha in the Login flow.

# Supported captcha validators

- Google Recaptcha

# Configurations

1. Google Recaptcha

Below is a map with list of modules with their respective secret keys to validate the captcha tokens

`mosip.captcha.secret-key={ 'preregistration' : 'secret key generated for preregistration domain' }`


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

Based on the module name, the respective secret is fetched from `mosip.captcha.secret-key` and used to validate the provided captcha token.

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
