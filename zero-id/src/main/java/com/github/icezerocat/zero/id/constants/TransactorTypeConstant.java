package com.github.icezerocat.zero.id.constants;

/**
 * 档案事务类型（暂未使用）
 * Created by zmj on 2018/8/28.
 */
public enum TransactorTypeConstant {
    /**
     * 事务类型常量
     */
    RECEIVE("接收档案", "包括接收归档数据包、校验数据包MD5和解压归档数据包"),
    CHECK("报文校验", "包括读取xml内容、必填项校验、附件数量校验、上传附件至Minio等"),
    FILING("归档", "归档"),
    SAVE("保存", "保存表单数据");

    String name;
    String value;

    private TransactorTypeConstant(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
