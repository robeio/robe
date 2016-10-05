package io.robe.hibernate.transaction;

@FunctionalInterface
public interface TransactionExceptionHandler {

    void onException(Exception exception);

}
