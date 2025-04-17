package io.mosip.captcha.util;

import io.mosip.captcha.spi.CaptchaProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "mosip.captcha")
public class CaptchaProviderFactory {

    @Autowired
    private List<CaptchaProvider> providers;

    @Getter
    @Setter
    private Map<String, String> moduleProviderMapping;

    public CaptchaProvider getCaptchaProvider(String moduleName) {
        String providerName = moduleProviderMapping.getOrDefault(moduleName, moduleProviderMapping.get("default"));
        return providers.stream()
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(providerName))
                .findFirst().orElseThrow();
    }

}
