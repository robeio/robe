package io.robe.hibernate.transaction;


import io.robe.hibernate.RobeHibernateBundle;
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
 * <b>Example usages</b> <br/>
 * <pre>
 * {@code
 *
 * Transaction.exec(() -> {
 *     dao.create(entity);
 * });
 *
 * Transaction.exec(() -> {
 *      dao.create(entity);
 *      Transaction.exec(() -> {
 *          anotherDao.create(anotherEntity);
 *      }, FlushMode.COMMIT);
 * });
 *
 * Transaction.exec(() -> {
 *   dao.create(entity);
 * }, (exception) -> {
 *      logger.log(exception);
 *      throw exception;
 *   });
 * }
 * </pre>
 */
public class Transaction {

    private static final SessionFactory SESSION_FACTORY = RobeHibernateBundle.getInstance().getSessionFactory();

    private static ThreadLocal<Stack<Session>> SESSIONS = new ThreadLocal<Stack<Session>>() {
        protected Stack<Session> initialValue() {
            return new Stack<>();
        }
    };


    private Session session;
    private TransactionWrapper transactionWrapper;
    private TransactionExceptionHandler exceptionHandler;
    private FlushMode flushMode;

    /**
     * Private constructor
     *
     * @param transactionWrapper {@link TransactionWrapper}
     * @param exceptionHandler   {@link TransactionExceptionHandler}
     * @param flushMode          {@link FlushMode}
     */
    private Transaction(TransactionWrapper transactionWrapper, TransactionExceptionHandler exceptionHandler, FlushMode flushMode) {
        this.transactionWrapper = transactionWrapper;
        this.exceptionHandler = exceptionHandler;
        this.flushMode = flushMode;
        storePreviousSession();
        configureNewSession();
        start();
    }

    /**
     * Begins new transaction in a new session and performs operations provided in {@link TransactionWrapper}
     *
     * @param transactionWrapper {@link TransactionWrapper}
     */
    public static void exec(TransactionWrapper transactionWrapper) {
        exec(transactionWrapper, null, FlushMode.AUTO);
    }

    /**
     * Begins new transaction in a new session and performs operations provided in {@link TransactionWrapper} <br/>
     * In case of an exception is thrown the {@link TransactionExceptionHandler} will be invoked.
     *
     * @param transactionWrapper {@link TransactionWrapper}
     * @param exceptionHandler   {@link TransactionExceptionHandler}
     */
    public static void exec(TransactionWrapper transactionWrapper, TransactionExceptionHandler exceptionHandler) {
        exec(transactionWrapper, exceptionHandler, FlushMode.AUTO);
    }

    /**
     * Begins new transaction in a new session and performs operations provided in {@link TransactionWrapper}
     *
     * @param transactionWrapper {@link TransactionWrapper}
     * @param flushMode          {@link FlushMode}
     */
    public static void exec(TransactionWrapper transactionWrapper, FlushMode flushMode) {
        exec(transactionWrapper, null, flushMode);
    }

    /**
     * Begins new transaction in a new session and performs operations provided in {@link TransactionWrapper} <br/>
     * In case of an exception is thrown the {@link TransactionExceptionHandler} will be invoked.
     *
     * @param transactionWrapper {@link TransactionWrapper}
     * @param exceptionHandler   {@link TransactionExceptionHandler}
     * @param flushMode          {@link FlushMode}
     */
    public static void exec(TransactionWrapper transactionWrapper, TransactionExceptionHandler exceptionHandler, FlushMode flushMode) {
        checkNotNull(transactionWrapper);
        new Transaction(transactionWrapper, exceptionHandler, flushMode);
    }

    /**
     * If present, backup current the session in {@link ManagedSessionContext} and unbinds it.
     */
    private void storePreviousSession() {
        if (ManagedSessionContext.hasBind(SESSION_FACTORY)) {
            SESSIONS.get().add(SESSION_FACTORY.getCurrentSession());
            ManagedSessionContext.unbind(SESSION_FACTORY);
        }
    }

    /**
     * Opens a new session, sets flush mode and bind this session to {@link ManagedSessionContext}
     */
    private void configureNewSession() {
        session = SESSION_FACTORY.openSession();
        session.setFlushMode(flushMode);
        ManagedSessionContext.bind(session);
    }

    /**
     * Start transaction
     */
    private void before() {
        session.beginTransaction();
    }

    /**
     * If transaction is present and active then commit.
     */
    private void success() {
        org.hibernate.Transaction txn = session.getTransaction();
        if (txn != null && txn.getStatus().equals(TransactionStatus.ACTIVE)) {
            txn.commit();
        }
    }

    /**
     * If transaction is present and active then rollback.
     */
    private void error() {
        org.hibernate.Transaction txn = session.getTransaction();
        if (txn != null && txn.getStatus().equals(TransactionStatus.ACTIVE)) {
            txn.rollback();
        }
    }

    /**
     * Closes and unbinds the current session. <br/>
     * If {@link ManagedSessionContext} had a session before the current session, re-binds it to {@link ManagedSessionContext}
     */
    private void finish() {
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

    /**
     * Starts the progress.
     */
    private void start() {
        try {
            before();
            transactionWrapper.wrap();
            success();
        } catch (Exception e) {
            error();
            if (exceptionHandler != null) {
                exceptionHandler.onException(e);
            } else {
                throw e;
            }
        } finally {
            finish();
        }
    }

}