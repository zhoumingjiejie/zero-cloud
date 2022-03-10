package com.github.icezerocat.zero.id.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: TODO
 * CreateDate:  2021/11/30 14:54
 *
 * @author zero
 * @version 1.0
 */
public class IdFormatterWithDate implements IdFormatter {
    private int length = 4;
    private String dateFormat = "yyyyMMdd";

    public void setLength(int length) {
        this.length = length;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public IdFormatterWithDate() {
        this.dateFormat = "yyyyMMdd";
    }

    public IdFormatterWithDate(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public IdFormatterWithDate(int length) {
        this.length = length;
        this.dateFormat = "yyyyMMdd";
    }

    public IdFormatterWithDate(int length, String dateFormat) {
        this.length = length;
        this.dateFormat = dateFormat;
    }

    @Override
    public String formatter(long id) {
        StringBuilder formatter = new StringBuilder();
        if (this.length <= 0) {
            this.length = 4;
        }

        if (this.length > 12) {
            this.length = 12;
        }

        for(int i = 0; i < this.length; ++i) {
            formatter.append("0");
        }

        return (new SimpleDateFormat(this.dateFormat)).format(new Date()) + (new DecimalFormat(formatter.toString())).format(id);
    }
}
