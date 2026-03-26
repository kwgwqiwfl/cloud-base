package com.ring.welkin.common.quartz.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
public class QuartzSchedulerListener implements SchedulerListener {
	// ----------------job相关---------------------
	// 添加job时触发
	@Override
	public void jobAdded(JobDetail arg0) {
		log.debug("jobAdded => {}", arg0.getKey().getGroup() + "-" + arg0.getKey().getName());
	}

	// 删除job时触发
	@Override
	public void jobDeleted(JobKey arg0) {
        log.debug("jobDeleted => {}", arg0.getGroup() + "-" + arg0.getName());
	}

	// 挂起job时触发（暂停）
	@Override
	public void jobPaused(JobKey arg0) {
        log.debug("jobPaused => {}", arg0.getGroup() + "-" + arg0.getName());
	}

	// 恢复job时触发（继续）
	@Override
	public void jobResumed(JobKey arg0) {
        log.debug("jobResumed => {}", arg0.getGroup() + "-" + arg0.getName());
	}

	// 部署job时触发
	@Override
	public void jobScheduled(Trigger arg0) {
        log.debug("jobScheduled => {}", arg0.getKey().getGroup() + "-" + arg0.getKey().getName());
	}

	// 卸载job时触发
	@Override
	public void jobUnscheduled(TriggerKey arg0) {
        log.debug("jobUnscheduled => {}", arg0.getGroup() + "-" + arg0.getName());
	}

	// 暂停job group时触发
	@Override
	public void jobsPaused(String arg0) {
        log.debug("jobsPaused => {}", arg0);
	}

	// 恢复job group时触发
	@Override
	public void jobsResumed(String arg0) {
        log.debug("jobsResumed => {}", arg0);
	}

	// ----------------trigger相关---------------------
	// 暂停trigger时触发
	@Override
	public void triggerPaused(TriggerKey arg0) {
        log.debug("triggerPaused => {}", arg0.getGroup() + "-" + arg0.getName());
	}

	// 恢复trigger时触发
	@Override
	public void triggerResumed(TriggerKey arg0) {
        log.debug("triggerResumed => {}", arg0.getGroup() + "-" + arg0.getName());

	}

	// 暂停group所有trigger时触发
	@Override
	public void triggersPaused(String arg0) {
        log.debug("triggersPaused => {}", arg0);
	}

	// 恢复group所有trigger时触发
	@Override
	public void triggersResumed(String arg0) {
        log.debug("triggersResumed => {}", arg0);
	}

	// 完成trigger时触发
	@Override
	public void triggerFinalized(Trigger trigger) {
        log.debug("triggerFinalized => {}", trigger);
	}

	// ----------------scheduler相关---------------------
	// Scheduler开始启动时触发
	@Override
	public void schedulerStarting() {
        log.debug("scheduler starting ...");
	}

	// Scheduler启动完成时触发
	@Override
	public void schedulerStarted() {
        log.debug("scheduler started.");
	}

	// Scheduler开始关闭时触发
	@Override
	public void schedulerShuttingdown() {
        log.debug("scheduler shutting down ...");
	}

	// Scheduler关闭完成时触发
	@Override
	public void schedulerShutdown() {
        log.debug("scheduler shutdown.");
	}

	// Scheduler异常时触发
	// Scheduler 的正常运行期间产生一个严重错误时调用这个方法。
	// 错误的类型会各式的，但是下面列举了一些错误例子：
	// 初始化 Job 类的问题,试图去找到下一 Trigger 的问题
	// JobStore 中重复的问题
	// 数据存储连接的问题
	// 我们可以使用 SchedulerException 的 getErrorCode() 或者 getUnderlyingException()
	// 方法或获取到特定错误的更详尽的信息。
	@Override
	public void schedulerError(String arg0, SchedulerException arg1) {
        log.warn("schedulerError => " + arg0, arg1);
	}

	// scheduler清理数据时触发
	@Override
	public void schedulingDataCleared() {
        log.debug("scheduling data cleared.");
	}

	// 系统关闭时会触发
	@Override
	public void schedulerInStandbyMode() {
        log.debug("scheduler in standby mode.");
	}
}
