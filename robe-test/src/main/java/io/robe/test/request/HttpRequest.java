package io.robe.test.request;

import java.io.IOException;

/**
 * Created by adem on 06/10/2016.
 */
public interface HttpRequest {

    TestResponse post(TestRequest request) throws IOException;
    TestResponse get(TestRequest request) throws IOException;
    TestResponse put(TestRequest request) throws IOException;
    TestResponse delete(TestRequest request) throws IOException;
    TestResponse patch(TestRequest request) throws IOException;

}
