package com.ring.welkin.common.quartz.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

@Slf4j
public class QuartzTriggerListener implements TriggerListener {

	@Override
	public String getName() {// 定义该监听器名称
		return "QuartzTriggerListener";
	}// 定义该listener的名字

	// 激发trigger，与该trigger关联的job也将被运行
	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("triggerFired => {}",
				context.getJobDetail().getKey().getGroup() + "-" + context.getJobDetail().getKey().getName());
    }

	// 在trigger激发后，job执行前,否决job执行（返回false通过，返回true否决）
	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		log.debug("vetoJobExecution => {}",
				context.getJobDetail().getKey().getGroup() + "-" + context.getJobDetail().getKey().getName());
        return false;
    }

	// 完成trigger时触发（代表完成job）
	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			Trigger.CompletedExecutionInstruction triggerInstructionCode) {
		log.debug("triggerComplete => {}",
				context.getJobDetail().getKey().getGroup() + "-" + context.getJobDetail().getKey().getName());
    }

	// 错过trigger激发时执行，如当前时间有很多触发器都需要执行，但是线程池中的有效线程都在工作，导致任务等待时错过执行时间
	@Override
	public void triggerMisfired(Trigger trigger) {
		log.debug("triggerMisfired => {}", trigger.getKey().getGroup() + "-" + trigger.getKey().getGroup());
	}
}