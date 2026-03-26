package com.ring.welkin.common.quartz.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
public class QuartzJobListener implements JobListener {

	@Override
	public String getName() {// 定义该监听器名称
		return "QuartzJobListener";
	}

	// 执行jobDetail前，jobDetail被否决时触发（JobDetail即将被执行，但又被TriggerListener的vetoJobExecution()否决时，触发该方法）
	@Override
	public void jobExecutionVetoed(JobExecutionContext arg0) {
		log.debug("jobExecutionVetoed => {}",
				arg0.getJobDetail().getKey().getGroup() + "-" + arg0.getJobDetail().getKey().getName());
	}

	// 将要执行jobDetail时触发
	@Override
	public void jobToBeExecuted(JobExecutionContext arg0) {
		log.debug("jobToBeExecuted => {}",
				arg0.getJobDetail().getKey().getGroup() + "-" + arg0.getJobDetail().getKey().getName());
	}

	// 执行完jobDetail时触发
	@Override
	public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
		log.debug("jobWasExecuted => {}",
				arg0.getJobDetail().getKey().getGroup() + "-" + arg0.getJobDetail().getKey().getName());
	}
}