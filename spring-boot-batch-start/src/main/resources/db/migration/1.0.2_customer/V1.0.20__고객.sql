create table customer
(
    customer_id int unsigned auto_increment primary key,
    store_id    int unsigned                         not null,
    first_name  varchar(45)                          not null,
    last_name   varchar(45)                          not null,
    email       varchar(50)                          null,
    address_id  int unsigned                         not null,
    active      tinyint(1) default 1                 not null,
    create_date datetime                             not null,
    last_update timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    charset = utf8mb3;

create index idx_fk_address_id on customer (address_id);
create index idx_fk_store_id on customer (store_id);
create index idx_last_name on customer (last_name);
