package io.robe.admin.util;

import io.robe.admin.recaptcha.ReCaptchaConfiguration;
import io.robe.admin.recaptcha.ReCaptchaResponseBody;
import io.robe.admin.recaptcha.ReCaptchaValidation;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hasanmumin on 22/12/2016.
 */
public class ReCaptchaValidationTest {

    @Test
    public void validate() throws IOException {

        ReCaptchaConfiguration configuration = new ReCaptchaConfiguration();
        configuration.setVerifyUrl("https://www.google.com/recaptcha/api/siteverify");
        configuration.setSecret("6LckQA8TAAAAAAuwC602KfqYuxOCSiXBqFS3m6OO");

        new ReCaptchaValidation(configuration);

        ReCaptchaResponseBody response = ReCaptchaValidation.getInstance().validate("03AHJ_Vusvlzf3FY4ILSvCO_cFKqsC3G4iX4aIrXpu2wydO9P5oO0Jryf8Stwccgu_nLvI1Dnnx2PxpFkYDATnhEIVCU44ZmtYkTOE0_U9ALQFtBX32dunF1VgUd1mqEwsERSqhXH6SbG1o4R_hxzuWM2Bm_tFSrDvoUsJieVLqmQNPG7JkHq0g1RgW8L_gGmB1J2ZZtLDWhn0tZa67Qrw");
        assert !response.isSuccess();
    }
}
