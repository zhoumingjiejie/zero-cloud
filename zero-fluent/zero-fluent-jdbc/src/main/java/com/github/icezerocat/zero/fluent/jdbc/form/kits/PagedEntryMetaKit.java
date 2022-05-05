package com.github.icezerocat.zero.fluent.jdbc.form.kits;

import com.github.icezerocat.zero.fluent.annotation.form.annotation.EntryType;
import com.github.icezerocat.zero.fluent.annotation.form.meta.EntryMeta;
import com.github.icezerocat.zero.fluent.annotation.form.meta.entry.EntryMetaKit;
import com.github.icezerocat.zero.fluent.annotation.form.meta.entry.PagedEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * PagedMetaKit
 *
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused"})
public class PagedEntryMetaKit implements EntryMetaKit {
    private static final List<EntryMeta> metas = new ArrayList<>(3);

    static {
        metas.add(new EntryMeta("pageSize", int.class, EntryType.PageSize, PagedEntry::getPageSize, null, true));
        metas.add(new EntryMeta("currPage", int.class, EntryType.CurrPage, PagedEntry::getCurrPage, null, true));
        metas.add(new EntryMeta("pagedTag", String.class, EntryType.PagedTag, PagedEntry::getPagedTag, null, true));
    }

    @Override
    public final List<EntryMeta> entryMetas() {
        return metas;
    }
}
