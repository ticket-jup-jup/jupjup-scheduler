package org.example.jupjupscheduler;

import org.example.jupjupscheduler.client.JupjupApiClient;
import org.example.jupjupscheduler.job.SchedulerJob;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class JupjupSchedulerApplicationTests {

    @MockitoBean
    private JupjupApiClient jupjupApiClient;

    @MockitoBean
    private SchedulerJob schedulerJob;

    @Test
    void contextLoads() {
    }
}