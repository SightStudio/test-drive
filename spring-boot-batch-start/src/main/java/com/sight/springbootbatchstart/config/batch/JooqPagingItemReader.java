package com.sight.springbootbatchstart.config.batch;

import lombok.RequiredArgsConstructor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;

import javax.sql.DataSource;

public class JooqPagingItemReader<T> extends JdbcPagingItemReader<T> {

    private final DSLContext dslContext;

    public JooqPagingItemReader(DSLContext dslContext) {
        MySqlPagingQueryProvider
        this.dslContext = dslContext;
        this.setDataSource(dslContext.parsingDataSource());
    }

}
