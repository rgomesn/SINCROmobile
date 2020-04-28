drop database if exists sincrodb;
create database sincrodb;
use sincrodb;

create table driver (
    driver_id bigint not null auto_increment unique key,
    first_name varchar(250) not null,
    middle_name varchar(100) not null,
    last_name varchar(50) not null,
    cc_number varchar(9) not null,
    driver_licence_number varchar(20) not null primary key,
    phone_number varchar(9) not null,
    email varchar(50) default null,
    check (phone_number regexp '^9[0-9]{8}$'),
    check (cc_number regexp '^[0-9]{9}$')
);

create table vehicle (
    vehicle_id bigint not null auto_increment unique key,
    licence_plate varchar(8) primary key,
    make varchar(20) not null,
    model varchar(20) not null,
    _date date not null,
    check ((licence_plate regexp '^[A-Z]{2}-[0-9]{2}-[0-9]{2}$'
        and year(_date) between 1937 and 1992)
        or (licence_plate regexp '^[0-9]{2}-[0-9]{2}-[A-Z]{2}$'
        and year(_date) between 1992 and 2004)
        or (licence_plate regexp '^[0-9]{2}-[A-Z]{2}-[0-9]{2}$'
        and year(_date) between 2005 and 2020)
        or (licence_plate regexp '^[A-Z]{2}-[0-9]{2}-[A-Z]{2}$'
        and year(_date) > 2020))
);

create table vehicle_driver (
	vehicle_driver_id bigint not null unique key auto_increment,
    driver_licence_number varchar(20),
    licence_plate varchar(8),
    vehicle_owner bool not null,
    primary key (driver_licence_number , licence_plate),
    foreign key (driver_licence_number)
        references driver (driver_licence_number),
    foreign key (licence_plate)
        references vehicle (licence_plate)
);

create table radar (
    radar_id bigint primary key auto_increment,
    latitude decimal(10 , 8 ) not null unique key,
    longitude decimal(11 , 8 ) not null unique key
);

create table infraction (
    infraction_id bigint not null unique key auto_increment,
    vehicle_driver_id bigint,
    radar_id bigint,
    infraction_timestamp timestamp not null,
    price decimal(10 , 0 ) not null,
    paid bool not null,
    last_updated timestamp not null,
    primary key (vehicle_driver_id, radar_id, infraction_timestamp),
    foreign key (vehicle_driver_id)
        references vehicle_driver (vehicle_driver_id),
    foreign key (radar_id)
        references radar (radar_id)
);