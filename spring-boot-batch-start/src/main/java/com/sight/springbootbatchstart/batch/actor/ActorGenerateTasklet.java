package com.sight.springbootbatchstart.batch.actor;

import com.sight.springbootbatchstart.actor.ActorRepository;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.jooq.generated.tables.pojos.Actor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ActorGenerateTasklet implements Tasklet {

    private final ActorGeneratorJobParameter jobParameter;
    private final ActorRepository actorRepository;

    public ActorGenerateTasklet(ActorGeneratorJobParameter jobParameter, ActorRepository actorRepository) {
        this.jobParameter = jobParameter;
        this.actorRepository = actorRepository;
    }

    @Override
    public RepeatStatus execute(@Nonnull StepContribution contribution, @Nonnull ChunkContext chunkContext) {
        log.info("배우 데이터 생성");

        List<Actor> actors = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        Faker faker = new Faker();

        long actorCount = jobParameter.actorCount();
        for (long i = 0; i < actorCount; i++) {
            Name name = faker.name();
            actors.add(
                    new Actor()
                            .setFirstName(name.firstName())
                            .setLastName(name.lastName())
                            .setLastUpdate(now)
            );
        }

        actorRepository.insert(actors);

        log.info("{}건의 배우가 생성됨, jobParam: {}", actorCount ,jobParameter);
        return RepeatStatus.FINISHED;
    }
}
