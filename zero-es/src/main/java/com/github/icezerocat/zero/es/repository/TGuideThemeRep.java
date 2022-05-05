package com.github.icezerocat.zero.es.repository;

import com.github.icezerocat.zero.es.entity.TGuideTheme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Description: 主题仓库
 * CreateDate:  2022/4/20 16:37
 *
 * @author zero
 * @version 1.0
 */
public interface TGuideThemeRep extends ElasticsearchRepository<TGuideTheme, Long> {
}
