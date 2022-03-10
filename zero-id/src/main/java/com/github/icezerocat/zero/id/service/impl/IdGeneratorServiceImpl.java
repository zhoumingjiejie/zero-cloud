package com.github.icezerocat.zero.id.service.impl;

import com.github.icezerocat.zero.id.dao.IdGeneratorDao;
import com.github.icezerocat.zero.id.model.IdBlock;
import com.github.icezerocat.zero.id.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description: TODO
 * CreateDate:  2021/11/30 14:22
 *
 * @author zero
 * @version 1.0
 */
public class IdGeneratorServiceImpl  implements IdGeneratorService {
    private IdGeneratorDao idGeneratorDao = null;
    private int idPoolSize = 1;

    public IdGeneratorServiceImpl() {
    }

    @Autowired
    public void setIdGeneratorDao(IdGeneratorDao idGeneratorDao) {
        this.idGeneratorDao = idGeneratorDao;
    }

    public void setIdPoolSize(int idPoolSize) {
        this.idPoolSize = idPoolSize;
    }

    @Override
    public IdBlock doGetNextIdBlock(String idName, String groupName) {
        IdBlock idBlock = this.idGeneratorDao.getIdBlock(idName, groupName);
        if (null == idBlock) {
            idBlock = new IdBlock();
            idBlock.setId(-1L);
            idBlock.setIdName(idName);
            idBlock.setGroupName(groupName);
            idBlock.setIdValue(1L);
        }

        if (this.idPoolSize <= 0) {
            this.idPoolSize = 1;
        }

        long oldValue = idBlock.getIdValue();
        long newValue = oldValue + (long)this.idPoolSize;
        idBlock.setIdValue(newValue);
        this.idGeneratorDao.save(idBlock);
        return new IdBlock(oldValue, newValue - 1L);
    }
}
