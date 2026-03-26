package com.ring.welkin.common.quartz.scheduler;

import com.ring.welkin.common.persistence.entity.base.CommonEntity;
import com.ring.welkin.common.quartz.service.QuartzService;
import com.ring.welkin.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractQuartzJobScheduler<ID extends Serializable, T extends CommonEntity<ID>> implements QuartzJobScheduler<T> {
    @Autowired
    protected QuartzService quartzService;

    protected JobKey generateJobKey(T t, String... jobTypes) {
        if (ArrayUtils.isNotEmpty(jobTypes)) {
            String suffix = Arrays.stream(jobTypes).collect(Collectors.joining(NAMING_SEPARATOR));
            return new JobKey(generateJobName(t.getClass().getSimpleName().toLowerCase(), suffix, t.getId().toString()), generateGroupName(t.getTenantId()));
        }
        return new JobKey(generateJobName(t.getClass().getSimpleName().toLowerCase(), t.getId().toString()), generateGroupName(t.getTenantId()));
    }

    protected TriggerKey generateTriggerKey(T t, String... jobTypes) {
        if (ArrayUtils.isNotEmpty(jobTypes)) {
            String suffix = Arrays.stream(jobTypes).collect(Collectors.joining(NAMING_SEPARATOR));
            return new TriggerKey(generateJobName(t.getClass().getSimpleName().toLowerCase(), suffix, t.getId().toString()),
                    generateGroupName(t.getTenantId()));
        }
        return new TriggerKey(generateJobName(t.getClass().getSimpleName().toLowerCase(), t.getId().toString()), generateGroupName(t.getTenantId()));
    }

    /**
     * 检查触发器是否存在
     *
     * @param t       生成调度任务的元数据对象
     * @param replace 如果任务存在是否替换
     * @return 触发器键
     * @throws SchedulerException
     */
    protected TriggerKey checkTriggerExists(T t, boolean replace) throws SchedulerException {
        TriggerKey triggerKey = generateTriggerKey(t);
        log.debug("begin to run scheduler: {} for {}: {}", triggerKey, t.getClass().getSimpleName(), t.getId());
        // 判断如果存在则重新调度以适配调度内容的修改
        if (quartzService.checkExists(triggerKey)) {
            // 强制更新则先取消原来的调度，重新提交
            if (replace) {
                log.debug("cancel schedule and replace trigger: {}", triggerKey.getName());
                cancel(t);
            } else {
                log.debug("trigger exist and skiped: {}", triggerKey.getName());
                return null;
            }
        }
        return triggerKey;
    }

    public boolean checkExists(T t) {
        return quartzService.checkExists(generateTriggerKey(t));
    }

    /**
     * 提交一个调度任务
     *
     * @param jobClass   调度任务类
     * @param triggerKey 触发器键
     * @param info       调度任务配置信息
     * @throws SchedulerException
     */
    protected void submitSchedule(Class<? extends QuartzJobBean> jobClass, TriggerKey triggerKey, SchedulerInfo info) throws SchedulerException {
        QuartzJobTrigger buildTrigger = buildTrigger(jobClass, triggerKey, info);
        log.debug("QuartzJobTrigger => {}", buildTrigger);
		quartzService.scheduleJob(buildTrigger);
        log.debug("success to run scheduler: triggerKey => {}", triggerKey);
    }

    @Override
    public void schedule(T t, Class<? extends QuartzJobBean> jobClass, boolean replace, Map<String, Object> additionalJdm) {
        try {
            // 检查调度是否存在，如果存在是否需要重新调度
            TriggerKey triggerKey = checkTriggerExists(t, replace);
            if (triggerKey == null) {
                return;
            }

            log.debug("start to schedule {}", triggerKey.getName());
            SchedulerInfo info = generateSchedulerInfo(t, jobClass, triggerKey, additionalJdm);
            log.info("Scheduler info => {}", JsonUtils.toJson(info));
            if (info == null) {
                log.warn("skip to run scheduler: triggerKey => {} ", triggerKey);
                return;
            }

            // 追加元数据
            if (additionalJdm != null && !additionalJdm.isEmpty()) {
            	log.info("Scheduler additionalJdm => {}", additionalJdm);
                info.addAdditionalJdm(additionalJdm);
            }

            // 提交任务
            submitSchedule(jobClass, triggerKey, info);
        } catch (Exception e) {
            log.error("schedule to run failed", e);
            throw new RuntimeException("schedule to run failed", e);
        }
    }

    /**
     * 生成调度任务元数据
     *
     * @param t             任务元数据对象
     * @param jobClass      任务类
     * @param additionalJdm 追加的元数据
     * @param triggerKey    触发键
     * @return 调度任务执行的元数据
     */
    protected abstract SchedulerInfo generateSchedulerInfo(T t, Class<? extends QuartzJobBean> jobClass, TriggerKey triggerKey,
                                                           Map<String, Object> additionalJdm);

    /**
     * 删除任务调度
     *
     * @param t 调度任务元数据
     */
    public void cancel(T t) {
        JobKey jobKey = generateJobKey(t);
        try {
            log.debug("try to cancel scheduler {}", jobKey.toString());
            quartzService.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("cancel job " + jobKey.toString() + " failed.", e);
        }
    }
}
