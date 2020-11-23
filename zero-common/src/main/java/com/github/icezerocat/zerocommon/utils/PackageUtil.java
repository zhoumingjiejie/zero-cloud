package com.github.icezerocat.zerocommon.utils;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Description: 类工具类
 * CreateDate:  2020/11/19 13:07
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
public class PackageUtil {

    /**
     * 获取项目包名
     *
     * @param pack 项目中任意一个类的包
     * @return 项目包名
     */
    public static String getProjectPackName(Package pack) {
        String packName = pack.getName();
        do {
            packName = packName.substring(0, packName.lastIndexOf("."));
            pack = Package.getPackage(packName);
        } while (null != pack);
        log.info("项目包名package：{}", packName);
        return packName;
    }

    /**
     * 获取某个类的所有子类或实现类
     *
     * @param superClass 实体类简称
     * @return 实体类
     */
    public static <T> Set<Class<? extends T>> getClassByName(Class<T> superClass) {
        Reflections reflections = new Reflections(getProjectPackName(superClass.getPackage()));
        return reflections.getSubTypesOf(superClass);
    }
}
