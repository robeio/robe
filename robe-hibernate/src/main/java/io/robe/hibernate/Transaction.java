package io.robe.hibernate;


import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides a new {@link Session} and run {@link TransactionWrapper} in this session scope.
 * So any transaction done here does not affect the transaction of session provided by {@link io.dropwizard.hibernate.UnitOfWork}
 * and the session that provided by this class in another session scope unless you catch the exceptions. <br/>
 * <b>Example usage</b> <br/>
 * <pre>
 * {@code
 * Transaction.run(() -> {
 *     dao.create(entity);
 *     Transaction.run(() -> {
 *         anotherDao.create(anotherEntity);
 *     });
 * });
 * }
 * </pre>
 */
public class Transaction {

    @FunctionalInterface
    public interface TransactionWrapper {
        void wrap();
    }

    private static final SessionFactory SESSION_FACTORY = RobeHibernateBundle.getInstance().getSessionFactory();

    private static ThreadLocal<Stack<Session>> SESSIONS = new ThreadLocal<Stack<Session>>() {
        protected Stack<Session> initialValue() {
            return new Stack<>();
        }
    };


    private Session session;
    private TransactionWrapper transactionWrapper;
    private boolean readOnly;
    private boolean transactional;
    private CacheMode cacheMode;
    private FlushMode flushMode;

    private Transaction(TransactionWrapper transactionWrapper, boolean readOnly, boolean transactional, CacheMode cacheMode, FlushMode flushMode) {
        this.transactionWrapper = transactionWrapper;
        this.readOnly = readOnly;
        this.transactional = transactional;
        this.cacheMode = cacheMode;
        this.flushMode = flushMode;
        storePreviousSession();
        configureNewSession();
        start();
    }

    public static void run(TransactionWrapper tx) {
        checkNotNull(tx);
        run(tx, false, true, CacheMode.NORMAL, FlushMode.AUTO);
    }

    public static void run(TransactionWrapper transactionWrapper, boolean readOnly, boolean transactional, CacheMode cacheMode, FlushMode flushMode) {
        checkNotNull(transactionWrapper);
        checkNotNull(readOnly);
        checkNotNull(transactional);
        checkNotNull(cacheMode);
        checkNotNull(flushMode);
        new Transaction(transactionWrapper, readOnly, transactional, cacheMode, flushMode);
    }

    private void storePreviousSession() {
        if (ManagedSessionContext.hasBind(SESSION_FACTORY)) {
            SESSIONS.get().add(SESSION_FACTORY.getCurrentSession());
            ManagedSessionContext.unbind(SESSION_FACTORY);
        }
    }

    private void configureNewSession() {
        session = SESSION_FACTORY.openSession();
        session.setDefaultReadOnly(readOnly);
        session.setCacheMode(cacheMode);
        session.setFlushMode(flushMode);
        ManagedSessionContext.bind(session);
    }

    private void before() {
        if (transactional) {
            session.beginTransaction();
        }
    }

    private void success() {
        if (!transactional) {
            return;
        }
        org.hibernate.Transaction txn = session.getTransaction();
        if (txn != null && txn.getStatus().equals(TransactionStatus.ACTIVE)) {
            txn.commit();
        }
    }

    private void error() {
        if (!transactional) {
            return;
        }
        org.hibernate.Transaction txn = session.getTransaction();
        if (txn != null && txn.getStatus().equals(TransactionStatus.ACTIVE)) {
            txn.rollback();
        }
    }

    public void finish() {
        try {
            if (session != null) {
                session.close();
            }
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            if (!SESSIONS.get().isEmpty()) {
                ManagedSessionContext.bind(SESSIONS.get().pop());
            }
        }
    }

    private void start() {
        try {
            before();
            transactionWrapper.wrap();
            success();
        } catch (Exception e) {
            error();
            throw e;
        } finally {
            finish();
        }
    }

}