package com.sight.springbootbatchstart.batch.film;

import com.sight.springbootbatchstart.config.batch.JooqPagingQueryProviderFactory;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.generated.tables.JFilm;
import org.jooq.impl.QOM;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FilmGenerateBatchConfig {

//    private static final String BATCH_NAME = "filmGenerate";
//    private final DSLContext dslContext;
//
//    @Bean(BATCH_NAME + "jobParam")
//    @JobScope
//    public FilmGenerateJobParameter jobParameter(
//            @Value("#{jobParameters[filmCount]}") Long filmCount
//    ) {
//        return new FilmGenerateJobParameter(filmCount);
//    }
//
//    @Bean
//    public Job filmGenerateJob(JobRepository jobRepository, @Qualifier("filmGeneratestep") Step filmGenerateStep) {
//        return new JobBuilder(BATCH_NAME + "job", jobRepository)
//                .start(filmGenerateStep)
//                .build();
//    }
//
//    @Bean
//    public Step filmGeneratestep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder(BATCH_NAME + "step", jobRepository)
//                .chunk(10, platformTransactionManager)
//                .reader(new FilmGenerateReader())
////                .processor(new FilmGenerateProcessor())
////                .writer(new FilmGenerateWriter())
//                .build();
//    }
//
//    @Bean
//    public PagingQueryProvider pagingQueryProvider() {
//        JFilm film = JFilm.FILM;
//        Select<?> dsl = dslContext.selectFrom(JFilm.FILM)
//                .where(film.FILM_ID.eq(1L));
//
//        dsl.$select();
//        dsl.$from();
//        dsl.$where();
//
//        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
//        queryProvider.setDataSource(dslContext.parsingDataSource());
//        queryProvider.setSelectClause(dslContext.selectClause());
//
////        JooqPagingQueryProviderFactory factory = new JooqPagingQueryProviderFactory(dslContext);
//
//        return factory.query();
//    }
}
