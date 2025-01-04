package com.sight.springbootbatchstart.batch.film;

import org.jooq.generated.tables.pojos.Film;
import org.springframework.batch.item.database.JdbcPagingItemReader;

public class FilmGenerateReader extends JdbcPagingItemReader<Film> {

}
