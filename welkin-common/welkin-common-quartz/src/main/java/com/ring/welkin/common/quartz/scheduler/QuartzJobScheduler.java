package com.ring.welkin.common.quartz.scheduler;

import com.ring.welkin.common.utils.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基于某一个bean对象的调度接口
 *
 * @param <T> bean类型
 * @author cloud
 * @date 2023年1月12日 上午10:25:01
 */
public interface QuartzJobScheduler<T> {
    public static final String NAMING_PREFIX = "tg";
    public static final String NAMING_SEPARATOR = CharUtils.UNDERLINE;

    /**
     * 生成任务分组名称
     *
     * @param identifications 追加标识
     * @return 生成的分组名称，格式如：tg_{identifications[0]}_{identifications[1]}...
     */
    default String generateGroupName(String... identifications) {
        return StringUtils.joinWith(NAMING_SEPARATOR, NAMING_PREFIX, Stream.of(identifications).collect(Collectors.joining(NAMING_SEPARATOR)));
    }

    /**
     * 生成任务名称
     *
     * @param identifications 追加标识
     * @return 最终任务名称:{identifications[0]}_{identifications[1]}...
     */
    default String generateJobName(String... identifications) {
        return StringUtils.joinWith(NAMING_SEPARATOR, NAMING_PREFIX, Stream.of(identifications).collect(Collectors.joining(NAMING_SEPARATOR)));
    }

    /**
     * 提交一个调度任务
     *
     * @param t          生成调度任务的元数据对象
     * @param jobClass   调度任务类
     * @param triggerKey 触发器键
     * @param jdm        调度任务配置信息
     * @param startAt    开始执行时间，可以为空，默认是当前时间
     * @param endAt      结束执行时间，可以为空
     * @param cron       调度的cron表达式，可以为空，为空时为单次调度
     * @throws SchedulerException
     */
    default QuartzJobTrigger buildTrigger(Class<? extends QuartzJobBean> jobClass, TriggerKey triggerKey, JobDataMap jdm, Date startAt, Date endAt,
                                          ScheduleBuilder<?> scheduleBuilder) throws SchedulerException {
        // 构建调度任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(triggerKey.getName(), triggerKey.getGroup()).usingJobData(jdm).build();
        // 构建调度触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobDetail);
        if (startAt != null) {
            triggerBuilder.startAt(startAt);
        }
        if (endAt != null) {
            triggerBuilder.endAt(endAt);
        }
        if (scheduleBuilder != null) {
            triggerBuilder.withSchedule(scheduleBuilder);
        }
        return QuartzJobTrigger.builder().jobDetail(jobDetail).trigger(triggerBuilder.build()).build();
    }

    default QuartzJobTrigger buildTrigger(Class<? extends QuartzJobBean> jobClass, TriggerKey triggerKey, SchedulerInfo info) throws SchedulerException {
        ScheduleBuilder<?> scheduleBuilder = null;
        String cron = info.getCron();
        if (StringUtils.isNotEmpty(cron)) {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        } else {
            scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        }
        return buildTrigger(jobClass, triggerKey, info.getJdm(), info.getStartAt(), info.getEndAt(), scheduleBuilder);
    }

    /**
     * 提交调度任务调度
     *
     * @param t             调度任务元数据
     * @param jobClass      调度任务类
     * @param replace       如果调度存在是否替换
     * @param additionalJdm 额外追加的元数据信息
     */
    void schedule(T t, Class<? extends QuartzJobBean> jobClass, boolean replace, Map<String, Object> additionalJdm);

    /**
     * 提交调度任务调度
     *
     * @param t        调度任务元数据
     * @param jobClass 调度任务类
     * @param replace  如果调度存在是否替换
     */
    default void schedule(T t, Class<? extends QuartzJobBean> jobClass, boolean replace) {
        schedule(t, jobClass, replace, null);
    }

    /**
     * 提交调度任务调度
     *
     * @param t        调度任务元数据对象
     * @param jobClass 调度任务类
     */
    default void schedule(T t, Class<? extends QuartzJobBean> jobClass) {
        schedule(t, jobClass, true);
    }

    /***
     * 取消调度任务
     *
     * @param t 调度任务元数据对象
     */
    void cancel(T t);
}
