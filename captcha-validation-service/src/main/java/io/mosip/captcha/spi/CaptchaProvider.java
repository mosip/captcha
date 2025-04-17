/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.captcha.spi;

import io.mosip.captcha.dto.CaptchaResponseDTO;
import io.mosip.captcha.exception.CaptchaException;

public interface CaptchaProvider {

    String getProviderName();

    CaptchaResponseDTO verifyCaptcha(String moduleName, String captchaToken) throws CaptchaException;

}
