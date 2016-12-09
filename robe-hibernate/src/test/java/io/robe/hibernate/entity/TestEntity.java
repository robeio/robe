package io.robe.hibernate.entity;

import io.robe.common.service.search.SearchIgnore;
import io.robe.common.service.search.SearchableEnum;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class TestEntity extends BaseEntity {

    private String name;
    private Long time;
    private Integer count;
    private Boolean bool;
    private Date day;
    private Status status;

    @SearchIgnore
    private String ignore;




    enum Status implements SearchableEnum {
        ACTIVE,
        PASSIVE;

        @Override
        public String getText() {
            return this.name();
        }
    }
}
