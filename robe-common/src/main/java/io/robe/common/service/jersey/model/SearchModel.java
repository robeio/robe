package io.robe.common.service.jersey.model;

import java.util.List;

/**
 * A data class for holding search query parameters as a auto
 * filled easy to access data structure.
 */
public class SearchModel {
    /**
     * Full text search parameter
     */
    private List<String> q;

    /**
     * Starting index for the paged fetches.
     */
    private String offset;
    /**
     * Maximum number of results per page.
     */
    private String limit;

    /**
     * on response fields
     */

    private String fields;

    public SearchModel() {
    }

    public List<String> getQ() {
        return q;
    }

    public void setQ(List<String> q) {
        this.q = q;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
