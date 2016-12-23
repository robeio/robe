package io.robe.admin.recaptcha;

/**
 * Created by hasanmumin on 23/12/2016.
 */
public class ReCaptchaConfiguration {
    private String verifyUrl;
    private String secret;

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
