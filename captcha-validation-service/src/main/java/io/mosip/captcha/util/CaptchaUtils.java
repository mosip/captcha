/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.util;

import io.mosip.kernel.core.util.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CaptchaUtils {

    public static String getCurrentResponseTime() {
        return DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC));
    }
}
