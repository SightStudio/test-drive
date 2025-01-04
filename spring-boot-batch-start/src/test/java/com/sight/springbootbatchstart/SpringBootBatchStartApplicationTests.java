package com.sight.springbootbatchstart;

import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.generated.tables.JFilm;
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
    Job actorCreateJob;

    @Autowired
    DSLContext dslContext;

    @Test
    void 테스트() throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLocalDateTime("invokeDateTime", LocalDateTime.now());
        jobParametersBuilder.addLong("actorCount", 10L);
        jobLauncher.run(actorCreateJob, jobParametersBuilder.toJobParameters());
    }

    @Test
    void jooq테스트() {

        dslContext.createTable()
        JFilm film = JFilm.FILM;
        Select<?> dsl = dslContext.selectFrom(JFilm.FILM)
                .where(film.FILM_ID.eq(1L));

        String render1 = dslContext.render(dsl.$select());
        String render2 = dslContext.render(dsl.$from());
        String render3 = dslContext.render(dsl.$where());
        System.out.println();
    }
}
