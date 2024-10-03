package com.sight.springbootbatchstart;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class SpringBootBatchStartApplicationTests {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job myJob;

    @Test
    void 테스트() throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLocalDateTime("time", LocalDateTime.now());
        jobLauncher.run(myJob, jobParametersBuilder.toJobParameters());
    }
}
