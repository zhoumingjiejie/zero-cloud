package com.github.icezerocat.zero.id.dao.impl;

import com.github.icezerocat.zero.id.dao.IdGeneratorDao;
import com.github.icezerocat.zero.id.model.IdBlock;
import github.com.icezerocat.component.common.model.Param;
import github.com.icezerocat.jdbctemplate.service.BaseJdbcTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description: ID 生成器 Dao
 * CreateDate:  2021/12/31 14:03
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Resource
@RequiredArgsConstructor
public class IdGeneratorDaoImpl implements IdGeneratorDao {

    final private BaseJdbcTemplate baseJdbcTemplate;

    @Override
    public IdBlock getIdBlock(String idName, String groupName) {
        List<Param> paramList = new ArrayList<>();
        paramList.add(new Param("id_name", idName));
        groupName = Optional.ofNullable(groupName).orElse("default");
        paramList.add(new Param("group_name", groupName));
        List<IdBlock> idBlockList = this.baseJdbcTemplate.getList(paramList, IdBlock.class, null);
        String finalGroupName = groupName;
        IdBlock idBlock = Optional.ofNullable(idBlockList)
                //存在则获取第一个
                .filter(o -> !CollectionUtils.isEmpty(o)).map(o -> o.get(0))
                //不存在则新建
                .orElseGet(() -> {
                    IdBlock ib = new IdBlock(idName, finalGroupName);
                    this.baseJdbcTemplate.insert(ib);
                    return ib;
                });
        return idBlock;
    }

    @Override
    public void save(IdBlock idBlock) {

    }

    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(123);
        Optional.ofNullable(list).filter(o -> !CollectionUtils.isEmpty(o)).orElseThrow(() -> new RuntimeException("插入对象集合不能为空"));
        log.debug("{}", new IdBlock());
    }
}
