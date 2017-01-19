package io.robe.hibernate.query.api.criteria;


import java.util.List;

/**
 * Created by kamilbukum on 11/01/2017.
 */
public class Result<E> {
    private List<E> list;
    private Long totalCount;

    public Result() {

    }

    public Result(List<E> list) {
        this(list, (long)(list != null ? list.size(): 0));
    }

    public Result(List<E> list, Long totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;

        Result<?> that = (Result<?>) o;

        if (getList() != null ? !getList().equals(that.getList()) : that.getList() != null) return false;
        return getTotalCount() != null ? getTotalCount().equals(that.getTotalCount()) : that.getTotalCount() == null;
    }

    @Override
    public int hashCode() {
        int result = getList() != null ? getList().hashCode() : 0;
        result = 31 * result + (getTotalCount() != null ? getTotalCount().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " Result => {" +
                "list=" + list +
                ", totalCount=" + totalCount +
                '}';
    }
}
