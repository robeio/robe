package io.robe.common.service.search.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * A data class for holding search query parameters as a auto
 * filled easy to access data structure.
 */
public class SearchModel {
    /**
     * Full text search parameter
     */
    private String q;

    /**
     * Starting index for the paged fetches.
     */
    private Integer offset;
    /**
     * Maximum number of results per page.
     */
    private Integer limit;

    /**
     * on response fields
     */

    private String[] fields;

    /**
     * Fieds for sorting + for asc and - for desc ordering followed  by field name Forex. +fieldName
     */
    private String[] sort;
    private String filter;
    private long totalCount;
    private HttpServletResponse response;

    public SearchModel() {
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @JsonIgnore
    public void addFilter(String field, String operator, String value) {
        if (this.filter == null || this.filter.isEmpty()) {
            this.filter = field + operator + value;
        } else {
            String[] fields = this.filter.split(",");

            boolean find = false;
            for (int i = 0; i < fields.length; i++) {
                String f = fields[i];
                if (f.contains(field)) {
                    fields[i] = field + operator + value;
                    find = true;
                    break;
                }
            }
            if (find) {
                this.filter = StringUtils.join(fields, ",");
            } else {
                this.filter = this.filter + "," + field + operator + value;
            }

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchModel model = (SearchModel) o;

        if (totalCount != model.totalCount) return false;
        if (q != null ? !q.equals(model.q) : model.q != null) return false;
        if (offset != null ? !offset.equals(model.offset) : model.offset != null) return false;
        if (limit != null ? !limit.equals(model.limit) : model.limit != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(fields, model.fields)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(sort, model.sort)) return false;
        return filter != null ? filter.equals(model.filter) : model.filter == null;

    }

    @Override
    public int hashCode() {
        int result = q != null ? q.hashCode() : 0;
        result = 31 * result + (offset != null ? offset.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(fields);
        result = 31 * result + Arrays.hashCode(sort);
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + (int) (totalCount ^ (totalCount >>> 32));
        return result;
    }

    @JsonIgnore
    public void addSort(String field, String operator) {
        if (this.sort == null) {
            this.sort = new String[1];
            this.sort[0] = operator + field;
        } else {
            boolean find = false;
            for (int i = 0; i < this.sort.length; i++) {
                String f = this.sort[i];
                if (f.contains(field)) {
                    this.sort[i] = field + operator;
                    find = true;
                    break;
                }
            }
            if (!find) {
                String[] array = new String[this.sort.length + 1];
                System.arraycopy(this.sort, 0, array, 0, this.sort.length);
                array[this.sort.length + 1] = operator + field;
                this.sort = array;
            }

        }


    }
}
