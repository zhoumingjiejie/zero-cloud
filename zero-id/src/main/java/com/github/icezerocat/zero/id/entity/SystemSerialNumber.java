package com.github.icezerocat.zero.id.entity;

import java.io.Serializable;

/**
 * Description: 系统序列号
 * CreateDate:  2021/11/30 12:01
 *
 * @author zero
 * @version 1.0
 */
public class SystemSerialNumber implements Serializable {

    private static final long serialVersionUID = 1919271869825573124L;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 流水号配置模板
     */
    private String configTemplate;

    /**
     * 序列号最大值
     */
    private String maxSerial;

    /**
     * 是否自动增长标示
     */
    private String autoIncrement;

    /**
     * 预生成流水号数量
     */
    private String preMaxNum;


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getConfigTemplate() {
        return configTemplate;
    }

    public void setConfigTemplate(String configTemplate) {
        this.configTemplate = configTemplate;
    }

    public String getMaxSerial() {
        return maxSerial;
    }

    public void setMaxSerial(String maxSerial) {
        this.maxSerial = maxSerial;
    }

    public String getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(String autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getPreMaxNum() {
        return preMaxNum;
    }

    public void setPreMaxNum(String preMaxNum) {
        this.preMaxNum = preMaxNum;
    }

    @Override
    public String toString() {
        return "SystemSerialNumber{" +
                "moduleName='" + moduleName + '\'' +
                ", moduleCode='" + moduleCode + '\'' +
                ", configTemplate='" + configTemplate + '\'' +
                ", maxSerial='" + maxSerial + '\'' +
                ", autoIncrement='" + autoIncrement + '\'' +
                ", preMaxNum='" + preMaxNum + '\'' +
                '}';
    }
}
