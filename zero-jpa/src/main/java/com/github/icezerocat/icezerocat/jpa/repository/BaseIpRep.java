package com.github.icezerocat.icezerocat.jpa.repository;

import com.github.icezerocat.icezerocat.jpa.entity.BaseEquipmentIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description: 基础设备ip
 * CreateDate:  2022/1/27 14:02
 *
 * @author zero
 * @version 1.0
 */
public interface BaseIpRep extends JpaRepository<BaseEquipmentIpAddress, String> {
}
