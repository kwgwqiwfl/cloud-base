package com.ring.welkin.common.quartz;

import com.ring.welkin.common.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

//@Configuration
public class ScheduledQuartzConfig {

    @Autowired
    QuartzService quartzService;

    @Bean
    public void job3() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", 3);
        quartzService.deleteJob("job3", "SimpleJob");
        quartzService.addJob(SimpleJob.class, "job3", "SimpleJob", "15 * * * * ?", map);
    }

    @Bean
    public void job4() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", 13);
        quartzService.deleteJob("job4", "SimpleJob");
        quartzService.addJob(SimpleJob.class, "job4", "SimpleJob", "15 * * * * ?", map);
    }

    @Bean
    public void job5() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", 14);
        quartzService.deleteJob("job5", "SimpleJob");
        quartzService.addJob(SimpleJob.class, "job5", "SimpleJob", "15 * * * * ?", map);
    }
}
