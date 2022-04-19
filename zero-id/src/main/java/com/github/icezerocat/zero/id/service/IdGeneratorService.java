package com.github.icezerocat.zero.id.service;

import com.github.icezerocat.zero.id.model.IdBlock;

/**
 * Description: ID生成器服务
 * CreateDate:  2021/11/30 14:21
 *
 * @author zero
 * @version 1.0
 */
public interface IdGeneratorService {
    /**
     * 获取下一个ID块，根据组名+id名自增，lastId为最新未使用的ID
     *
     * @param idName    id名字
     * @param groupName 分组名
     * @return ID块
     */
    IdBlock doGetNextIdBlock(String idName, String groupName);

    /**
     * 获取下一个ID块，根据组名+id名自增，lastId为最新未使用的ID
     *
     * @param groupName 组件
     * @return ID块
     */
    IdBlock doGetNextIdBlock(String groupName);

    /**
     * 获取下一个ID块，根据组名+id名自增，lastId为最新未使用的ID
     *
     * @return ID块
     */
    IdBlock doGetNextIdBlock();
}
