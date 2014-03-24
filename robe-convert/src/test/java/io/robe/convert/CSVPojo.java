package io.robe.convert;

import java.math.BigDecimal;

public class CSVPojo {
    @MappingProperty(order = 0, optional = false, unique = true, name = "id")
    private int id;
    @MappingProperty(order = 1)
    private String name;
    @MappingProperty(order = 2)
    private String surname;
    @MappingProperty(order = 4)
    private long longid;
    @MappingProperty(order = 3)
    private double doubleid;
    @MappingProperty(order = 5)
    private BigDecimal big = BigDecimal.ONE;

    public CSVPojo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getLongid() {
        return longid;
    }

    public void setLongid(long longid) {
        this.longid = longid;
    }

    public double getDoubleid() {
        return doubleid;
    }

    public void setDoubleid(double doubleid) {
        this.doubleid = doubleid;
    }

    public BigDecimal getBig() {
        return big;
    }

    public void setBig(BigDecimal big) {
        this.big = big;
    }
}