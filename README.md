# captcha
A single place to have all captcha needs across your service. 

## Build & run (for developers)
The project requires JDK 1.21.
and mvn version - 3.9.6
1. Build and install:
    ```
     $ mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
    ```
2. Configure module wise secrets in the [application-default.properties](captcha-validation-service/src/main/resources/application-default.properties)
3. Run [CaptchaServiceApplication.java](captcha-validation-service/src/main/java/io/mosip/captcha/CaptchaServiceApplication.java) from IDE.
4. Service should be accessible at http://localhost:9089/v1/captcha


**Note**: Refer [README.md](captcha-validation-service/README.md) for more details.
