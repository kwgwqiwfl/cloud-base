package com.ring.welkin.common.quartz.scheduler;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Builder
public class SchedulerInfo implements Serializable {
    private static final long serialVersionUID = 3757431117345157662L;

    /**
     * 调度任务元数据
     */
    private JobDataMap jdm;

    /**
     * 调度开始时间，默认为当前系统时间
     */
    @Default
    private Date startAt = new Date();

    /**
     * 调度结束时间，默认为空
     */
    private Date endAt;

    /**
     * 调度周期表达式，默认为空
     */
    private String cron;

    /**
     * 追加配置信息
     *
     * @param additionalJdm 追加的配置信息
     * @return 返回追加后的对象
     */
    public SchedulerInfo addAdditionalJdm(Map<String, Object> additionalJdm) {
        if (jdm == null) {
            jdm = new JobDataMap();
        }
        jdm.putAll(additionalJdm);
        return this;
    }

    public static SchedulerInfo build(JobDataMap jdm, Date startAt, Date endAt, String cron) {
        SchedulerInfoBuilder builder = SchedulerInfo.builder().jdm(jdm);
        if (startAt != null) {
            builder.startAt(startAt);
        }
        if (endAt != null) {
            builder.endAt(endAt);
        }
        if (StringUtils.isNotBlank(cron)) {
            builder.cron(cron);
        }
        return builder.build();
    }

    public static SchedulerInfo build(JobDataMap jdm, Long startAt, Long endAt, String cron) {
        SchedulerInfoBuilder builder = SchedulerInfo.builder().jdm(jdm);
        if (startAt != null && startAt >= 0) {
            builder.startAt(new Date(startAt));
        }
        if (endAt != null && endAt >= 0) {
            builder.endAt(new Date(endAt));
        }
        if (StringUtils.isNotBlank(cron)) {
            builder.cron(cron);
        }
        return builder.build();
    }

}
