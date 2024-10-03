create table actor
(
    actor_id    int unsigned auto_increment primary key,
    first_name  varchar(45)                         not null comment '배우의 이름',
    last_name   varchar(45)                         not null comment '배우의 성',
    last_update timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) CHARSET=UTF8 COMMENT='배우';

create index idx_actor_last_name on actor (last_name);

create table category
(
    category_id int unsigned auto_increment primary key,
    name        varchar(25)                         not null comment '영화 카테고리 이름',
    last_update timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) CHARSET=UTF8 COMMENT='영화 카테고리';

create table language
(
    language_id int unsigned auto_increment primary key,
    name        char(20)                            not null comment '영화 언어명',
    last_update timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) CHARSET=UTF8 COMMENT='언어';

create table film
(
    film_id              int unsigned auto_increment primary key,
    title                varchar(255)                                                            not null comment '영화 제목',
    description          text                                                                    null comment '영화 설명',
    release_year         year                                                                    null comment '개봉 연도',
    language_id          int unsigned                                                            not null comment '상영 영화 언어',
    original_language_id int unsigned                                                            null comment '원본 영화 언어',
    rental_duration      tinyint unsigned                        default '3'                     not null comment '대여 가능일자',
    rental_rate          decimal(4, 2)                           default 4.99                    not null comment '대여료',
    length               smallint unsigned                                                       null comment '상영 시간 (분)',
    replacement_cost     decimal(5, 2)                           default 19.99                   not null comment '교체 비용',
    rating               enum ('G', 'PG', 'PG-13', 'R', 'NC-17') default 'G'                     null comment '영화 등급',
    special_features     set ('Trailers', 'Commentaries', 'Deleted Scenes', 'Behind the Scenes') null comment '영화 특수분류',
    last_update          timestamp                               default CURRENT_TIMESTAMP       not null on update CURRENT_TIMESTAMP,
    constraint fk_film_language foreign key (language_id) references language (language_id) on update cascade,
    constraint fk_film_language_original foreign key (original_language_id) references language (language_id) on update cascade
) CHARSET=UTF8 COMMENT='영화';

create index idx_fk_language_id on film (language_id);
create index idx_fk_original_language_id on film (original_language_id);
create index idx_title on film (title);

create table film_actor
(
    actor_id    int unsigned                        not null,
    film_id     int unsigned                        not null,
    last_update timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    primary key (actor_id, film_id),
    constraint fk_film_actor_actor foreign key (actor_id) references actor (actor_id) on update cascade,
    constraint fk_film_actor_film foreign key (film_id) references film (film_id) on update cascade
) CHARSET=UTF8 COMMENT='영화 배역';

create index idx_fk_film_id on film_actor (film_id);

create table film_category
(
    film_id     int unsigned                        not null,
    category_id int unsigned                        not null,
    last_update timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    primary key (film_id, category_id),
    constraint fk_film_category_category foreign key (category_id) references category (category_id) on update cascade,
    constraint fk_film_category_film foreign key (film_id) references film (film_id) on update cascade
) CHARSET=UTF8 COMMENT='영화 카테고리';

