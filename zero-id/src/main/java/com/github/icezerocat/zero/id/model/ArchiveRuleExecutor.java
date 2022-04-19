package com.github.icezerocat.zero.id.model;

import com.github.icezerocat.zero.id.service.IdGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Description: 档案编号解析规则（暂未使用）
 * CreateDate:  2021/11/30 14:59
 *
 * @author zero
 * @version 1.0
 */
public class ArchiveRuleExecutor {
    private final Logger logger = LoggerFactory.getLogger(ArchiveRuleExecutor.class);

    private IdGeneratorService idGeneratorService;  //流水号操作接口
    private Map<String, Object> formData;

    /**
     * 归档号加工厂
     *
     * @param formData 表单数据
     */
   /* public ArchiveRuleExecutor(PlatformAPDataService apDataService, IdGeneratorService idGeneratorService, Map<String, Object> formData) {
        this.apDataService = apDataService;
        this.idGeneratorService = idGeneratorService;
        this.formData = formData;
    }

    *//**
     * 解析规则
     *
     * @return List 规则字段id列表
     * @throws Exception
     *//*
    public List<String> executeRule() throws Exception {
        String category = this.apDataService.searchData(FormMappingIdConstant.FORM_ARCHIVES_CATEGORY_CODE, FormMappingIdConstant.DB_CATEGORY_NAME_ID, formData.get(FormMappingIdConstant.TABLE_TYPE), FormMappingIdConstant.DB_ARCHIVES_NUM_SET_ID);
        List<Map<String, String>> archiveRuleData = getArchiveNoRule(category);//获取规则
        List<String> listId = new ArrayList();//规则id列表
        for (Map<String, String> archiveRule : archiveRuleData) {
            String key = archiveRule.get(FormMappingIdConstant.TABLE_COLUMN_ID);//字段id
            listId.add(key);
            String ruleName = archiveRule.get(FormMappingIdConstant.TABLE_ARCHIVES_RULE);//规则

            String archiveNo = analyseArchiveNoRule(ruleName);//解析出来的规则

            formData.put(key, archiveNo);
        }
        return listId;
    }

    *//**
     * 获取规则字符串
     *
     * @param archivesNumSet 归档号规则标识
     * @return
     *//*
    private List<Map<String, String>> getArchiveNoRule(Object archivesNumSet) throws Exception {
        //获取规则里面的id字段
        String fkId = this.apDataService.searchData(FormMappingIdConstant.FORM_ARCHIVES_RULE_CODE, FormMappingIdConstant.TABLE_ARCHIVES_RULE, archivesNumSet, FormMappingIdConstant.TABLE_ID);
        //通过id字段获取子表里面的规则
        JSONArray ruleArr = this.apDataService.searchSomeData("APF_vpo9ePXi", FormMappingIdConstant.DB_FKID, fkId);
        List<Map<String, String>> mapList = new ArrayList();
        for (Object rules : ruleArr) {
            Map<String, String> ruleMap = (Map<String, String>) rules;
            mapList.add(ruleMap);
            String ruleName = ruleMap.get(FormMappingIdConstant.TABLE_ARCHIVES_RULE);
            //禁止自己截取自己的规则
            if (ruleName.contains((String) archivesNumSet)) {
                throw new Exception("禁止自己调用自己的规则进行截取字符串，会导致死循环！请检查规则基础表中的规则");
            }
        }
        return mapList;
    }

    *//**
     * 归档号规则转换对应文字
     *
     * @param archiveNoRule 归档号规则
     * @return
     *//*
    private String analyseArchiveNoRule(String archiveNoRule) throws Exception {

        //处理{}的规则
        if (archiveNoRule.contains("{") && archiveNoRule.contains("}")) {
            archiveNoRule = codeRules(archiveNoRule);
        }
        //处理$$的规则
        if (archiveNoRule.contains("$")) {
            archiveNoRule = subStringRules(archiveNoRule);
        }

        return archiveNoRule;
    }

    *//**
     * 获取代字
     *
     * @param ruleItemName 需要获取哪些字段的代字
     * @return code代字
     *//*
    private String getRelateData(String ruleItemName) throws Exception {
        String code = "";
        if (!formData.containsKey(FormMappingIdConstant.CODE_UNIT_ID) || !formData.containsKey(FormMappingIdConstant.TABLE_SAVE_TIME)) {
            throw new Exception("表单中" + FormMappingIdConstant.CODE_UNIT_ID + "单位ID和" + FormMappingIdConstant.TABLE_SAVE_TIME + "保管期限不能为空");
        }
        switch (ruleItemName) {
            case "单位代字":
                code = (String) formData.get(FormMappingIdConstant.CODE_UNIT_ID);
                break;
            case "档案类别代字":
                code = getRuleCode(FormMappingIdConstant.FORM_ARCHIVES_CATEGORY_CODE, FormMappingIdConstant.DB_CATEGORY_NAME_ID, FormMappingIdConstant.TABLE_TYPE, FormMappingIdConstant.CODE_CATEGORY_ID);
                break;
            case "档案年度代字":
                code = getRuleCode(FormMappingIdConstant.FORM_YEAR_CODE, FormMappingIdConstant.DB_YEAR_ID, FormMappingIdConstant.TABLE_YEAR, FormMappingIdConstant.CODE_YEAR_ID);
                break;
            case "档案室代字":
                code = getRuleCode(FormMappingIdConstant.FORM_ARCHIVES_ROOM_CODE, FormMappingIdConstant.DB_ARCHIVES_ROOM_ID, FormMappingIdConstant.TABLE_ARCHIVE_ROOM, FormMappingIdConstant.CODE_ARCHIVES_ROOM_ID);
                break;
            case "档案柜代字":
                code = getRuleCode(FormMappingIdConstant.FORM_ARCHIVES_CABINET_CODE, FormMappingIdConstant.DB_CABINET_NAME_ID, FormMappingIdConstant.TABLE_ARCHIVES_CABINET, FormMappingIdConstant.CODE_CABINET_ID);
                break;
            case "密级代字":
                code = getRuleCode(FormMappingIdConstant.FORM_RANK_CODE, FormMappingIdConstant.DB_RANK_ID, FormMappingIdConstant.TABLE_RANK, FormMappingIdConstant.CODE_RANK_ID);
                break;
            case "保管期限代字":
                code = (String) formData.get(FormMappingIdConstant.TABLE_SAVE_TIME);
                break;
            default:
                code = "-1";
        }
        //判断是否有对应的代字
        if (code.equals("")) {
            throw new Exception("规则解析失败，基础表中没有对应的" + ruleItemName);
        }
        if (code.equals("-1") && (ruleItemName.indexOf("*") == -1)) {
            throw new Exception("规则基础表中规则错误。规则名列表：单位代字、档案类别代字、档案年度代字、档案室代字、档案柜代字、密级代字、保管期限代字");
        }

        return code;
    }

    *//**
     * 获取指定字符里面的字段
     *
     * @param managers    字符串
     * @param beforeSplit 分割 beforeSplit 字符后面的内容
     * @param afterSplit  分割的内容直到 afterSplit 截止
     * @return
     *//*
    private List<String> getRuleNameList(String managers, String beforeSplit, String afterSplit) {
        List<String> ls = new ArrayList();
        String splitStr = "(?<=\\" + beforeSplit + ")(.+?)(?=\\" + afterSplit + ")";
        Pattern pattern = Pattern.compile(splitStr.toString());
        Matcher matcher = pattern.matcher(managers);
        while (matcher.find()) {
            ls.add(matcher.group());
        }
        return ls;
    }

    *//**
     * 根据表单对应关系获取代字
     *
     * @param formCode    表单标识符
     * @param name        基础表中表单字段名
     * @param formFiledId 表单字段ID，这里的字段值和表单的字段值需要有一个对应
     * @param codeId      基础信息表中代字ID
     * @return
     *//*
    private String getRuleCode(String formCode, String name, String formFiledId, String codeId) throws Exception {
        String ruleCode = this.apDataService.searchData(formCode, name, formData.get(formFiledId), codeId);
        return ruleCode;
    }

    *//**
     * @param managers 自定义的序列号
     * @return
     * @throws Exception
     *//*
    private String customKey(String managers) throws Exception {

        List<String> customKeyList = getRuleNameList(managers, "(", ")");
        for (Object ruleItemName : customKeyList) {
            String code = this.getRelateData((String) ruleItemName);//转换代字
            managers = managers.replace("(" + ruleItemName + ")", code);
        }

        return managers;
    }

    *//**
     * 对含有{}字符的规则进行解析赋值
     *
     * @param archiveNoRule 归档号规则
     * @return
     * @throws Exception
     *//*
    private String codeRules(String archiveNoRule) throws Exception {
        List<String> ruleList = getRuleNameList(archiveNoRule, "{", "}");//规则列表
        List<String> codeList = new ArrayList();//代字列表
        for (Object ruleItemName : ruleList) {
            codeList.add(this.getRelateData((String) ruleItemName));
        }
        for (int i = 0; i < codeList.size(); i++) {
            String code = "";//需要code内容替换规则里面的内容
            String name = ruleList.get(i);
            //是否需要序列号，存在*字符时代表这个是序列号
            if (name.indexOf("*") != -1) {
                String keyName = name.substring(0, name.indexOf("*"));
                String key = "";//序号自动增长的key，默认："序列号"为序列号前面的字符串作为key，自定义："(xxx代字)"作为key
                if (keyName.equals("序列号")) {
                    key = archiveNoRule.replace("{" + name + "}", "");
                } else {
                    key = customKey(keyName);
                }
                int length = Integer.parseInt(name.substring(name.indexOf("*") + 1, name.length()));//序列号的长度
                code = this.idGeneratorService.doGetNextId(key, length);
            } else {
                code = codeList.get(i);
            }
            archiveNoRule = archiveNoRule.replace("{" + name + "}", code);
        }
        return archiveNoRule;
    }

    *//**
     * 对含有$字符的规则进行解析赋值
     *
     * @param archiveNoRule 归档号规则
     * @return
     *//*
    private String subStringRules(String archiveNoRule) throws Exception {
        List<String> ls = getRuleNameList(archiveNoRule, "$", "$");
        Map<String, Object> result = new HashMap();

        for (String str : ls) {
            String[] arr = str.split(",");
            String analyseRules = (String) formData.get(arr[0]);
            //长度大于1时，需要截取字符串
            int length = arr.length;
            if (length > 1) {
                result.put(str, analyseRules.substring(Integer.valueOf(arr[1]), Integer.valueOf(arr[2])));
            } else {
                result.put(str, analyseRules);
            }
        }
        //替换
        for (Map.Entry<String, Object> key : result.entrySet()) {
            archiveNoRule = archiveNoRule.replace("$" + key.getKey() + "$", (String) key.getValue());
        }
        return archiveNoRule;
    }

    private void ruleOption(Map<String, String> map, String archiveNo) throws Exception {
        String columnId = FormMappingIdConstant.TABLE_COLUMN_ID;//字段名称
        //判断是否必填
        if (map.get("requiredValue").equals("是")) {
            if (this.formData.get(map.get(columnId)).equals("")) {
                throw new Exception(FormMappingIdConstant.TABLE_COLUMN_ID + "字段不能为空");
            }
        }
        //判断归档表单字段是否唯一
        if (map.get("onlyValue").equals("是")) {
            String id = map.get(columnId);
            JSONArray result = this.apDataService.searchSomeData(FormMappingIdConstant.FORM_FILING_CODE, id, (String) this.formData.get(id));
            if (result != null) {
                throw new Exception(FormMappingIdConstant.TABLE_COLUMN_ID + "字段不是唯一");
            }
        }
    }*/
}
