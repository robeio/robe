package io.robe.common.service.headers;

import io.robe.common.service.search.model.SearchModel;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class ResponseHeadersUtilTest {
    SearchModel model;
    HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        model = new SearchModel();
        response = new HttpServletResponse() {
            Map<String, String> map = new HashMap<String, String>();

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {
                map.put(s, s1);

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public String getHeader(String s) {
                return map.get(s);
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }
        };
        model.setResponse(response);

    }

    @Test
    public void addTotalCount() throws Exception {
        ResponseHeadersUtil.addTotalCount(model);
        assertEquals(model.getTotalCount() + "", model.getResponse().getHeader("X-Total-Count"));

    }

    @Test
    public void addLocation() throws Exception {
        ResponseHeadersUtil.addLocation(response, "Location");
        assertEquals(response.getHeader("Location"), "Location");

    }
}
