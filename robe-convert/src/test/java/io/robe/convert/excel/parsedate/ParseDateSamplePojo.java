package io.robe.convert.excel.parsedate;

import io.robe.convert.common.annotation.Convert;

import java.util.Date;

/**
 * Created by hasanmumin on 20/10/2016.
 */
public class ParseDateSamplePojo {
    @Convert(type = Convert.Type.DATE)
    private Date date;

    public ParseDateSamplePojo(Date date) {
        this.date = date;
    }

    public ParseDateSamplePojo() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
