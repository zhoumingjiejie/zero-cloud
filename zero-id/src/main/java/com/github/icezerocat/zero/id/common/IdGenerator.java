package com.github.icezerocat.zero.id.common;

import com.github.icezerocat.zero.id.model.IdBlock;
import com.github.icezerocat.zero.id.model.IdFormatter;
import com.github.icezerocat.zero.id.service.IdGeneratorService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: id生成类
 * CreateDate:  2021/11/30 14:20
 *
 * @author zero
 * @version 1.0
 */
public class IdGenerator {
    private static Map<String, IdGenerator> idGeneratorMap = new HashMap<>();
    private String idName;
    private String groupName;
    private long nextId = 0L;
    private long lastId = -1L;

    private IdGenerator() {
    }

    private synchronized long getNextId() {
        if (this.lastId < this.nextId) {
            this.getNewBlock();
        }
        return this.nextId++;
    }

    private synchronized void getNewBlock() {
        Object o = "idGeneratorService";
        IdGeneratorService idGeneratorService = (IdGeneratorService) o;
        IdBlock idBlock = idGeneratorService.doGetNextIdBlock(this.idName, this.groupName);
        this.nextId = idBlock.getNextId();
        this.lastId = idBlock.getLastId();
    }

    public static long getNextId(String idName) {
        return getNextId(idName, "");
    }

    public static String getNextId(String idName, IdFormatter idFormatter) {
        return getNextId(idName, "", idFormatter);
    }

    public static synchronized long getNextId(String idName, String groupName) {
        IdGenerator idGenerator;
        String key = idName;
        if (StringUtils.hasText(groupName)) {
            key = idName + "." + groupName;
        }

        if (idGeneratorMap.containsKey(key)) {
            idGenerator = idGeneratorMap.get(key);
        } else {
            idGenerator = new IdGenerator();
            idGenerator.idName = idName;
            idGenerator.groupName = groupName;
            idGeneratorMap.put(key, idGenerator);
        }

        return idGenerator.getNextId();
    }

    public static String getNextId(String idName, String groupName, IdFormatter idFormatter) {
        long id = getNextId(idName, groupName);
        return null == idFormatter ? String.valueOf(id) : idFormatter.formatter(id);
    }
}
