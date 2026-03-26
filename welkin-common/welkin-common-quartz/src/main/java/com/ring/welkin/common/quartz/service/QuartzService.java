package com.ring.welkin.common.quartz.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ring.welkin.common.quartz.scheduler.QuartzJobTrigger;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface QuartzService {

    /**
     * 提交一个调度任务
     *
     * @param jobDetail 任务信息
     * @param trigger   触发信息
     * @return 下一个触发时间
     */
    Date scheduleJob(JobDetail jobDetail, Trigger trigger);

    /**
     * 提交一个调度任务
     *
     * @param trigger 触发信息
     * @return 下一个触发时间
     */
    Date scheduleJob(Trigger trigger);

    /**
     * 提交一个调度任务
     *
     * @param trigger 触发信息
     * @return 下一个触发时间
     */
    Date scheduleJob(QuartzJobTrigger jobTrigger);

    /**
     * 增加一个job
     *
     * @param jobClass          任务实现类
     * @param jobName           任务名称
     * @param jobGroupName      任务组名
     * @param intervalInSeconds 时间表达式 (这是每隔多少秒为一次任务)
     * @param repeatCount       运行的次数 （<0:表示不限次数）
     * @param jobData           参数
     * @return 下一个触发时间
     */
    Date addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int intervalInSeconds, int repeatCount,
                Map<String, Object> jobData);

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称(建议唯一)
     * @param jobGroupName 任务组名
     * @param jobCron      时间表达式 （如：0/5 * * * * ? ）
     * @param jobData      参数
     * @return 下一个触发时间
     */
    Date addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobCron, Map<String, Object> jobData);

    /**
     * 修改 一个job的 时间表达式
     *
     * @param triggerKey 触发器键
     * @param jobCron    时间表达式 （如：0/5 * * * * ? ）
     * @return 下一个触发时间
     */
    Date updateJob(TriggerKey triggerKey, String jobCron);

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName      任务名称(建议唯一)
     * @param jobGroupName 任务组名
     * @param jobCron      时间表达式 （如：0/5 * * * * ? ）
     * @return 下一个触发时间
     */
    Date updateJob(String jobName, String jobGroupName, String jobCron);

    /**
     * 删除任务一个job
     *
     * @param jobKey 任务键
     * @return 删除结果
     */
    boolean deleteJob(JobKey jobKey);

    /**
     * 删除任务一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @return 删除结果
     */
    boolean deleteJob(String jobName, String jobGroupName);

    /**
     * 暂停一个job
     * 
     * @param jobKey 任务键
     */
    void pauseJob(JobKey jobKey);

    /**
     * 暂停一个job
     * 
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void pauseJob(String jobName, String jobGroupName);

    /**
     * 恢复一个job
     *
     * @param jobKey 任务键
     */
    void resumeJob(JobKey jobKey);

    /**
     * 恢复一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void resumeJob(String jobName, String jobGroupName);

    /**
     * 立即执行一个job
     *
     * @param jobKey 任务key
     */
    void runAJobNow(JobKey jobKey);

    /**
     * 立即执行一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void runAJobNow(String jobName, String jobGroupName);

    /**
     * 检查任务是否存在
     * 
     * @param jobKey 任务键
     * @return 是否存在
     */
    boolean checkExists(JobKey jobKey);

    /**
     * 检查触发器是否存在
     * 
     * @param triggerKey 触发键
     * @return 是否存在
     */
    boolean checkExists(TriggerKey triggerKey);

    /**
     * 检查任务是否存在
     * 
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @return 是否存在
     */
    boolean checkExistsByJobKey(String jobName, String jobGroupName);

    /**
     * 检查任务是否存在
     * 
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @return 是否存在
     */
    boolean checkExistsByTriggerKey(String jobName, String jobGroupName);

    /**
     * 获取所有计划中的任务列表
     *
     * @return 任务列表
     */
    List<TriggerInfo> queryAllJob();

    /**
     * 获取所有正在运行的job
     *
     * @return 任务列表
     */
    List<TriggerInfo> queryRunJob();

    @Setter
    @Getter
    @Builder
    public static class TriggerInfo implements Serializable {
        private static final long serialVersionUID = 6399976752529676165L;

        @ApiModelProperty(value = "任务分组")
        private String jobGroupName;
        @ApiModelProperty(value = "任务名称")
        private String jobName;
        @ApiModelProperty(value = "任务触发器")
        private String triggerKey;
        @ApiModelProperty(value = "任务状态")
        private TriggerState triggerState;
        @ApiModelProperty(value = "任务调度时间表达式")
        private String jobCron;

        @ApiModelProperty(value = "开始时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        public Date startTime;
        @ApiModelProperty(value = "结束时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        public Date endTime;
        @ApiModelProperty(value = "描述")
        public String description;

    }

}