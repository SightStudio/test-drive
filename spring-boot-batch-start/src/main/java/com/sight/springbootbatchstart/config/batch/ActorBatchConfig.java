package com.sight.springbootbatchstart.config.batch;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ActorBatchConfig {

    private final DSLContext dslContext;

    @Bean
    public Job myJob(JobRepository jobRepository, Step actorCreateStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(actorCreateStep)
                .build();
    }

    @Bean
    public Step actorCreateStep(JobRepository jobRepository, Tasklet batchTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("actorCreateStep", jobRepository)
                .tasklet(batchTasklet, transactionManager)
                .startLimit(1)
                .build();
    }

    @Bean
    public Tasklet batchTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Batch tasklet executed");
            JActor actor = JActor.ACTOR;

            dslContext.selectFrom(actor).fetch();
            return RepeatStatus.FINISHED;
        };
    }
}
