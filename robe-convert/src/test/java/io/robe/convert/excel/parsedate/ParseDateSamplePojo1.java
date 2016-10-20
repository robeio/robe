package io.robe.convert.excel.parsedate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.robe.convert.common.annotation.Convert;

import java.util.Date;

/**
 * Created by hasanmumin on 20/10/2016.
 */
public class ParseDateSamplePojo1 {
    @Convert(type = Convert.Type.DATE)
    @JsonFormat
    private Date date;

    public ParseDateSamplePojo1(Date date) {
        this.date = date;
    }

    public ParseDateSamplePojo1() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
