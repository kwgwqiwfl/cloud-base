package com.ring.welkin.common.quartz.scheduler;

import com.ring.welkin.common.quartz.scheduler.listener.QuartzJobListener;
import com.ring.welkin.common.quartz.scheduler.listener.QuartzSchedulerListener;
import com.ring.welkin.common.quartz.scheduler.listener.QuartzTriggerListener;
import lombok.extern.slf4j.Slf4j;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
public class QuartzConfig {

	@Bean
	@Order(1)
	public SchedulerFactoryBeanCustomizer jobFactoryCustomizer(QuartzJobFactory jobFactory) {
		return (schedulerFactoryBean) -> {
			if (jobFactory != null) {
				schedulerFactoryBean.setJobFactory(jobFactory);
			}
		};
	}

	@Bean
	public Scheduler scheduler(Scheduler scheduler) throws Exception {
		try {
			ListenerManager listenerManager = scheduler.getListenerManager();
			listenerManager.addSchedulerListener(new QuartzSchedulerListener());
			listenerManager.addJobListener(new QuartzJobListener());
			listenerManager.addTriggerListener(new QuartzTriggerListener());
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
		return scheduler;
	}
}
