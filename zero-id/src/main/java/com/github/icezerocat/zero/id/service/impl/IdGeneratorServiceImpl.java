package com.github.icezerocat.zero.id.service.impl;

import com.github.icezerocat.zero.id.model.IdBlock;
import com.github.icezerocat.zero.id.service.IdGeneratorService;
import github.com.icezerocat.component.common.model.Param;
import github.com.icezerocat.jdbctemplate.service.BaseJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description: ID生成器服务
 * CreateDate:  2021/11/30 14:22
 *
 * @author zero
 * @version 1.0
 */
@Service("idGeneratorService")
public class IdGeneratorServiceImpl implements IdGeneratorService {

    final private BaseJdbcTemplate baseJdbcTemplate;

    public IdGeneratorServiceImpl(BaseJdbcTemplate baseJdbcTemplate) {
        this.baseJdbcTemplate = baseJdbcTemplate;
    }

    @Override
    public synchronized IdBlock doGetNextIdBlock(String idName, String groupName) {
        return this.synDoGetNextIdBlock(idName, groupName);
    }

    @Override
    public IdBlock doGetNextIdBlock(String groupName) {
        return this.doGetNextIdBlock(groupName, groupName);
    }

    @Override
    public synchronized IdBlock doGetNextIdBlock() {
        return this.doGetNextIdBlock("default", "default");
    }

    /**
     * 同步锁获取下一个
     *
     * @param idName    id名字
     * @param groupName 分组名
     * @return id块
     */
    @Transactional(rollbackFor = Exception.class)
    IdBlock synDoGetNextIdBlock(String idName, String groupName) {
        List<Param> paramList = new ArrayList<>();
        paramList.add(new Param("id_name", idName));
        groupName = Optional.ofNullable(groupName).orElse("default");
        paramList.add(new Param("group_name", groupName));
        List<IdBlock> idBlockList = this.baseJdbcTemplate.getList(paramList, IdBlock.class, null);
        String finalGroupName = groupName;
        IdBlock idBlock = null;
        //存在则获取第一个,不存在则新建
        if (!CollectionUtils.isEmpty(idBlockList)) {
            idBlock = idBlockList.get(0);
            long nextId = idBlock.getNextId();
            idBlock.setLastId(nextId);
            idBlock.setNextId(++nextId);
            this.baseJdbcTemplate.update("UPDATE t_ap_serial_number SET last_id = ?, next_id = ? WHERE id = ?", idBlock.getLastId(), idBlock.getNextId(), idBlock.getId());
        } else {
            idBlock = new IdBlock(idName, finalGroupName);
            Long maxId = this.baseJdbcTemplate.queryForObject("SELECT MAX(id) FROM t_ap_serial_number", new Object[]{}, Long.class);
            long id = maxId == null ? 1L : ++maxId;
            idBlock.setId(id);
            this.baseJdbcTemplate.update(
                    "INSERT INTO t_ap_serial_number (ID, ID_NAME , ID_VALUE , GROUP_NAME , NEXT_ID , LAST_ID  )  VALUES  (  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?   )",
                    idBlock.getId(), idBlock.getIdName(), idBlock.getIdValue(), idBlock.getGroupName(), idBlock.getNextId(), idBlock.getLastId());
        }
        return idBlock;
    }
}
