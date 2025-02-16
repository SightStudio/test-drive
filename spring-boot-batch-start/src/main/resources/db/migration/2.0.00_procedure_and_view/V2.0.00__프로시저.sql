create procedure film_in_stock(IN p_film_id int, IN p_store_id int, OUT p_film_count int)
    reads sql data
BEGIN
    SELECT inventory_id
    FROM inventory
    WHERE film_id = p_film_id
      AND store_id = p_store_id
      AND inventory_in_stock(inventory_id);

    SELECT FOUND_ROWS() INTO p_film_count;
END;

create procedure film_not_in_stock(IN p_film_id int, IN p_store_id int, OUT p_film_count int)
    reads sql data
BEGIN
    SELECT inventory_id
    FROM inventory
    WHERE film_id = p_film_id
      AND store_id = p_store_id
      AND NOT inventory_in_stock(inventory_id);

    SELECT FOUND_ROWS() INTO p_film_count;
END;

create function get_customer_balance(p_customer_id int, p_effective_date datetime) returns decimal(5, 2)
    deterministic reads sql data
BEGIN

    #OK, WE NEED TO CALCULATE THE CURRENT BALANCE GIVEN A CUSTOMER_ID AND A DATE
    #THAT WE WANT THE BALANCE TO BE EFFECTIVE FOR. THE BALANCE IS:
    #   1) RENTAL FEES FOR ALL PREVIOUS RENTALS
    #   2) ONE DOLLAR FOR EVERY DAY THE PREVIOUS RENTALS ARE OVERDUE
    #   3) IF A FILM IS MORE THAN RENTAL_DURATION * 2 OVERDUE, CHARGE THE REPLACEMENT_COST
    #   4) SUBTRACT ALL PAYMENTS MADE BEFORE THE DATE SPECIFIED

    DECLARE v_rentfees DECIMAL(5, 2); #FEES PAID TO RENT THE VIDEOS INITIALLY
    DECLARE v_overfees INTEGER; #LATE FEES FOR PRIOR RENTALS
    DECLARE v_payments DECIMAL(5, 2); #SUM OF PAYMENTS MADE PREVIOUSLY

    SELECT IFNULL(SUM(film.rental_rate), 0)
    INTO v_rentfees
    FROM film,
         inventory,
         rental
    WHERE film.film_id = inventory.film_id
      AND inventory.inventory_id = rental.inventory_id
      AND rental.rental_date <= p_effective_date
      AND rental.customer_id = p_customer_id;

    SELECT IFNULL(SUM(IF((TO_DAYS(rental.return_date) - TO_DAYS(rental.rental_date)) > film.rental_duration,
                         ((TO_DAYS(rental.return_date) - TO_DAYS(rental.rental_date)) - film.rental_duration), 0)), 0)
    INTO v_overfees
    FROM rental,
         inventory,
         film
    WHERE film.film_id = inventory.film_id
      AND inventory.inventory_id = rental.inventory_id
      AND rental.rental_date <= p_effective_date
      AND rental.customer_id = p_customer_id;


    SELECT IFNULL(SUM(payment.amount), 0)
    INTO v_payments
    FROM payment

    WHERE payment.payment_date <= p_effective_date
      AND payment.customer_id = p_customer_id;

    RETURN v_rentfees + v_overfees - v_payments;
END;

create function inventory_held_by_customer(p_inventory_id int) returns int
    reads sql data
BEGIN
    DECLARE v_customer_id INT;
    DECLARE EXIT HANDLER FOR NOT FOUND RETURN NULL;

    SELECT customer_id
    INTO v_customer_id
    FROM rental
    WHERE return_date IS NULL
      AND inventory_id = p_inventory_id;

    RETURN v_customer_id;
END;

create function inventory_in_stock(p_inventory_id int) returns tinyint(1)
    reads sql data
BEGIN
    DECLARE v_rentals INT;
    DECLARE v_out INT;

    #AN ITEM IS IN-STOCK IF THERE ARE EITHER NO ROWS IN THE rental TABLE
    #FOR THE ITEM OR ALL ROWS HAVE return_date POPULATED

    SELECT COUNT(*)
    INTO v_rentals
    FROM rental
    WHERE inventory_id = p_inventory_id;

    IF v_rentals = 0 THEN
        RETURN TRUE;
    END IF;

    SELECT COUNT(rental_id)
    INTO v_out
    FROM inventory
             LEFT JOIN rental USING (inventory_id)
    WHERE inventory.inventory_id = p_inventory_id
      AND rental.return_date IS NULL;

    IF v_out > 0 THEN
        RETURN FALSE;
    ELSE
        RETURN TRUE;
    END IF;
END;

create procedure rewards_report(IN min_monthly_purchases tinyint unsigned,
                                IN min_dollar_amount_purchased decimal(10, 2) unsigned, OUT count_rewardees int)
    comment 'Provides a customizable report on best customers' reads sql data
proc:
BEGIN

    DECLARE last_month_start DATE;
    DECLARE last_month_end DATE;

    /* Some sanity checks... */
    IF min_monthly_purchases = 0 THEN
        SELECT 'Minimum monthly purchases parameter must be > 0';
        LEAVE proc;
    END IF;
    IF min_dollar_amount_purchased = 0.00 THEN
        SELECT 'Minimum monthly dollar amount purchased parameter must be > $0.00';
        LEAVE proc;
    END IF;

    /* Determine start and end time periods */
    SET last_month_start = DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH);
    SET last_month_start = STR_TO_DATE(CONCAT(YEAR(last_month_start), '-', MONTH(last_month_start), '-01'), '%Y-%m-%d');
    SET last_month_end = LAST_DAY(last_month_start);

    /*
        Create a temporary storage area for
        Customer IDs.
    */
    CREATE TEMPORARY TABLE tmpCustomer
    (
        customer_id INT UNSIGNED NOT NULL PRIMARY KEY
    );

    /*
        Find all customers meeting the
        monthly purchase requirements
    */
    INSERT INTO tmpCustomer (customer_id)
    SELECT p.customer_id
    FROM payment AS p
    WHERE DATE(p.payment_date) BETWEEN last_month_start AND last_month_end
    GROUP BY customer_id
    HAVING SUM(p.amount) > min_dollar_amount_purchased
       AND COUNT(customer_id) > min_monthly_purchases;

    /* Populate OUT parameter with count of found customers */
    SELECT COUNT(*) FROM tmpCustomer INTO count_rewardees;

    /*
        Output ALL customer information of matching rewardees.
        Customize output as needed.
    */
    SELECT c.*
    FROM tmpCustomer AS t
             INNER JOIN customer AS c ON t.customer_id = c.customer_id;

    /* Clean up */
    DROP TABLE tmpCustomer;
END;

