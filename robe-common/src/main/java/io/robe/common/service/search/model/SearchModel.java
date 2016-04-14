package io.robe.common.service.search.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;

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
    public void addFiler(String field, String operator, String value) {
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
}
