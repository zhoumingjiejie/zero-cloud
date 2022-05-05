package com.github.icezerocat.zero.es.repository;

import com.github.icezerocat.zero.es.entity.ZhfwQaQuestion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Description: 问题库
 * CreateDate:  2022/2/14 23:08
 *
 * @author zero
 * @version 1.0
 */
public interface QaQuestionRep extends ElasticsearchRepository<ZhfwQaQuestion, String> {
}
