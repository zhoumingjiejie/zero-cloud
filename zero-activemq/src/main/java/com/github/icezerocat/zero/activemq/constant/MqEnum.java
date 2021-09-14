package com.github.icezerocat.zero.activemq.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description: mq常量
 * CreateDate:  2021/8/26 11:34
 *
 * @author zero
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum MqEnum {
    /**
     * mq队列、主题声明
     */
    QUEUE_NAME("publish.queue", "队列名"),
    OUT_QUEUE("out.queue", "出队信息，回调接口"),
    TOPIC_NAME("publish.topic", "主题名");
    /**
     * 名字
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
}
