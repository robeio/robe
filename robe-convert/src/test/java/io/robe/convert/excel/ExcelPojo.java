package io.robe.convert.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.robe.convert.MappingProperty;
//import io.robe.convert.SimpleDateFormat;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelPojo {
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

    @MappingProperty(order = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "CET")
    private Date date2 = new Date();

    public ExcelPojo() {

    }

    public ExcelPojo(int id, String name, String surname, long longid, double doubleid, BigDecimal big, Date date2) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.longid = longid;
        this.doubleid = doubleid;
        this.big = big;
        this.date2 = date2;
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

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }
}
