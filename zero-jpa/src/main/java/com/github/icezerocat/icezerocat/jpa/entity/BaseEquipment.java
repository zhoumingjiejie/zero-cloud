package com.github.icezerocat.icezerocat.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description: 基础设备信息
 * CreateDate:  2021/10/15 13:52
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ZHFW_BASE_EQUIPMENT")
public class BaseEquipment implements Serializable {
    /**
     * 设备ID
     */
    @Id
    private String id;
    final public static String ID = "ID";
    /**
     * 设备编号
     */
    private String deviceNo;
    final public static String DEVICE_NO = "DEVICE_NO";
    /**
     * cpu序列号
     */
    private String cpuSerial;
    /**
     * 主板序列号
     */
    private String mainBoardSerial;
    final public static String MAIN_BOARD_SERIAL = "MAIN_BOARD_SERIAL";

    @Transient
    private String code;

    /**
     * 创建时间
     */
    private Date createDate;
    public static final String CREATE_DATE = "CREATE_DATE";

    /**
     * 更新时间
     */
    private Date updateDate;
    public static final String UPDATE_DATE = "UPDATE_DATE";

    /**
     * 注册时间
     */
    private Date registerDate;
    public static final String REGISTER_DATE = "REGISTER_DATE";

    /**
     * 基础设备ip地址列表
     * <code>
     *     mappedBy 属性指定关联实体的属性，例如Order 与 OrderItem是一对多的关联，OrderItem中有一个orderId属性执行Order 的id，
     *     那么mappedBy的值即为orderId。注意，此处的值是属性值而非数据库中列名。可以使用@JoinColumn注解来替代mappedBy属性，但是@JoinColumn的name属性指定的是数据库中的列名
     * </code>
     */
//    @Transient
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "equipmentId")
//    @JoinColumn(name = "EQUIPMENT_ID")
    private List<BaseEquipmentIpAddress> baseEquipmentIpAddressList;

}
