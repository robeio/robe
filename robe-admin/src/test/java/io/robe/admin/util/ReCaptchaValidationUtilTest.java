package io.robe.admin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.robe.admin.recaptcha.ReCaptchaResponseBody;
import io.robe.admin.recaptcha.ReCaptchaValidationUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hasanmumin on 22/12/2016.
 */
public class ReCaptchaValidationUtilTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void validate() throws IOException {
        ReCaptchaResponseBody response = ReCaptchaValidationUtil.validate("03AHJ_Vusvlzf3FY4ILSvCO_cFKqsC3G4iX4aIrXpu2wydO9P5oO0Jryf8Stwccgu_nLvI1Dnnx2PxpFkYDATnhEIVCU44ZmtYkTOE0_U9ALQFtBX32dunF1VgUd1mqEwsERSqhXH6SbG1o4R_hxzuWM2Bm_tFSrDvoUsJieVLqmQNPG7JkHq0g1RgW8L_gGmB1J2ZZtLDWhn0tZa67Qrw");
        assert !response.isSuccess();
    }
}
