package io.mosip.captcha.utils;

import io.mosip.kernel.core.util.DateUtils;
import java.util.Date;

public class CaptchaUtils {
    public static String getCurrentResponseTime() {
        String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        return DateUtils.formatDate(new Date(System.currentTimeMillis()), dateTimeFormat);
    }
}
