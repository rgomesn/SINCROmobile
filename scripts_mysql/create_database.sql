drop database if exists sincrodb;
create database sincrodb;
use sincrodb;

create table citizen (
    citizen_id bigint not null auto_increment unique key,
    first_name varchar(250) not null,
    middle_name varchar(100) not null,
    last_name varchar(50) not null,
    cc_number varchar(9) primary key,
    driver_licence_number varchar(20) not null,
    phone_number varchar(9) not null,
    email varchar(50) default null
);

create table user (
	username varchar(20) primary key,
    pwd varchar(20) not null,
    cc_number varchar(9) not null,
    foreign key (cc_number)
        references citizen (cc_number)
);

create table vehicle (
    vehicle_id bigint not null auto_increment unique key,
    licence_plate varchar(10) primary key,
    make varchar(20) not null,
    model varchar(20) not null,
    vehicle_date date not null,
    category char(1) not null
    /*check ((licence_plate regexp '^[A-Z]{2}-[0-9]{2}-[0-9]{2}$'
        and year(_date) between 1937 and 1992)
        or (licence_plate regexp '^[0-9]{2}-[0-9]{2}-[A-Z]{2}$'
        and year(_date) between 1992 and 2004)
        or (licence_plate regexp '^[0-9]{2}-[A-Z]{2}-[0-9]{2}$'
        and year(_date) between 2005 and 2020)
        or (licence_plate regexp '^[A-Z]{2}-[0-9]{2}-[A-Z]{2}$'
        and year(_date) > 2020))*/
);

create table vehicle_authorization (
	vehicle_authorization_id bigint not null unique key auto_increment,
    cc_number_owner varchar(9),
    cc_number_driver varchar(9),
    licence_plate varchar(10) not null,
    _active boolean not null,
    authorization_start date not null,
    authorization_end date not null,
    primary key (cc_number_owner, cc_number_driver, licence_plate),
    foreign key (cc_number_owner)
        references citizen (cc_number),
    foreign key (cc_number_driver)
        references citizen (cc_number),
    foreign key (licence_plate)
        references vehicle (licence_plate)
);

create table vehicle_owner (
	vehicle_owner_id bigint not null unique key auto_increment,
    cc_number varchar(9) not null,
    licence_plate varchar(10) not null,
    _active boolean not null,
    ownership_start date not null,
    ownership_end date,
    primary key (cc_number, licence_plate),
    foreign key (cc_number)
        references citizen (cc_number),
    foreign key (licence_plate)
        references vehicle (licence_plate)
);

create table vehicle_driver (
	vehicle_driver_id bigint not null unique key auto_increment,
    cc_number varchar(9),
    licence_plate varchar(8),
    _active boolean not null,
    driving_start date not null,
    driving_end date,
    primary key (cc_number, licence_plate),
    foreign key (cc_number)
        references citizen (cc_number),
    foreign key (licence_plate)
        references vehicle (licence_plate)
);

create table radar (
    radar_id bigint primary key auto_increment,
    latitude decimal(10, 8) not null unique key,
    longitude decimal(11, 8) not null unique key,
    address varchar(50) not null,
    kilometer integer not null,
    direction varchar(100) not null
);

create table infraction (
    infraction_id bigint not null unique key auto_increment,
    licence_plate varchar(8),
    radar_id bigint,
    infraction_date_time timestamp not null,
    price decimal(10, 2) not null,
    paid bool not null,
    payment_day_time timestamp,
    primary key (licence_plate, radar_id, infraction_date_time),
    foreign key (licence_plate)
        references vehicle (licence_plate),
    foreign key (radar_id)
        references radar (radar_id)
);

create table speed_infraction (
	infraction_id bigint primary key,
    vehicle_speed integer not null,
    speed_limit integer not null,
    direction char(1) not null,
    chasing_vehicle_speed integer,
    foreign key (infraction_id)
		references infraction (infraction_id)
);

create table distance_infraction (
	infraction_id bigint primary key,
    distance_to_next_vehicle integer not null,
    distance_limit integer not null,
    foreign key (infraction_id)
		references infraction (infraction_id)
);

create table red_light_infraction (
	infraction_id bigint primary key,
    vehicle_speed integer not null,
    elapsed_time integer not null,
    yellow_duration integer not null,
    foreign key (infraction_id)
		references infraction (infraction_id)
);

create table notification (
	notification_id bigint not null unique key auto_increment,
    infraction_id bigint,
    cc_number varchar(9),
    notified boolean not null,
    notification_time timestamp not null,
    primary key (infraction_id, cc_number),
    foreign key (infraction_id)
		references infraction (infraction_id),
    foreign key (cc_number)
        references citizen (cc_number)
);