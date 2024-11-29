# captcha
A single place to have all captcha needs across your service. 

## Build & run (for developers)
The project requires JDK 21.0.3
and mvn version - 3.9.6
1. Build and install:
    ```
     $ mvn install -Dgpg.skip=true
    ```
2.  Build Docker for a service:
    ```
    $ cd <service folder>
    $ docker build -f Dockerfile
    ```
3. Configure module wise secrets in the [application-default.properties](captcha-validation-service/src/main/resources/application-default.properties)
4. Run [CaptchaServiceApplication.java](captcha-validation-service/src/main/java/io/mosip/captcha/CaptchaServiceApplication.java) from IDE.
5. Service should be accessible at http://localhost:9089/v1/captcha

## Configuration
Captcha Validation Service uses the following configuration files that are accessible in this [repository](https://github.com/mosip/mosip-config/tree/master).
Please refer to the required released tagged version for configuration.
1. [Configuration-Captcha](https://github.com/mosip/mosip-config/blob/master/captcha-default.properties)

## Deploy
To deploy captcha-service on Kubernetes cluster using Dockers refer to [Sandbox Deployment](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## APIs
API documentation is available [here](https://mosip.github.io/documentation/1.2.0/1.2.0.html).

## License
This project is licensed under the terms of [Mozilla Public License 2.0](LICENSE).

**Note**: Refer [README.md](captcha-validation-service/README.md) for more details.
