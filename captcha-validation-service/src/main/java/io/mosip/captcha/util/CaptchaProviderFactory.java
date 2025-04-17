/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
@ConfigurationProperties(prefix = "mosip.captcha.module.provider")
public class CaptchaProviderFactory {

    @Autowired
    private List<CaptchaProvider> providers;

    @Getter
    @Setter
    private Map<String, String> mapping;

    public CaptchaProvider getCaptchaProvider(String moduleName) {
        String providerName = mapping.getOrDefault(moduleName, mapping.get("default"));
        return providers.stream()
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(providerName))
                .findFirst().orElseThrow();
    }

}
