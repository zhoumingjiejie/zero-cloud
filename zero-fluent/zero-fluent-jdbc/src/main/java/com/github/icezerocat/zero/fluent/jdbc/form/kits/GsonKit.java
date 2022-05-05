package com.github.icezerocat.zero.fluent.jdbc.form.kits;

import com.github.icezerocat.zero.fluent.jdbc.form.Form;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * gson实现
 *
 * @author wudarui
 */
public class GsonKit {
    public static final Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .setDateFormat(StrConstant.DateStrFormat)
        .create();

    public static Form form(String json) {
        return gson.fromJson(json, Form.class);
    }
}
