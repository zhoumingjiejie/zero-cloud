package com.github.icezerocat.zero.id.model;

import java.text.DecimalFormat;

/**
 * Description: id长度格式器
 * CreateDate:  2021/11/30 14:45
 *
 * @author zero
 * @version 1.0
 */
public class IdFormatterN  implements IdFormatter {
    private int length = 0;

    public void setLength(int length) {
        this.length = length;
    }

    public IdFormatterN() {
    }

    public IdFormatterN(int length) {
        this.length = length;
    }

    @Override
    public String formatter(long id) {
        StringBuilder formatter = new StringBuilder();
        if (this.length <= 0) {
            return String.valueOf(id);
        } else {
            if (this.length > 12) {
                this.length = 12;
            }

            for(int i = 0; i < this.length; ++i) {
                formatter.append("0");
            }

            return (new DecimalFormat(formatter.toString())).format(id);
        }
    }
}
