package io.robe.convert;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SamplePojo {
    @MappingProperty(order = 0, unique = true, name = "Kullanıcı Id", columnWidth = 12)
    private int id;
    @MappingProperty(order = 0, hidden = false, columnWidth = 10)
    private String name;
    @MappingProperty(order = 0, name = "Soyad")
    private String surname;
    @MappingProperty(order = 0, name = "Long Id")
    private long longid;
    @MappingProperty(order = 0, name = "Double Id")
    private double doubleid;

    @MappingProperty(order = 0)
    private BigDecimal big = BigDecimal.ONE;

    @MappingProperty(order = 0, name = "Date Format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "CET")
    private Date date2;

    public SamplePojo() {

    }

    public SamplePojo(int id, String name, String surname, long longid, double doubleid, BigDecimal big, Date date2) {
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

    @Override
    public String toString() {
        return "SamplePojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", longid=" + longid +
                ", doubleid=" + doubleid +
                ", big=" + big +
                ", date2=" + date2 +
                '}';
    }
}