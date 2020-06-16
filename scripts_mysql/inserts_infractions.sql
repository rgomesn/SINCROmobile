INSERT INTO citizen (`first_name`, `middle_name`, `last_name`, `cc_number`, `driver_licence_number`, `phone_number`, `email`)
VALUES ('Rafael', 'Gomes', 'Nobre', '123456789', '987654321', '912345678', 'rafael.gomes.nobre@gmail.com'); 

INSERT INTO user (`username`, `pwd`, `cc_number`)
VALUES ('rgn', 'rgn', '123456789'); 

INSERT INTO vehicle (`licence_plate`, `make`, `model`, `vehicle_date`, `category`)
VALUES ('12-AB-34', 'Opel', 'Corsa', '2001-01-01', 'L'); 

INSERT INTO infraction (`infraction_id`, `licence_plate`, `radar_id`, `infraction_date_time`, `price`, `paid`, `payment_day_time`)
VALUES (1, '12-AB-34', 1, '2020-01-01 09:00:00', 100.00, false, null);

INSERT INTO speed_infraction (`infraction_id`, `vehicle_speed`, `speed_limit`, `direction`, `chasing_vehicle_speed`)
VALUES (1, 90, 60, '+', 0); 

INSERT INTO infraction (`infraction_id`, `licence_plate`, `radar_id`, `infraction_date_time`, `price`, `paid`, `payment_day_time`)
VALUES (2, '12-AB-34', 2, '2020-01-02 09:00:00', 150.00, true, '2020-02-02 09:00:00');

INSERT INTO speed_infraction (`infraction_id`, `vehicle_speed`, `speed_limit`, `direction`, `chasing_vehicle_speed`)
VALUES (2, 100, 60, '+', 0); 


INSERT INTO infraction (`infraction_id`, `licence_plate`, `radar_id`, `infraction_date_time`, `price`, `paid`, `payment_day_time`)
VALUES (3, '12-AB-34', 3, '2020-01-03 09:00:00', 100.00, false, null);

INSERT INTO distance_infraction (`infraction_id`, `distance_to_next_vehicle`, `distance_limit`)
VALUES (3, 5, 10); 

INSERT INTO infraction (`infraction_id`, `licence_plate`, `radar_id`, `infraction_date_time`, `price`, `paid`, `payment_day_time`)
VALUES (4, '12-AB-34', 4, '2020-01-04 09:00:00', 150.00, true, '2020-02-04 09:00:00');

INSERT INTO distance_infraction (`infraction_id`, `distance_to_next_vehicle`, `distance_limit`)
VALUES (3, 10, 20); 


INSERT INTO infraction (`infraction_id`, `licence_plate`, `radar_id`, `infraction_date_time`, `price`, `paid`, `payment_day_time`)
VALUES (5, '12-AB-34', 5, '2020-01-05 09:00:00', 100.00, false, null);

INSERT INTO red_light_infraction (`infraction_id`, `vehicle_speed`, `elapsed_time`, `yellow_duration`)
VALUES (5, 90, 5, 30); 

INSERT INTO infraction (`infraction_id`, `licence_plate`, `radar_id`, `infraction_date_time`, `price`, `paid`, `payment_day_time`)
VALUES (6, '12-AB-34', 6, '2020-01-06 09:00:00', 150.00, true, '2020-02-06 09:00:00');

INSERT INTO red_light_infraction (`infraction_id`, `vehicle_speed`, `elapsed_time`, `yellow_duration`)
VALUES (1, 90, 5, 30); 