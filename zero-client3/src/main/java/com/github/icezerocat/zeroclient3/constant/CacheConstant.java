package com.github.icezerocat.zeroclient3.constant;

/**
 * Description: Caffeine常量
 * CreateDate:  2020/12/21 17:39
 *
 * @author zero
 * @version 1.0
 */
public enum CacheConstant {

    /**
     * 微信同步锁
     */
    WX_SYNC_LOCK("WX_SYNC_LOCK", "微信同步锁"),
    /**
     * 微信同步key前缀
     */
    WX_SYNC_PREFIX("wx_sync_", "微信同步key前缀"),

    /**
     * 微信同步锁更新时间
     */
    WX_SYNC_LOCK_UPDATE_DATE("WX_SYNC_LOCK_UPDATE_DATE", "微信同步锁更新时间"),

    /**
     * 微信同步时判断，数据库是否存在异常数据需要同步。值：true/false
     */
    WX_SYNC_DB_DATA("WX_SYNC_DB_DATA", "微信同步时判断，数据库是否存在异常数据需要同步。值：true/false");

    CacheConstant(String value, String describe) {
        this.value = value;
        this.describe = describe;
    }

    /**
     * 标识符
     */
    private String value;
    /**
     * 描述
     */
    private String describe;

    public String getValue() {
        return value;
    }

    public String getDescribe() {
        return describe;
    }
}
