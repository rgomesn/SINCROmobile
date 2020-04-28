insert into driver (first_name, middle_name, last_name, driver_licence_number, cc_number, phone_number, email)
values ('Rafael', 'Gomes', 'Nobre', "123456789", '251489558', '933417044', 'a39267@alunos.isel.pt');

insert into vehicle (licence_plate, make, model, _date)
values ('69-HZ-10', 'Fiat', 'Punto', '2006-01-01');

insert into vehicle_driver (cc_number, licence_plate, vehicle_owner)
values ('251489558', '69-HZ-10', false);

select *
from driver;