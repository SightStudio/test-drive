package com.sight.springbootbatchstart.batch.actor;

import com.sight.springbootbatchstart.actor.ActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ActorGeneratorBatchConfig {

    private static final String BATCH_NAME = "actorCreate";

    @Bean(BATCH_NAME + "jobParameter")
    @JobScope
    public ActorGeneratorJobParameter jobParameter(
            @Value("#{jobParameters[invokeDateTime]}") LocalDateTime invokeDateTime,
            @Value("#{jobParameters[actorCount]}") Long actorCount
    ) {
        return new ActorGeneratorJobParameterImpl(invokeDateTime, actorCount);
    }

    @Bean
    public Job actorCreateJob(JobRepository jobRepository, @Qualifier("actorCreateStep") Step actorCreateStep) {
        return new JobBuilder(BATCH_NAME + "Job", jobRepository)
                .start(actorCreateStep)
                .build();
    }

    @Bean
    protected Step actorCreateStep(JobRepository jobRepository, Tasklet actorGenerateTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder(BATCH_NAME + "Step", jobRepository)
                .tasklet(actorGenerateTasklet, transactionManager)
                .startLimit(1)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet actorCreateTasklet(ActorGeneratorJobParameter jobParameter, ActorRepository actorRepository) {
        return new ActorGenerateTasklet(jobParameter, actorRepository);
    }
}
