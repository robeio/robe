package io.robe.convert;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;

public class SamplePojo {
    @MappingProperty(order = 0, unique = true, name = "Kullanıcı Id")
    private int id;
    @MappingProperty(order = 1, optional = true)
    private String name;
    @MappingProperty(order = 2, name = "Soyad")
    private String surname;
    @MappingProperty(order = 4, name = "Long Id")
    private long longid;
    @MappingProperty(order = 3, name = "Double Id")
    private double doubleid;

    @MappingProperty(order = 5)
    private BigDecimal big = BigDecimal.ONE;

    @MappingProperty(order = 6, name = "Date Format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "CET")
    private Date date2 = new Date();

    @Enumerated(EnumType.STRING)
    @MappingProperty(order = 7, name = "Normal Enum")
    private SampleEnum sampleEnum;


    @Enumerated(EnumType.STRING)
    @MappingProperty(order = 8, name = "Sample 2 Enum")
    private Sample2Enum sample2Enum;

    public Sample2Enum getSample2Enum() {
        return sample2Enum;
    }

    public void setSample2Enum(Sample2Enum sample2Enum) {
        this.sample2Enum = sample2Enum;
    }

    public SampleEnum getSampleEnum() {
        return sampleEnum;
    }

    public void setSampleEnum(SampleEnum sampleEnum) {
        this.sampleEnum = sampleEnum;
    }

    public SamplePojo() {

    }

    public SamplePojo(int id, String name, String surname, long longid, double doubleid, BigDecimal big, Date date2, SampleEnum sampleEnum, Sample2Enum sample2Enum) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.longid = longid;
        this.doubleid = doubleid;
        this.big = big;
        this.date2 = date2;
        this.sampleEnum = sampleEnum;
        this.sample2Enum = sample2Enum;
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
                ", sampleEnum=" + sampleEnum +
                ", sample2Enum=" + sample2Enum +
                '}';
    }
}