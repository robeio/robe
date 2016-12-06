package io.robe.hibernate.criteria.api;

/**
 * Created by kamilbukum on 24/11/16.
 */
public class ResultPair <L, C> {
    private L left;
    private C right;

    public ResultPair() {
    }

    public ResultPair(L left) {
        this.left = left;
    }

    public ResultPair(L left, C right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public C getRight() {
        return right;
    }

    public void setRight(C right) {
        this.right = right;
    }
}
