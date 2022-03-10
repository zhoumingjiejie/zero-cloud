package com.github.icezerocat.zero.id.service;

import com.github.icezerocat.zero.id.model.IdBlock;

/**
 * Description: TODO
 * CreateDate:  2021/11/30 14:21
 *
 * @author zero
 * @version 1.0
 */
public interface IdGeneratorService {
    IdBlock doGetNextIdBlock(String idName, String groupName);
}
