package com.github.icezerocat.zero.id.dao;

import com.github.icezerocat.zero.id.model.IdBlock;

/**
 * Description: TODO
 * CreateDate:  2021/11/30 14:33
 *
 * @author zero
 * @version 1.0
 */
public interface IdGeneratorDao {
    /**
     *<code>
     *     public IdBlock getIdBlock(String idName, String groupName) {
     *         List<Object> args = new ArrayList();
     *         String hql = "from IdBlock idBlock where 1 = 1 ";
     *         if (!StringUtils.isNullString(idName)) {
     *             hql = hql + "and idBlock.idName = ? ";
     *             args.add(idName);
     *         }
     *
     *         if (!StringUtils.isNullString(groupName)) {
     *             hql = hql + "and idBlock.groupName = ? ";
     *             args.add(groupName);
     *         }
     *
     *         return (IdBlock)this.findUnique(hql, args.toArray());
     *     }
     *</code>
     * @param idName
     * @param groupName
     * @return
     */
    IdBlock getIdBlock(String idName, String groupName);

    void save(IdBlock idBlock);
}
