package com.github.icezerocat.zero.id.model;

/**
 * Description: ID 格式化程序
 * CreateDate:  2021/11/30 14:53
 *
 * @author zero
 * @version 1.0
 */
public interface IdFormatter {

    /**
     * Id格式期
     *
     * @param length id长度
     * @return ID
     */
    String formatter(long length);
}
