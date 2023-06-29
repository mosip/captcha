package io.mosip.captcha.util;

import io.mosip.kernel.core.util.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CaptchaUtils {

    public static String getCurrentResponseTime() {
        return DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC));
    }
}
