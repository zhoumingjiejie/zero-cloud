package com.github.icezerocat.task.schedule.web.controller;

import com.github.icezerocat.task.schedule.thread.ScheduleTask;
import com.github.icezerocat.task.schedule.utils.ScheduleUtil;
import com.github.icezerocat.zerocore.config.ApplicationContextZeroHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Description: schedule-CRUD控制器
 * CreateDate:  2020/11/22 16:03
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("schedule")
public class ScheduleController {

    /**
     * @param beanName beanName
     * @param corn     定时表达式
     * @param zone     地区
     * @return 操作结果boolean
     */
    @PostMapping("saveOrUpdate")
    public boolean saveOrUpdate(String beanName, String corn, @RequestParam(required = false) String zone) {
        boolean resultBl;
        ScheduleTask scheduleTask = ApplicationContextZeroHelper.getBean(beanName, ScheduleTask.class);
        if (StringUtils.isNotBlank(zone)) {
            resultBl = ScheduleUtil.reset(scheduleTask, corn, zone);
        } else {
            resultBl = ScheduleUtil.reset(scheduleTask, corn);
        }
        return resultBl;
    }

    /**
     * 取消定时任务
     *
     * @param beanName bean名字
     * @return 操作结果boolean
     */
    @DeleteMapping("cancel")
    public boolean cancel(String beanName) {
        return ScheduleUtil.cancel(ApplicationContextZeroHelper.getBean(beanName, ScheduleTask.class));
    }
}
