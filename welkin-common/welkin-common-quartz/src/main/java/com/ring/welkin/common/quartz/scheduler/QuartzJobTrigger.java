package com.ring.welkin.common.quartz.scheduler;

import lombok.Builder;
import lombok.Data;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.io.Serializable;

/**
 * 触发器定义信息
 *
 * @author cloud
 * @date 2023年1月16日 上午10:54:54
 */
@Data
@Builder
public class QuartzJobTrigger implements Serializable {
    private static final long serialVersionUID = 4678940424848916018L;

    /**
     * 任务详情
     */
    private JobDetail jobDetail;

    /**
     * 触发器详情
     */
    private Trigger trigger;
}
