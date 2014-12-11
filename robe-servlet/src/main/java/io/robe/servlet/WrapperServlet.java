package io.robe.servlet;

import io.dropwizard.hibernate.UnitOfWork;
import io.robe.guice.GuiceBundle;
import org.hibernate.*;
import org.hibernate.context.internal.ManagedSessionContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * A Wrapper servlet class to manage method calls, session, injection and transactions.
 * Uses standard Servlet methods and forwards these calls to the {@link ResourceServlet} implementation.
 * {@link io.dropwizard.hibernate.UnitOfWork} annotation properties works exactly the same with Dropwizard Resources methods.
 */
public class WrapperServlet extends HttpServlet {

    private final Class<ResourceServlet> servletClass;
    private ResourceServlet singleton;
    private final Method doGet;
    private final UnitOfWork uowDoGet;
    private final Method doPut;
    private final UnitOfWork uowDoPut;
    private final Method doPost;
    private final UnitOfWork uowDoPost;
    private final Method doDelete;
    private final UnitOfWork uowDoDelete;
    private final Method doOptions;
    private final UnitOfWork uowDoOptions;
    private final Method doHead;
    private final UnitOfWork uowDoHead;
    private final Method doTrace;
    private final UnitOfWork uowDoTrace;

    /**
     * Contstructor to get
     *
     * @param servletClass {@link ResourceServlet} implementation class and cache all methods and
     *                     {@link io.dropwizard.hibernate.UnitOfWork} annotations of methods.
     * @throws NoSuchMethodException
     */
    public WrapperServlet(Class<ResourceServlet> servletClass, boolean singleInstance) throws NoSuchMethodException {
        this.servletClass = servletClass;
        this.singleton = null;
        try {
            if (singleInstance)
                singleton = GuiceBundle.getInjector().getInstance(servletClass);
            doGet = servletClass.getMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
            doPut = servletClass.getMethod("doPut", HttpServletRequest.class, HttpServletResponse.class);
            doPost = servletClass.getMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
            doDelete = servletClass.getMethod("doDelete", HttpServletRequest.class, HttpServletResponse.class);
            doOptions = servletClass.getMethod("doOptions", HttpServletRequest.class, HttpServletResponse.class);
            doHead = servletClass.getMethod("doHead", HttpServletRequest.class, HttpServletResponse.class);
            doTrace = servletClass.getMethod("doTrace", HttpServletRequest.class, HttpServletResponse.class);

            uowDoGet = doGet.getAnnotation(UnitOfWork.class);
            uowDoPut = doPut.getAnnotation(UnitOfWork.class);
            uowDoPost = doPost.getAnnotation(UnitOfWork.class);
            uowDoDelete = doDelete.getAnnotation(UnitOfWork.class);
            uowDoOptions = doOptions.getAnnotation(UnitOfWork.class);
            uowDoHead = doHead.getAnnotation(UnitOfWork.class);
            uowDoTrace = doTrace.getAnnotation(UnitOfWork.class);
        } catch (NoSuchMethodException e) {
            throw e; // Never
        }

    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doGet, uowDoGet, req, resp);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doPut, uowDoPut, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doPost, uowDoPost, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doDelete, uowDoDelete, req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doOptions, uowDoOptions, req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doHead, uowDoHead, req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(doTrace, uowDoTrace, req, resp);
    }

    /**
     * Executes the desired method call with a new instance of the {@link ResourceServlet} implementation.
     * Transaction management is done here.
     *
     * @param call
     * @param uow
     * @param req
     * @param resp
     */
    private final void dispatch(Method call, UnitOfWork uow, HttpServletRequest req, HttpServletResponse resp) {
        if (uow == null) {
            try {
                ResourceServlet servlet = GuiceBundle.getInjector().getInstance(servletClass);
                call.invoke(servlet, req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            SessionFactory sessionFactory = GuiceBundle.getInjector().getInstance(SessionFactory.class);
            Session session = sessionFactory.openSession();
            try {
                configureSession(session, uow);
                ManagedSessionContext.bind(session);
                beginTransaction(session, uow);
                try {
                    ResourceServlet servlet = (singleton == null) ? singleton : GuiceBundle.getInjector().getInstance(servletClass);
                    call.invoke(servlet, req, resp);
                    commitTransaction(session, uow);
                } catch (Exception e) {
                    rollbackTransaction(session, uow);
                    throw new RuntimeException(e);
                }
            } finally {
                session.close();
                ManagedSessionContext.unbind(sessionFactory);
            }
        }
    }

    private void beginTransaction(Session session, UnitOfWork uow) {
        if (uow.transactional()) {
            session.beginTransaction();
        }

    }

    private void configureSession(Session session, UnitOfWork uow) {
        session.setDefaultReadOnly(uow.readOnly());
        session.setCacheMode(uow.cacheMode());
        session.setFlushMode(uow.flushMode());
    }

    private void rollbackTransaction(Session session, UnitOfWork uow) {
        if (uow.transactional()) {
            Transaction txn = session.getTransaction();
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }

    }

    private void commitTransaction(Session session, UnitOfWork uow) {
        if (uow.transactional()) {
            Transaction txn = session.getTransaction();
            if (txn != null && txn.isActive()) {
                txn.commit();
            }
        }

    }
}
