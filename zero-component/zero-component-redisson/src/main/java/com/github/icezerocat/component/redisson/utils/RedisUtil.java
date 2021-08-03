package com.github.icezerocat.component.redisson.utils;

import com.github.icezerocat.component.redisson.config.RedisApplicationContextHelper;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法
 * 针对所有的List 都是以l开头的方法
 * <p>
 * Created by zmj
 * On 2019/11/9.
 *
 * @author 0.0.0
 */
@SuppressWarnings("unused")
public class RedisUtil {
    private static volatile RedisTemplate<String, Object> redisTemplate = null;
    private static volatile RedisUtil redisUtil = null;

    public static RedisUtil getInstance() {
        if (RedisUtil.redisUtil == null) {
            synchronized (RedisUtil.class) {
                if (redisUtil == null) {
                    RedisUtil.redisUtil = new RedisUtil();
                    RedisUtil.redisTemplate = RedisApplicationContextHelper.getTBean("redisTemplate");
                }
            }
        }
        return RedisUtil.redisUtil;
    }
    //=============================common============================

    /**
     * 获取redisTemplate
     *
     * @return redisTemplate
     */
    public RedisTemplate redisTemplate() {
        return redisTemplate;
    }

    /**
     * 验证redis链接
     *
     * @return boolean（true连接成功）
     */
    public boolean validationLink() {
        return RedisUtil.getInstance().redisTemplate() != null;
    }

    /**
     * 获取redis连接状态
     *
     * @return 连接信息
     */
    public String redisStatus() {
        List list = redisTemplate.getClientList();
        return list != null ? list.toString() : "连接redis失败";
    }

    /**
     * 清空redis数据
     *
     * @return 清空结果
     */
    public String redisFlushAll() {
        Set<String> keys = redisTemplate.keys("*");
        return "清空redis数据：" + redisTemplate.delete(keys) + "\t" + new Date();
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     */
    public <T> List<T> lGet(String key, long start, long end) {
        try {
            return (List<T>) redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有的list
     *
     * @param key key
     * @param <T> 对象
     * @return 所有的list
     */
    public <T> List<T> lGetAll(String key) {
        return this.lGet(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSetAll(String key, List<?> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSetAll(String key, List<?> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    // =================================================================================================================

    // ~ RedisKey
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Description: 设值
     *
     * @param key   缓存 {@link RedisKey}
     * @param value 值
     * @return T 放入缓存中的值
     * @date 2020-04-27 11:05:05
     */
    public <T> T setValue(RedisKey key, T value) {
        redisTemplate.opsForValue().set(key.of(), value);
        return value;
    }

    /**
     * Description: 设值
     *
     * @param key     缓存 {@link RedisKey}
     * @param value   值
     * @param seconds 有效时长 (秒)
     * @return T 放入缓存中的值
     * @date 2020-04-27 12:39:39
     */
    public <T> T setValue(RedisKey key, T value, long seconds) {
        redisTemplate.opsForValue().set(key.of(), value, seconds, TimeUnit.SECONDS);
        return value;
    }

    private Object getValue(String key) {
        if (!Optional.ofNullable(redisTemplate.hasKey(key)).orElse(Boolean.FALSE)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Description: 取值
     *
     * @param key   缓存 {@link RedisKey}
     * @param clazz 缓存对应的对象的 class 对象
     * @return T or null
     * @date 2020-04-27 12:32:28
     */
    public <T> T getValue(RedisKey key, Class<T> clazz) {
        return clazz.cast(getValue(key.of()));
    }


    // =================================================================================================================

    // ~ Hash
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Description: 获取操作 Hash 的对象引用
     */
    public Hash hash() {
        return new Hash();
    }

    /**
     * Description: Hash 操作对象
     */
    public static class Hash {

        /**
         * Description: 设值
         *
         * @param key 缓存 {@link RedisKey}
         * @param map Map 对象
         * @author LiKe
         * @date 2020-08-03 14:59:32
         */
        public void putAll(RedisKey key, Map<Object, Object> map) {
            redisTemplate.opsForHash().putAll(key.of(), map);
        }

        /**
         * Description: 获取全部
         *
         * @param key 缓存 {@link RedisKey}
         * @return java.util.Map<Object, Object>
         * @author LiKe
         * @date 2020-08-03 16:38:55
         */
        public Map<Object, Object> getAll(RedisKey key) {
            return redisTemplate.opsForHash().entries(key.of());
        }
    }

    // =================================================================================================================

    // ~ Files
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Description: 获取操作 Hash 的对象引用
     */
    public Files files() {
        return new Files();
    }

    /**
     * Description: 文件操作对象
     */
    public static class Files {

        private static final String CACHE_KEY_PREFIX = "file:";

        private static final String FIELD_FILE_NAME = "fileName";

        private static final String FIELD_FILE_CONTENT = "fileContent";

        /**
         * Description: 缓存文件<br>
         * Details: 将文件对象读入内存, 获取字节数组, 最后 {@code Base64} 编码. 以 缓存前缀 + 文件名 作为缓存 key
         *
         * @param file (Required) 文件对象
         * @author LiKe
         * @date 2020-10-09 14:41:03
         */
        @SneakyThrows
        public void setFile(File file) {
            final HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();

            final String fileName = Objects.requireNonNull(file, "文件不能为空").getName();
            final String fileContent = new String(
                    Base64.getEncoder().encode(IOUtils.toByteArray(org.apache.commons.io.FileUtils.openInputStream(Objects.requireNonNull(file, "文件对象不能为空")))),
                    StandardCharsets.UTF_8
            );

            final HashMap<String, String> map = new HashMap<>(2);
            map.put(FIELD_FILE_NAME, fileName);
            map.put(FIELD_FILE_CONTENT, fileContent);
            ops.putAll(CACHE_KEY_PREFIX + fileName, map);
        }

        /**
         * Description: 获取文件
         *
         * @param fileName (Required) 文件名
         * @return java.io.File 文件对象
         */
        @SneakyThrows
        public File getFile(String fileName) {
            final HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();

            // 缓存 Key
            final Map<Object, Object> entries = ops.entries(CACHE_KEY_PREFIX + Objects.requireNonNull(fileName, "文件名不能为空"));
            if (MapUtils.isEmpty(entries)) {
                return null;
            }

            final String cachedFileName = MapUtils.getString(entries, FIELD_FILE_NAME);
            final String cachedFileContent = MapUtils.getString(entries, FIELD_FILE_CONTENT);

            final File file = new File(FileUtils.getTempDirectoryPath() + cachedFileName);
            try (
                    final FileOutputStream out = new FileOutputStream(file);
                    final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)
            ) {
                bufferedOutputStream.write(Base64.getDecoder().decode(cachedFileContent));
            }

            return file;
        }

        /**
         * Description: 获取文件的字节数组
         *
         * @param fileName (Required) 文件名
         * @return byte[] 文件的字节数组
         */
        public byte[] getBytes(String fileName) {
            final HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();

            final Map<Object, Object> entries = ops.entries(CACHE_KEY_PREFIX + Objects.requireNonNull(fileName, "文件名不能为空"));
            return Base64.getDecoder().decode(MapUtils.getString(entries, FIELD_FILE_CONTENT));
        }

    }


}