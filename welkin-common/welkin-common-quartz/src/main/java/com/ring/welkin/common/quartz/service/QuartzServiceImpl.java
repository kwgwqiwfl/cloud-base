package com.ring.welkin.common.quartz.service;

import com.ring.welkin.common.quartz.scheduler.QuartzJobTrigger;
import com.ring.welkin.common.quartz.service.QuartzService.TriggerInfo.TriggerInfoBuilder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private Scheduler scheduler;

    @Override
    public Date scheduleJob(JobDetail myJob, Trigger trigger) {
        try {
            return scheduler.scheduleJob(myJob, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Date scheduleJob(Trigger trigger) {
        try {
            return scheduler.scheduleJob(trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Date scheduleJob(QuartzJobTrigger jobTrigger) {
        JobDetail jobDetail = jobTrigger.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        if (checkExists(jobKey)) {
            return scheduleJob(jobTrigger.getTrigger());
        }
        return scheduleJob(jobDetail, jobTrigger.getTrigger());
    }

    @Override
    public Date addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int intervalInSeconds, int repeatCount,
                       Map<String, Object> jobData) {
        try {
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 设置job参数
            if (jobData != null && jobData.size() > 0) {
                jobDetail.getJobDataMap().putAll(jobData);
            }
            // 使用simpleTrigger规则
            Trigger trigger = null;
            if (repeatCount < 0) {
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(intervalInSeconds)).startNow().build();
            } else {
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(intervalInSeconds).withRepeatCount(repeatCount))
                        .startNow().build();
            }
            return scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Date addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobCron, Map<String, Object> jobData) {
        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 设置job参数
            if (jobData != null && jobData.size() > 0) {
                jobDetail.getJobDataMap().putAll(jobData);
            }
            // 定义调度触发规则
            // 使用cornTrigger规则
            // 触发器key
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobCron)).startNow().build();
            // 把作业和触发器注册到任务调度中
            return scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Date updateJob(TriggerKey triggerKey, String jobCron) {
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(jobCron)).build();
            // 重启触发器
            return scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Date updateJob(String jobName, String jobGroupName, String jobCron) {
        return updateJob(TriggerKey.triggerKey(jobName, jobGroupName), jobCron);
    }

    @Override
    public boolean deleteJob(JobKey jobKey) {
        try {
            return scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroupName) {
        return deleteJob(new JobKey(jobName, jobGroupName));
    }

    @Override
    public void pauseJob(JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pauseJob(String jobName, String jobGroupName) {
        pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }

    @Override
    public void resumeJob(JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroupName) {
        resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    @Override
    public void runAJobNow(JobKey jobKey) {
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runAJobNow(String jobName, String jobGroupName) {
        runAJobNow(JobKey.jobKey(jobName, jobGroupName));
    }

    @Override
    public boolean checkExists(JobKey jobKey) {
        try {
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkExistsByJobKey(String jobName, String jobGroupName) {
        return checkExists(JobKey.jobKey(jobName, jobGroupName));
    }

    @Override
    public boolean checkExists(TriggerKey triggerKey) {
        try {
            return scheduler.checkExists(triggerKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkExistsByTriggerKey(String jobName, String jobGroupName) {
        return checkExists(TriggerKey.triggerKey(jobName, jobGroupName));
    }

    @Override
    public List<TriggerInfo> queryAllJob() {
        List<TriggerInfo> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<TriggerInfo>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    jobList.add(parseTriggerInfo(jobKey, trigger));
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return jobList;
    }

    @Override
    public List<TriggerInfo> queryRunJob() {
        List<TriggerInfo> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<TriggerInfo>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                JobDetail jobDetail = executingJob.getJobDetail();
                jobList.add(parseTriggerInfo(jobDetail.getKey(), executingJob.getTrigger()));
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return jobList;
    }

    private TriggerInfo parseTriggerInfo(JobKey jobKey, Trigger trigger) throws SchedulerException {
        TriggerInfoBuilder builder = TriggerInfo.builder().jobGroupName(jobKey.getGroup()).jobName(jobKey.getName()).triggerKey(trigger.toString())
                .triggerState(scheduler.getTriggerState(trigger.getKey())).description(trigger.getDescription()).startTime(trigger.getStartTime())
                .endTime(trigger.getEndTime());
        if (trigger instanceof CronTrigger) {
            builder.jobCron(((CronTrigger) trigger).getCronExpression());
        }
        return builder.build();
    }

}
