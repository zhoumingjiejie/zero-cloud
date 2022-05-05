package com.github.icezerocat.zero.fluent.jdbc.mybatis.functions;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.EntityRefKit;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.RefKit;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * RefFunction2: 分组关联
 *
 * @param <E>
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AllArgsConstructor
public class RefFinder<E> implements IGetter<List<E>> {
    private final Class entityClass;

    private final String refMethodName;

    public final IGetter<List<E>> finder;

    @Override
    public Object get(List<E> entities) {
        return finder.get(entities);
    }

    public void relation(Object source) {
        RefKey refKey = RefKit.byEntity(entityClass).findRefKey(refMethodName);
        List entities = new ArrayList();
        if (source instanceof List) {
            entities.addAll((List) source);
        } else {
            entities.add(source);
        }
        if (!entities.isEmpty()) {
            List refs = (List) finder.get(entities);
            EntityRefKit.groupRelation(refKey, entities, refs);
        }
    }
}
