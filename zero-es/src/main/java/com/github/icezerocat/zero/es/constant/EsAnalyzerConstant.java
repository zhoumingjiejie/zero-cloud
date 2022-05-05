package com.github.icezerocat.zero.es.constant;

/**
 * Description: es分析解析器
 * CreateDate:  2022/2/12 15:41
 *
 * @author zero
 * @version 1.0
 */
public interface EsAnalyzerConstant {

    /**
     * IK小粒度分词器
     */
    String IK_MAX_WORD = "ik_max_word";
    /**
     * IK智能分词器
     */
    String IK_SMART = "ik_smart";
    /**
     * es默认的标准分词器
     */
    String STANDARD = "standard";
    /**
     * 同义词分词器
     */
    String IK_SYNO = "ik_syno";
    /**
     * 最小粒度同义词分词器
     */
    String IK_SYNO_MAX = "ik_syno_max";
}
