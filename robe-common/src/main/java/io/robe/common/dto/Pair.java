package io.robe.common.dto;

/**
 *
 * @param <L>
 * @param <C>
 */
public class Pair<L, C> {
    private L left;
    private C right;

    public Pair() {
    }

    public Pair(L left) {
        this.left = left;
    }

    public Pair(L left, C right) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> that = (Pair<?, ?>) o;

        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        return right != null ? right.equals(that.right) : that.right == null;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        String result = "=";

        if(left != null) {
           result = left + result;
        }
        if(right != null) {
            result = result + right;
        }
        return result;
    }
}
