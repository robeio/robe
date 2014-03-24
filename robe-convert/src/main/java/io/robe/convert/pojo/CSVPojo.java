package io.robe.convert.pojo;

/**
 * Created by kaanalkim on 19/03/14.
 */
public class CSVPojo {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int id;

    public String nameSurname;

    public String job;

    public CSVPojo() {

    }

}
