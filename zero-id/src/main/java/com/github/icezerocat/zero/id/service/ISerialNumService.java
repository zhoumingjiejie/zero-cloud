package com.github.icezerocat.zero.id.service;

import com.github.icezerocat.zero.id.entity.SystemSerialNumber;

/**
 * Description: TODO
 * CreateDate:  2021/11/30 12:07
 *
 * @author zero
 * @version 1.0
 */
public interface ISerialNumService {
    public SystemSerialNumber find(SystemSerialNumber systemSerialNumber);

    public String generateSerialNumberByModelCode(String moduleCode);

    /**
     * 设置最小值
     * @param value 最小值，要求：大于等于零
     * @return      流水号生成器实例
     */
    ISerialNumService setMin(int value);

    /**
     * 设置最大值
     * @param value 最大值，要求：小于等于Long.MAX_VALUE ( 9223372036854775807 )
     * @return      流水号生成器实例
     */
    ISerialNumService setMax(long value);

    /**
     * 设置预生成流水号数量
     * @param count 预生成数量
     * @return      流水号生成器实例
     */
    ISerialNumService setPrepare(int count);
}
