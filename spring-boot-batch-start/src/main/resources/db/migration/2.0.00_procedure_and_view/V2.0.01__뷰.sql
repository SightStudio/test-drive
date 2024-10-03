create view actor_info as
select `a`.`actor_id`               AS `actor_id`,
       `a`.`first_name`             AS `first_name`,
       `a`.`last_name`              AS `last_name`,
       group_concat(distinct
                    concat(`c`.`name`, ': ', (select group_concat(`f`.`title` order by `f`.`title` ASC separator ', ')
                                              from ((`film` `f` join `film_category` `fc`
                                                     on ((`f`.`film_id` = `fc`.`film_id`))) join `film_actor` `fa`
                                                    on ((`f`.`film_id` = `fa`.`film_id`)))
                                              where ((`fc`.`category_id` = `c`.`category_id`) and
                                                     (`fa`.`actor_id` = `a`.`actor_id`)))) order by `c`.`name` ASC
                    separator '; ') AS `film_info`
from (((`actor` `a` left join `film_actor` `fa` on ((`a`.`actor_id` = `fa`.`actor_id`))) left join `film_category` `fc`
       on ((`fa`.`film_id` = `fc`.`film_id`))) left join `category` `c` on ((`fc`.`category_id` = `c`.`category_id`)))
group by `a`.`actor_id`, `a`.`first_name`, `a`.`last_name`;

create view customer_list as
select `cu`.`customer_id`                               AS `ID`,
       concat(`cu`.`first_name`, ' ', `cu`.`last_name`) AS `name`,
       `a`.`address`                                    AS `address`,
       `a`.`postal_code`                                AS `zip code`,
       `a`.`phone`                                      AS `phone`,
       `city`.`city`                                    AS `city`,
       `country`.`country`                              AS `country`,
       if(`cu`.`active`, 'active', '')                  AS `notes`,
       `cu`.`store_id`                                  AS `SID`
from (((`customer` `cu` join `address` `a` on ((`cu`.`address_id` = `a`.`address_id`))) join `city`
       on ((`a`.`city_id` = `city`.`city_id`))) join `country` on ((`city`.`country_id` = `country`.`country_id`)));

create view film_list as
select `film`.`film_id`                                                                    AS `FID`,
       `film`.`title`                                                                      AS `title`,
       `film`.`description`                                                                AS `description`,
       `category`.`name`                                                                   AS `category`,
       `film`.`rental_rate`                                                                AS `price`,
       `film`.`length`                                                                     AS `length`,
       `film`.`rating`                                                                     AS `rating`,
       group_concat(concat(`actor`.`first_name`, ' ', `actor`.`last_name`) separator ', ') AS `actors`
from ((((`category` left join `film_category`
         on ((`category`.`category_id` = `film_category`.`category_id`))) left join `film`
        on ((`film_category`.`film_id` = `film`.`film_id`))) join `film_actor`
       on ((`film`.`film_id` = `film_actor`.`film_id`))) join `actor`
      on ((`film_actor`.`actor_id` = `actor`.`actor_id`)))
group by `film`.`film_id`, `film`.`title`, `film`.`description`, `film`.`rental_rate`, `film`.`length`, `film`.`rating`,
         `category`.`name`;

create view nicer_but_slower_film_list as
select `film`.`film_id`     AS `FID`,
       `film`.`title`       AS `title`,
       `film`.`description` AS `description`,
       `category`.`name`    AS `category`,
       `film`.`rental_rate` AS `price`,
       `film`.`length`      AS `length`,
       `film`.`rating`      AS `rating`,
       group_concat(concat(concat(upper(substr(`actor`.`first_name`, 1, 1)),
                                  lower(substr(`actor`.`first_name`, 2, length(`actor`.`first_name`))), ' ',
                                  concat(upper(substr(`actor`.`last_name`, 1, 1)),
                                         lower(substr(`actor`.`last_name`, 2, length(`actor`.`last_name`)))))) separator
                    ', ')   AS `actors`
from ((((`category` left join `film_category`
         on ((`category`.`category_id` = `film_category`.`category_id`))) left join `film`
        on ((`film_category`.`film_id` = `film`.`film_id`))) join `film_actor`
       on ((`film`.`film_id` = `film_actor`.`film_id`))) join `actor`
      on ((`film_actor`.`actor_id` = `actor`.`actor_id`)))
group by `film`.`film_id`, `film`.`title`, `film`.`description`, `film`.`rental_rate`, `film`.`length`, `film`.`rating`,
         `category`.`name`;

create view sales_by_film_category as
select `c`.`name` AS `category`, sum(`p`.`amount`) AS `total_sales`
from (((((`payment` `p` join `rental` `r` on ((`p`.`rental_id` = `r`.`rental_id`))) join `inventory` `i`
         on ((`r`.`inventory_id` = `i`.`inventory_id`))) join `film` `f`
        on ((`i`.`film_id` = `f`.`film_id`))) join `film_category` `fc`
       on ((`f`.`film_id` = `fc`.`film_id`))) join `category` `c` on ((`fc`.`category_id` = `c`.`category_id`)))
group by `c`.`name`
order by `total_sales` desc;

create view sales_by_store as
select concat(`c`.`city`, ',', `cy`.`country`)        AS `store`,
       concat(`m`.`first_name`, ' ', `m`.`last_name`) AS `manager`,
       sum(`p`.`amount`)                              AS `total_sales`
from (((((((`payment` `p` join `rental` `r` on ((`p`.`rental_id` = `r`.`rental_id`))) join `inventory` `i`
           on ((`r`.`inventory_id` = `i`.`inventory_id`))) join `store` `s`
          on ((`i`.`store_id` = `s`.`store_id`))) join `address` `a`
         on ((`s`.`address_id` = `a`.`address_id`))) join `city` `c`
        on ((`a`.`city_id` = `c`.`city_id`))) join `country` `cy`
       on ((`c`.`country_id` = `cy`.`country_id`))) join `staff` `m` on ((`s`.`manager_staff_id` = `m`.`staff_id`)))
group by `s`.`store_id`
order by `cy`.`country`, `c`.`city`;

create view staff_list as
select `s`.`staff_id`                                 AS `ID`,
       concat(`s`.`first_name`, ' ', `s`.`last_name`) AS `name`,
       `a`.`address`                                  AS `address`,
       `a`.`postal_code`                              AS `zip code`,
       `a`.`phone`                                    AS `phone`,
       `city`.`city`                                  AS `city`,
       `country`.`country`                            AS `country`,
       `s`.`store_id`                                 AS `SID`
from (((`staff` `s` join `address` `a` on ((`s`.`address_id` = `a`.`address_id`))) join `city`
       on ((`a`.`city_id` = `city`.`city_id`))) join `country` on ((`city`.`country_id` = `country`.`country_id`)));

