package io.robe.auth.tokenbased;

import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BasicTokenTest {

    @Before
    public void initialize(){

        TokenBasedAuthConfiguration  configuration = new TokenBasedAuthConfiguration(){
            @Override
            public int getPoolSize() {
                return 1;
            }
            @Override
            public String getServerPassword() {
                return "auto";
            }
            @Override
            public String getAlgorithm() {
                return "PBEWithMD5AndTripleDES";
            }

            @Override
            public int getMaxage() {
                return 3600 ;//sec;
            }
        };
        BasicToken.configure(configuration);
    }

    @Test
    public void tokenCreateTest() throws Exception {

        Map<String,String> attributes = new HashMap<>();
        attributes.put("userAgent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.1.25 (KHTML, like Gecko) Version/8.0 Safari/600.1.25");
        attributes.put("remoteAddr","192.168.1.6");
        BasicToken basicToken1 = new BasicToken("1","seray", DateTime.now(),attributes);
        String token = basicToken1.getTokenString();

        BasicToken basicToken2 = new BasicToken(token);

        assert basicToken1.equals(basicToken2);


    }

}