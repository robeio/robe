package io.robe.hibernate.transaction;

@FunctionalInterface
public interface TransactionWrapper {

    void wrap();

}
