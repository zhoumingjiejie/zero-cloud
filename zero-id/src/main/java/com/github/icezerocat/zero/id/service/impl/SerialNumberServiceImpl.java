package com.github.icezerocat.zero.id.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: TODO
 * CreateDate:  2021/11/30 12:11
 *
 * @author zero
 * @version 1.0
 */
public class SerialNumberServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerialNumberServiceImpl.class);

    /*@Autowired
    private SerialNumberDAO serialNumberDAO;

    @Autowired
    private SerialNumberRepository serialNumberRepository;

    *//**
     * 格式
     *//*
    private String pattern = "";

    *//**
     * 生成器锁
     *//*
    private final ReentrantLock lock = new ReentrantLock();

    *//**
     * 流水号格式化器
     *//*
    private DecimalFormat format = null;

    *//**
     * 预生成锁
     *//*
    private final ReentrantLock prepareLock = new ReentrantLock();

    *//**
     * 最小值
     *//*
    private int min = 0;

    *//**
     * 最大值
     *//*
    private long max = 0;

    *//**
     * 已生成流水号（种子）
     *//*
    private long seed = min;

    *//**
     * 预生成数量
     *//*
    private int prepare = 0;

    *//**
     * 数据库存储的当前最大序列号
     **//*
    long maxSerialInt = 0;

    *//**
     * 当前序列号是否为个位数自增的模式
     **//*
    private String isAutoIncrement = "0";

    SystemSerialNumber systemSerialNumberDTO = new SystemSerialNumber();

    *//**
     * 预生成流水号
     *//*
    HashMap<String, List<String>> prepareSerialNumberMap = new HashMap<>();

    *//**
     * 查询单条序列号配置信息
     *
     * @param systemSerialNumberDTO
     * @return
     *//*
    @Override
    public SystemSerialNumber find(SystemSerialNumber systemSerialNumberDTO) {
        return serialNumberDAO.find(systemSerialNumberDTO);
    }

    *//**
     * 根据模块code生成预数量的序列号存放到Map中
     *
     * @param moduleCode 模块code
     * @return
     *//*
    @CachePut(value = "serialNumber", key = "#moduleCode")
    public List<String> generatePrepareSerialNumbers(String moduleCode) {
        //临时List变量
        List<String> resultList = new ArrayList<String>(prepare);
        lock.lock();
        try {
            for (int i = 0; i < prepare; i++) {
                maxSerialInt = maxSerialInt + 1;
                if (maxSerialInt > min && (maxSerialInt + "").length() < max) {
                    seed = maxSerialInt;
                } else {
                    //如果动态数字长度大于模板中的长度 例：模板CF000  maxSerialInt 1000
                    seed = maxSerialInt = 0;
                    //更新数据，重置maxSerialInt为0
                    systemSerialNumberDTO.setMaxSerial("0");
                    SystemSerialNumber systemSerialNumber = new SystemSerialNumber();
                    BeanUtils.copyProperties(systemSerialNumber, systemSerialNumberDTO);
                    serialNumberRepository.save(systemSerialNumber);
                }
                //动态数字生成
                String formatSerialNum = format.format(seed);

                //动态日期的生成
                if (pattern.contains(SerialNumConstants.DATE_SYMBOL)) {
                    String currentDate = DateUtils.format(new Date(), "yyyyMMdd");
                    formatSerialNum = formatSerialNum.replace(SerialNumConstants.DATE_SYMBOL, currentDate);
                }

                resultList.add(formatSerialNum);
            }
            //更新数据
            systemSerialNumberDTO.setMaxSerial(maxSerialInt + "");
            SystemSerialNumber systemSerialNumber = new SystemSerialNumber();
            BeanUtils.copyProperties(systemSerialNumber, systemSerialNumberDTO);
            serialNumberRepository.save(systemSerialNumber);
        } finally {
            lock.unlock();
        }
        return resultList;
    }

    *//**
     * 根据模块code生成序列号
     *
     * @param moduleCode 模块code
     * @return 序列号
     *//*
    public String generateSerialNumberByModelCode(String moduleCode) {

        //预序列号加锁
        prepareLock.lock();
        try {
            //判断内存中是否还有序列号
            if (null != prepareSerialNumberMap.get(moduleCode) && prepareSerialNumberMap.get(moduleCode).size() > 0) {
                //若有，返回第一个，并删除
                return prepareSerialNumberMap.get(moduleCode).remove(0);
            }
        } finally {
            //预序列号解锁
            prepareLock.unlock();
        }
        systemSerialNumberDTO = new SystemSerialNumberDTO();
        systemSerialNumberDTO.setModuleCode(moduleCode);
        systemSerialNumberDTO = serialNumberDAO.find(systemSerialNumberDTO);
        prepare = Integer.parseInt(systemSerialNumberDTO.getPreMaxNum().trim());//预生成流水号数量
        pattern = systemSerialNumberDTO.getConfigTemplet().trim();//配置模板
        String maxSerial = systemSerialNumberDTO.getMaxSerial().trim(); //存储当前最大值
        isAutoIncrement = systemSerialNumberDTO.getIsAutoIncrement().trim();
        maxSerialInt = Long.parseLong(maxSerial.trim());//数据库存储的最大序列号
        max = this.counter(pattern, '0') + 1;//根据模板判断当前序列号数字的最大值
        if (isAutoIncrement.equals("1")) {
            pattern = pattern.replace("0", "#");
        }
        format = new DecimalFormat(pattern);
        //生成预序列号，存到缓存中
        List<String> resultList = generatePrepareSerialNumbers(moduleCode);
        prepareLock.lock();
        try {
            prepareSerialNumberMap.put(moduleCode, resultList);
            return prepareSerialNumberMap.get(moduleCode).remove(0);
        } finally {
            prepareLock.unlock();
        }
    }

    *//**
     * 设置最小值
     *
     * @param value 最小值，要求：大于等于零
     * @return 流水号生成器实例
     *//*
    public ISerialNumService setMin(int value) {
        lock.lock();
        try {
            this.min = value;
        } finally {
            lock.unlock();
        }
        return this;
    }

    *//**
     * 最大值
     *
     * @param value 最大值，要求：小于等于Long.MAX_VALUE ( 9223372036854775807 )
     * @return 流水号生成器实例
     *//*
    public ISerialNumService setMax(long value) {
        lock.lock();
        try {
            this.max = value;
        } finally {
            lock.unlock();
        }
        return this;
    }

    *//**
     * 设置预生成流水号数量
     *
     * @param count 预生成数量
     * @return 流水号生成器实例
     *//*
    public ISerialNumService setPrepare(int count) {
        lock.lock();
        try {
            this.prepare = count;
        } finally {
            lock.unlock();
        }
        return this;
    }

    *//**
     * 统计某一个字符出现的次数
     *
     * @param str 查找的字符
     * @param c
     * @return
     *//*
    private int counter(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
*/
}
