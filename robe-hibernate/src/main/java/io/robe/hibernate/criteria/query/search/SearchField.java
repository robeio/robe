package io.robe.hibernate.criteria.query.search;

import java.util.Map;

/**
 * Created by kamilbukum on 27/01/2017.
 */
public class SearchField {
    private Map<String, SearchField> searchFieldMap;
    private final String name;
    public SearchField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, SearchField> getSearchFieldMap() {
        return searchFieldMap;
    }

    public void setSearchFieldMap(Map<String, SearchField> searchFieldMap) {
        this.searchFieldMap = searchFieldMap;
    }
}
