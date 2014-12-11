package io.robe.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by serayuzgur on 09/12/14.
 */
public interface ResourceServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public void doHead(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    public void doTrace(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
