package com.github.icezerocat.zeroclient3.executor;

import com.github.icezerocat.component.jdbctemplate.service.BaseJdbcTemplate;
import com.github.icezerocat.component.jdbctemplate.service.impl.BaseJdbcTemplateImpl;
import com.github.icezerocat.component.redisson.dto.SyncDto;
import com.github.icezerocat.component.redisson.utils.RedisUtil;
import com.github.icezerocat.zeroclient3.constant.CacheConstant;
import com.github.icezerocat.zeroclient3.intercept.AbstractSyncProviderExe;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Description: 批量保存或更新同步操作
 * CreateDate:  2020/12/21 18:49
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service("saveOrUpdateBatchExe")
public class SaveOrUpdateBatchExe extends AbstractSyncProviderExe {
    private final BaseJdbcTemplate baseJdbcTemplate;

    public SaveOrUpdateBatchExe(BaseJdbcTemplate baseJdbcTemplate) {
        this.baseJdbcTemplate = baseJdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAround(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object o : args) {
            Collection dynamicVersionList = (Collection) o;
            for (Object dynamicVersion : dynamicVersionList) {
                BaseJdbcTemplateImpl.TableCheck tableCheck = baseJdbcTemplate.containsKey(dynamicVersion.getClass(), dynamicVersion);
                String sql;
                Object[] values;
                SyncDto syncDto = new SyncDto();
                if (tableCheck.isContainsKey()) {
                    sql = baseJdbcTemplate.getUpdateSql(dynamicVersion.getClass(), tableCheck);
                    values = baseJdbcTemplate.getUpdateValue(dynamicVersion, tableCheck);
                } else {
                    sql = baseJdbcTemplate.getInsertSql(dynamicVersion.getClass());
                    values = baseJdbcTemplate.getInsertValue(dynamicVersion);
                }
                syncDto.setCreateDate(new Date());
                syncDto.setSql(sql);
                syncDto.setValues(values);
                RedisUtil.getInstance().sSet(CacheConstant.WX_SYNC_PREFIX.getValue(), syncDto);

                Set<Object> objects1 = RedisUtil.getInstance().sGet(CacheConstant.WX_SYNC_PREFIX.getValue());
                log.debug("redisData:{}", objects1);
                objects1.forEach(o1 -> {
                    SyncDto syn = (SyncDto) o1;
                    this.baseJdbcTemplate.update(syn.getSql(), syn.getValues());
                });
            }
        }
    }

    @Override
    public void doAfterThrowing(Throwable ex) {
        log.debug("异常处理者：", ex);
    }
}
