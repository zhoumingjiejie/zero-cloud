package com.github.icezerocat.icezerocat.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: 基础设备ip地址
 * CreateDate:  2021/10/15 14:12
 *
 * @author zero
 * @version 1.0
 */
@Data
@Entity
@Table(name = "ZHFW_BASE_EQUIPMENT_IP")
public class BaseEquipmentIpAddress implements Serializable {

    private static final long serialVersionUID = -8074273027812084147L;

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 设备Id
     */
    @Column(name = "EQUIPMENT_ID")
    private String equipmentId;
    public static final String EQUIPMENT_ID = "EQUIPMENT_ID";
    /**
     * ip地址
     */
    private String ipAddress;
}
