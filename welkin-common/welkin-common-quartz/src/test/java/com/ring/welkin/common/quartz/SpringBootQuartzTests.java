package com.ring.welkin.common.quartz;

import com.ring.welkin.common.quartz.service.QuartzService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { QuartzTestStarter.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootQuartzTests {
    @Autowired
    private QuartzService quartzService;

    @Test
    public void test1() throws Exception {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", 3);
        quartzService.deleteJob("job3", "SimpleJob");
        quartzService.addJob(SimpleJob.class, "job3", "SimpleJob", "5 * * * * ?", map);

        map.put("name", 13);
        quartzService.deleteJob("job4", "SimpleJob");
        quartzService.addJob(SimpleJob.class, "job4", "SimpleJob", "5 * * * * ?", map);
        map.put("name", 14);
        quartzService.deleteJob("job5", "SimpleJob");
        quartzService.addJob(SimpleJob.class, "job5", "SimpleJob", "5 * * * * ?", map);

        for (;;) {
            Thread.sleep(100000);
            System.out.println("线程睡眠100秒钟");
        }
    }

}
