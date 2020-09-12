package com.isel.sincroserver.repositories;

import com.isel.sincroserver.entities.*;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.interfaces.repositories.Repository;
import com.isel.sincroserver.interfaces.resources.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
@Qualifier("MySQLRepository")
public class MySQLRepository implements Repository {
    private static final Logger logger = LogManager.getLogger(MySQLRepository.class);
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Qualifier("FileResources")
    Resources resources;

    public MySQLRepository() {

    }

    @Autowired
    public MySQLRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Resources resources) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.resources = resources;
    }

    @Override
    public int insertCitizen(Citizen citizen) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_CITIZEN);

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("first_name", citizen.getFirst_name())
                .addValue("middle_name", citizen.getMiddle_name())
                .addValue("last_name", citizen.getLast_name())
                .addValue("driver_licence_number", citizen.getDriver_licence_number())
                .addValue("cc_number", citizen.getCc_number())
                .addValue("phone_number", citizen.getPhone_number())
                .addValue("email", citizen.getEmail());

        logger.info(Constants.INSERT_CITIZEN + ": " + query);
        logger.debug(Constants.INSERT_CITIZEN + ": " + citizen.getCc_number());

        int rows = namedParameterJdbcTemplate.update(query, parameters, holder);

        citizen.setCitizen_id(holder.getKey().longValue());

        return rows;
    }

    @Override
    public int deleteCitizen(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.DELETE_CITIZEN);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.DELETE_CITIZEN + ": " + query);
        logger.debug(Constants.DELETE_CITIZEN + ": " + cc_number);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int updateCitizenEmail(String cc_number, String email) throws SincroServerException {
        String query = resources.getQuery(Constants.UPDATE_CITIZEN_EMAIL);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number)
                .addValue("email", email);

        logger.info(Constants.UPDATE_CITIZEN_EMAIL + ": " + query);
        logger.debug(Constants.UPDATE_CITIZEN_EMAIL + ": " + cc_number + ", update email: " + email);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int updateCitizenPhoneNumber(String cc_number, String phone_number) throws SincroServerException {
        String query = resources.getQuery(Constants.UPDATE_CITIZEN_PHONE_NUMBER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number)
                .addValue("phone_number", phone_number);

        logger.info(Constants.UPDATE_CITIZEN_PHONE_NUMBER + ": " + query);
        logger.debug(Constants.UPDATE_CITIZEN_PHONE_NUMBER + ": " + cc_number + ", update phone: " + phone_number);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public Citizen getCitizen(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN + ": " + query);
        logger.debug(Constants.GET_CITIZEN + ": " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapCitizen(rs, false);
        });
    }

    @Override
    public List<Citizen> getCitizens() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZENS);

        logger.info(Constants.GET_CITIZEN + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapCitizen(rs, true));
    }

    @Override
    public List<Citizen> getVehicleCitizens(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_CITIZENS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_CITIZENS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_CITIZENS + ": " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapCitizen(rs, true));
    }

    @Override
    public List<Citizen> getVehicleOwnerCitizens(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_OWNER_CITIZENS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_OWNER_CITIZENS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_OWNER_CITIZENS + ": " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapCitizen(rs, true));
    }

    @Override
    public List<Citizen> getVehicleDriverCitizens(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_DRIVER_CITIZENS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_DRIVER_CITIZENS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_DRIVER_CITIZENS + ": " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapCitizen(rs, true));
    }

    @Override
    public int insertUser(User user) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_USER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("pwd", user.getPwd())
                .addValue("cc_number", user.getCc_number());

        logger.info(Constants.INSERT_USER + ": " + query);
        logger.debug(Constants.INSERT_USER + ": " + user.getUsername());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int deleteUser(String username) throws SincroServerException {
        String query = resources.getQuery(Constants.DELETE_USER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username);

        logger.info(Constants.DELETE_USER + ": " + query);
        logger.debug(Constants.DELETE_USER + ": " + username);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int updateUserPassword(String username, String password) throws SincroServerException {
        String query = resources.getQuery(Constants.UPDATE_USER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("password", password);

        logger.info(Constants.UPDATE_USER + ": " + query);
        logger.debug(Constants.UPDATE_USER + ": " + username);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public User getUser(String username) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_USER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username);

        logger.info(Constants.GET_USER + ": " + query);
        logger.debug(Constants.GET_USER + ": " + username);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapUser(rs, false);
        });
    }

    @Override
    public User getCitizenUser(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_USER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN_USER + ": " + query);
        logger.debug(Constants.GET_CITIZEN_USER + ": " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapUser(rs, false);
        });
    }

    @Override
    public int insertVehicle(Vehicle vehicle) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_VEHICLE);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", vehicle.getLicence_plate())
                .addValue("make", vehicle.getMake())
                .addValue("model", vehicle.getModel())
                .addValue("vehicle_date", vehicle.getVehicle_date())
                .addValue("category", vehicle.getCategory());

        logger.info(Constants.INSERT_VEHICLE + ": " + query);
        logger.debug(Constants.INSERT_VEHICLE + ": " + vehicle.getLicence_plate());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int deleteVehicle(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.DELETE_VEHICLE);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.DELETE_VEHICLE + ": " + query);
        logger.debug(Constants.DELETE_VEHICLE + ": " + licence_plate);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public Vehicle getVehicle(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE + ": " + query);
        logger.debug(Constants.GET_VEHICLE + ": " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapVehicle(rs, false);
        });
    }

    @Override
    public List<Vehicle> getVehicles() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLES);

        logger.info(Constants.GET_VEHICLES + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapVehicle(rs, true));
    }

    @Override
    public List<Vehicle> getCitizenVehicles(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_VEHICLES);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN_VEHICLES + ": " + query);
        logger.debug(Constants.GET_CITIZEN_VEHICLES + ": " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapVehicle(rs, true));
    }

    @Override
    public List<Vehicle> getCitizenOwnedVehicles(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_OWNED_VEHICLES);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN_OWNED_VEHICLES + ": " + query);
        logger.debug(Constants.GET_CITIZEN_OWNED_VEHICLES + ": " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapVehicle(rs, true));
    }

    @Override
    public List<Vehicle> getCitizenDrivenVehicles(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_DRIVEN_VEHICLES);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN_DRIVEN_VEHICLES + ": " + query);
        logger.debug(Constants.GET_CITIZEN_DRIVEN_VEHICLES + ": " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapVehicle(rs, true));
    }

    @Override
    public int insertVehicleAuthorization(VehicleAuthorization vehicleAuthorization) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_VEHICLE_AUTHORIZATION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number_owner", vehicleAuthorization.getCc_number_owner())
                .addValue("cc_number_driver", vehicleAuthorization.getCc_number_driver())
                .addValue("licence_plate", vehicleAuthorization.getLicence_plate())
                .addValue("authorization_start", vehicleAuthorization.getAuthorization_start());

        logger.info(Constants.INSERT_VEHICLE_AUTHORIZATION + ": " + query);
        logger.debug(Constants.INSERT_VEHICLE_AUTHORIZATION + ": owner: " + vehicleAuthorization.getCc_number_owner() +
                ", driver: " + vehicleAuthorization.getCc_number_driver() +
                ", licence_plate: " + vehicleAuthorization.getLicence_plate());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int endVehicleAuthorization(String cc_number_owner, String cc_number_driver, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.END_VEHICLE_AUTHORIZATION);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number_owner", cc_number_owner)
                .addValue("cc_number_driver", cc_number_driver)
                .addValue("licence_plate", licence_plate)
                .addValue("authorization_end", end);

        logger.info(Constants.END_VEHICLE_AUTHORIZATION + ": " + query);
        logger.debug(Constants.END_VEHICLE_AUTHORIZATION + ": owner: " + cc_number_owner +
                ", driver: " + cc_number_driver +
                ", licence_plate: " + licence_plate +
                ", end time: " + end);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public VehicleAuthorization getVehicleAuthorization(String cc_number_owner, String cc_number_driver, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_AUTHORIZATION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number_owner", cc_number_owner)
                .addValue("cc_number_driver", cc_number_driver)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_AUTHORIZATION + ": " + query);
        logger.debug(Constants.GET_VEHICLE_AUTHORIZATION + ": owner: " + cc_number_owner +
                ", driver: " + cc_number_driver +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapVehicleAuthorization(rs, false);
        });
    }

    @Override
    public List<VehicleAuthorization> getCitizenGrantedVehicleAuthorizations(String cc_number_owner, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_GRANTED_VEHICLE_AUTHORIZATION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number_owner", cc_number_owner)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_CITIZEN_GRANTED_VEHICLE_AUTHORIZATION + ": " + query);
        logger.debug(Constants.GET_CITIZEN_GRANTED_VEHICLE_AUTHORIZATION + ": owner: " + cc_number_owner +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapVehicleAuthorization(rs, true));
    }

    @Override
    public List<VehicleAuthorization> getCitizenGivenVehicleAuthorizations(String cc_number_driver, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_GIVEN_VEHICLE_AUTHORIZATION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number_driver", cc_number_driver)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_CITIZEN_GIVEN_VEHICLE_AUTHORIZATION + ": " + query);
        logger.debug(Constants.GET_CITIZEN_GIVEN_VEHICLE_AUTHORIZATION + ": driver: " + cc_number_driver +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapVehicleAuthorization(rs, true));
    }

    @Override
    public int insertVehicleOwner(VehicleOwner vehicleOwner) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_VEHICLE_OWNER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", vehicleOwner.getCc_number())
                .addValue("licence_plate", vehicleOwner.getLicence_plate())
                .addValue("ownership_start", vehicleOwner.getOwnership_start());

        logger.info(Constants.INSERT_VEHICLE_OWNER + ": " + query);
        logger.debug(Constants.INSERT_VEHICLE_OWNER + ": cc_number: " + vehicleOwner.getCc_number() +
                ", licence_plate: " + vehicleOwner.getLicence_plate() +
                ", ownership_start: " + vehicleOwner.getOwnership_start());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int endVehicleOwnership(String cc_number, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.END_VEHICLE_OWNERSHIP);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number)
                .addValue("licence_plate", licence_plate)
                .addValue("ownership_end", end);

        logger.info(Constants.END_VEHICLE_OWNERSHIP + ": " + query);
        logger.debug(Constants.END_VEHICLE_OWNERSHIP + ": cc_number: " + cc_number +
                ", licence_plate: " + licence_plate +
                ", end time: " + end);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public VehicleOwner getVehicleOwner(String cc_number, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_OWNER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_OWNER + ": " + query);
        logger.debug(Constants.GET_VEHICLE_OWNER + ": cc_number: " + cc_number +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapVehicleOwner(rs, false);
        });
    }

    @Override
    public int insertVehicleDriver(VehicleDriver vehicleDriver) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_VEHICLE_DRIVER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", vehicleDriver.getCc_number())
                .addValue("licence_plate", vehicleDriver.getLicence_plate())
                .addValue("driving_start", vehicleDriver.getDriving_start());

        logger.info(Constants.INSERT_VEHICLE_DRIVER + ": " + query);
        logger.debug(Constants.INSERT_VEHICLE_DRIVER + ": cc_number: " + vehicleDriver.getCc_number() +
                ", licence_plate: " + vehicleDriver.getLicence_plate() +
                ", driving_start: " + vehicleDriver.getDriving_start());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int endVehicleDriving(String cc_number, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.END_VEHICLE_DRIVING);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number)
                .addValue("licence_plate", licence_plate)
                .addValue("driving_end", end);

        logger.info(Constants.END_VEHICLE_DRIVING + ": " + query);
        logger.debug(Constants.END_VEHICLE_DRIVING + ": cc_number: " + cc_number +
                ", licence_plate: " + licence_plate +
                ", end time: " + end);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public VehicleDriver getVehicleDriver(String cc_number, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_DRIVER);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_DRIVER + ": " + query);
        logger.debug(Constants.GET_VEHICLE_DRIVER + ": cc_number: " + cc_number +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapVehicleDriver(rs, false);
        });
    }

    @Override
    public int insertInfraction(Infraction infraction) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_INFRACTION);

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", infraction.getLicence_plate())
                .addValue("radar_id", infraction.getRadar_id())
                .addValue("infraction_date_time", infraction.getInfraction_date_time())
                .addValue("price", infraction.getPrice());

        logger.info(Constants.INSERT_INFRACTION + ": " + query);
        logger.debug(Constants.INSERT_INFRACTION + ": licence_plate: " + infraction.getLicence_plate() +
                ", radar_id: " + infraction.getRadar_id() +
                ", infraction_date_time: " + infraction.getInfraction_date_time() +
                ", price: " + infraction.getPrice());

        int res = namedParameterJdbcTemplate.update(query, parameters, holder);

        infraction.setInfraction_id(holder.getKey().longValue());

        return res;
    }

    @Override
    public int updateInfractionPayment(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.UPDATE_INFRACTION_PAYMENT);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id)
                .addValue("payment_date_time", end);

        logger.info(Constants.UPDATE_INFRACTION_PAYMENT + ": " + query);
        logger.debug(Constants.UPDATE_INFRACTION_PAYMENT + ": infraction_id: " + infraction_id +
                ", payment time: " + end);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public Infraction getInfraction(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_INFRACTION);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_INFRACTION + ": " + query);
        logger.debug(Constants.GET_INFRACTION + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapInfraction(rs, false);
        });
    }

    @Override
    public List<Infraction> getInfractions() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_INFRACTIONS);

        logger.info(Constants.GET_INFRACTIONS + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapInfraction(rs, true));
    }

    @Override
    public int getInfractionType(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_INFRACTION_TYPE);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_INFRACTION_TYPE + ": " + query);
        logger.debug(Constants.GET_INFRACTION_TYPE + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return rs.getInt("infraction_type");
        });
    }

    @Override
    public List<Infraction> getVehicleInfractions(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_INFRACTIONS + ": licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapInfraction(rs, true));
    }

    @Override
    public List<Infraction> getRadarAndVehicleInfractions(long radar_id, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_AND_RADAR_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("radar_id", radar_id)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_AND_RADAR_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_AND_RADAR_INFRACTIONS + ": radar_id: " + radar_id +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapInfraction(rs, true));
    }

    @Override
    public int insertRedLightInfraction(RedLightInfraction redLightinfraction) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_RED_LIGHT_INFRACTION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", redLightinfraction.getInfraction_id())
                .addValue("vehicle_speed", redLightinfraction.getVehicle_speed())
                .addValue("elapsed_time", redLightinfraction.getElapsed_time())
                .addValue("yellow_duration", redLightinfraction.getYellow_duration());

        logger.info(Constants.INSERT_RED_LIGHT_INFRACTION + ": " + query);
        logger.debug(Constants.INSERT_RED_LIGHT_INFRACTION + ": infraction_id: " + redLightinfraction.getInfraction_id() +
                ", vehicle_speed: " + redLightinfraction.getVehicle_speed() +
                ", elapsed_time: " + redLightinfraction.getElapsed_time() +
                ", yellow_duration: " + redLightinfraction.getYellow_duration());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public RedLightInfraction getRedLightInfraction(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_RED_LIGHT_INFRACTION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_RED_LIGHT_INFRACTION + ": " + query);
        logger.debug(Constants.GET_RED_LIGHT_INFRACTION + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapRedLightInfraction(rs, false);
        });
    }

    @Override
    public List<RedLightInfraction> getRedLightInfractions() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_RED_LIGHT_INFRACTIONS);

        logger.info(Constants.GET_RED_LIGHT_INFRACTIONS + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapRedLightInfraction(rs, true));
    }

    @Override
    public List<RedLightInfraction> getVehicleRedLightInfractions(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_RED_LIGHT_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_RED_LIGHT_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_RED_LIGHT_INFRACTIONS + ": licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapRedLightInfraction(rs, true));
    }

    @Override
    public List<RedLightInfraction> getRadarAndVehicleRedLightInfractions(long radar_id, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_AND_RADAR_RED_LIGHT_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("radar_id", radar_id)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_AND_RADAR_RED_LIGHT_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_AND_RADAR_RED_LIGHT_INFRACTIONS + ": radar_id: " + radar_id +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapRedLightInfraction(rs, true));
    }

    @Override
    public int insertSpeedInfraction(SpeedInfraction speedinfraction) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_SPEED_INFRACTION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", speedinfraction.getInfraction_id())
                .addValue("vehicle_speed", speedinfraction.getVehicle_speed())
                .addValue("speed_limit", speedinfraction.getSpeed_limit())
                .addValue("direction", speedinfraction.getDirection())
                .addValue("chasing_vehicle_speed", speedinfraction.getChasing_vehicle_speed());

        logger.info(Constants.INSERT_SPEED_INFRACTION + ": " + query);
        logger.debug(Constants.INSERT_SPEED_INFRACTION +
                ": infraction_id: " + speedinfraction.getInfraction_id() +
                ", vehicle_speed: " + speedinfraction.getVehicle_speed() +
                ", speed_limit: " + speedinfraction.getSpeed_limit() +
                ", direction: " + speedinfraction.getDirection() +
                ", chasing_vehicle_speed: " + speedinfraction.getChasing_vehicle_speed());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public SpeedInfraction getSpeedInfraction(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_SPEED_INFRACTION);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_SPEED_INFRACTION + ": " + query);
        logger.debug(Constants.GET_SPEED_INFRACTION + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapSpeedInfraction(rs, false);
        });
    }

    @Override
    public List<SpeedInfraction> getSpeedInfractions() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_SPEED_INFRACTIONS);

        logger.info(Constants.GET_SPEED_INFRACTIONS + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapSpeedInfraction(rs, true));
    }

    @Override
    public List<SpeedInfraction> getVehicleSpeedInfractions(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_SPEED_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_SPEED_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_SPEED_INFRACTIONS + ": licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapSpeedInfraction(rs, true));
    }

    @Override
    public List<SpeedInfraction> getRadarAndVehicleSpeedInfractions(long radar_id, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_AND_RADAR_SPEED_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("radar_id", radar_id)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_AND_RADAR_SPEED_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_AND_RADAR_SPEED_INFRACTIONS + ": radar_id: " + radar_id +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapSpeedInfraction(rs, true));
    }

    @Override
    public int insertDistanceInfraction(DistanceInfraction distanceinfraction) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_DISTANCE_INFRACTION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", distanceinfraction.getInfraction_id())
                .addValue("distance_to_next_vehicle", distanceinfraction.getDistance_to_next_vehicle())
                .addValue("distance_limit", distanceinfraction.getDistance_limit());

        logger.info(Constants.INSERT_DISTANCE_INFRACTION + ": " + query);
        logger.debug(Constants.INSERT_DISTANCE_INFRACTION + ": infraction_id: " + distanceinfraction.getInfraction_id() +
                ", distance_to_next_vehicle: " + distanceinfraction.getDistance_to_next_vehicle() +
                ", distance_limit: " + distanceinfraction.getDistance_limit());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public DistanceInfraction getDistanceInfraction(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_DISTANCE_INFRACTION);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_DISTANCE_INFRACTION + ": " + query);
        logger.debug(Constants.GET_DISTANCE_INFRACTION + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapDistanceInfraction(rs, false);
        });
    }

    @Override
    public List<DistanceInfraction> getDistanceInfractions() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_DISTANCE_INFRACTIONS);

        logger.info(Constants.GET_DISTANCE_INFRACTIONS + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapDistanceInfraction(rs, true));
    }

    @Override
    public List<DistanceInfraction> getVehicleDistanceInfractions(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_DISTANCE_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_DISTANCE_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_DISTANCE_INFRACTIONS + ": licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapDistanceInfraction(rs, true));
    }

    @Override
    public List<DistanceInfraction> getRadarAndVehicleDistanceInfractions(long radar_id, String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_AND_RADAR_DISTANCE_INFRACTIONS);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("radar_id", radar_id)
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_AND_RADAR_DISTANCE_INFRACTIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_AND_RADAR_DISTANCE_INFRACTIONS + ": radar_id: " + radar_id +
                ", licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapDistanceInfraction(rs, true));
    }

    @Override
    public int insertNotification(Notification notification) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_NOTIFICATION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", notification.getInfraction_id())
                .addValue("cc_number", notification.getCc_number());

        logger.info(Constants.INSERT_NOTIFICATION + ": " + query);
        logger.debug(Constants.INSERT_NOTIFICATION + ": infraction_id: " + notification.getInfraction_id() +
                ", cc_number: " + notification.getCc_number());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int deleteNotification(long infraction_id, String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.DELETE_NOTIFICATION);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id)
                .addValue("cc_number", cc_number);

        logger.info(Constants.INSERT_NOTIFICATION + ": " + query);
        logger.debug(Constants.INSERT_NOTIFICATION + ": infraction_id: " + infraction_id +
                ", cc_number: " + cc_number);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public int updateNotification(long infraction_id, String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.UPDATE_NOTIFICATION);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id)
                .addValue("cc_number", cc_number)
                .addValue("notification_time", end);

        logger.info(Constants.UPDATE_NOTIFICATION + ": " + query);
        logger.debug(Constants.UPDATE_NOTIFICATION + ": infraction_id: " + infraction_id +
                ", cc_number: " + cc_number +
                ", notification_time: " + end);

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public Notification getNotification(long infraction_id, String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_NOTIFICATION);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id)
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_NOTIFICATION + ": " + query);
        logger.debug(Constants.GET_NOTIFICATION + ": infraction_id: " + infraction_id +
                ", cc_number: " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapNotification(rs, false);
        });
    }

    @Override
    public List<Notification> getNotifications() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_NOTIFICATIONS);

        logger.info(Constants.GET_NOTIFICATIONS + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public List<Notification> getInfractionNotifications(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_INFRACTION_NOTIFICATIONS);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_INFRACTION_NOTIFICATIONS + ": " + query);
        logger.debug(Constants.GET_INFRACTION_NOTIFICATIONS + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public List<Notification> getInfractionToNotifyNotifications(long infraction_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_INFRACTION_TO_NOTIFY_NOTIFICATIONS);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("infraction_id", infraction_id);

        logger.info(Constants.GET_INFRACTION_TO_NOTIFY_NOTIFICATIONS + ": " + query);
        logger.debug(Constants.GET_INFRACTION_TO_NOTIFY_NOTIFICATIONS + ": infraction_id: " + infraction_id);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public List<Notification> getCitizenNotifications(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_NOTIFICATIONS);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN_NOTIFICATIONS + ": " + query);
        logger.debug(Constants.GET_CITIZEN_NOTIFICATIONS + ": cc_number: " + cc_number);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public List<Notification> getCitizenToNotifyNotifications(String cc_number) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_CITIZEN_TO_NOTIFY_NOTIFICATIONS);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("cc_number", cc_number);

        logger.info(Constants.GET_CITIZEN_TO_NOTIFY_NOTIFICATIONS + ": " + query);
        logger.debug(Constants.GET_CITIZEN_TO_NOTIFY_NOTIFICATIONS + ": cc_number: " + cc_number);

        return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public List<Notification> getVehicleNotifications(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_NOTIFICATIONS);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_NOTIFICATIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_NOTIFICATIONS + ": licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public List<Notification> getVehicleToNotifyNotifications(String licence_plate) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_VEHICLE_TO_NOTIFY_NOTIFICATIONS);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("licence_plate", licence_plate);

        logger.info(Constants.GET_VEHICLE_TO_NOTIFY_NOTIFICATIONS + ": " + query);
        logger.debug(Constants.GET_VEHICLE_TO_NOTIFY_NOTIFICATIONS + ": licence_plate: " + licence_plate);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapNotification(rs, true));
    }

    @Override
    public int insertRadar(Radar radar) throws SincroServerException {
        String query = resources.getQuery(Constants.INSERT_RADAR);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("latitude", radar.getLatitude())
                .addValue("longitude", radar.getLongitude())
                .addValue("address", radar.getAddress())
                .addValue("kilometer", radar.getKilometer())
                .addValue("direction", radar.getDirection());

        logger.info(Constants.INSERT_RADAR + ": " + query);
        logger.debug(Constants.INSERT_RADAR + ": latitude: " + radar.getLatitude() +
                ", longitude: " + radar.getLongitude() +
                ", address: " + radar.getAddress() +
                ", kilometer: " + radar.getKilometer() +
                ", direction: " + radar.getDirection());

        return namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public Radar getRadar(long radar_id) throws SincroServerException {
        String query = resources.getQuery(Constants.GET_RADAR);

        Date end = Date.from(Instant.now());

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("radar_id", radar_id);

        logger.info(Constants.GET_RADAR + ": " + query);
        logger.debug(Constants.GET_RADAR + ": radar_id: " + radar_id);

        return namedParameterJdbcTemplate.query(query, parameters, rs -> {
            return mapRadar(rs, false);
        });
    }

    @Override
    public List<Radar> getRadars() throws SincroServerException {
        String query = resources.getQuery(Constants.GET_RADARS);

        logger.info(Constants.GET_RADARS + ": " + query);

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> mapRadar(rs, true));
    }

    private Citizen mapCitizen(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new Citizen(rs.getLong("citizen_id"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("cc_number"),
                rs.getString("driver_licence_number"),
                rs.getString("phone_number"),
                rs.getString("email"));
    }

    private User mapUser(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new User(rs.getString("username"),
                rs.getString("pwd"),
                rs.getString("cc_number"));
    }

    private Vehicle mapVehicle(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new Vehicle(rs.getLong("vehicle_id"),
                rs.getString("licence_plate"),
                rs.getString("make"),
                rs.getString("model"),
                rs.getDate("vehicle_date"),
                rs.getString("category").charAt(0));
    }

    private VehicleAuthorization mapVehicleAuthorization(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new VehicleAuthorization(rs.getLong("vehicle_authorization_id_id"),
                rs.getString("cc_number_owner"),
                rs.getString("cc_number_driver"),
                rs.getString("licence_plate"),
                rs.getBoolean("_active"),
                rs.getDate("authorization_start"),
                rs.getDate("authorization_end"));
    }

    private VehicleOwner mapVehicleOwner(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new VehicleOwner(rs.getLong("vehicle_owner_id"),
                rs.getString("cc_number"),
                rs.getString("licence_plate"),
                rs.getBoolean("_active"),
                rs.getDate("ownership_start"),
                rs.getDate("ownership_end"));
    }

    private VehicleDriver mapVehicleDriver(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new VehicleDriver(rs.getLong("vehicle_driver_id"),
                rs.getString("cc_number"),
                rs.getString("licence_plate"),
                rs.getBoolean("_active"),
                rs.getDate("driving_start"),
                rs.getDate("driving_end"));
    }

    private Infraction mapInfraction(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new Infraction(rs.getLong("infraction_id"),
                rs.getString("licence_plate"),
                rs.getLong("radar_id"),
                rs.getDate("infraction_date_time"),
                rs.getDouble("price"),
                rs.getBoolean("paid"),
                rs.getDate("payment_day_time"));
    }

    private SpeedInfraction mapSpeedInfraction(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;

        Date d;
        try {
            d = rs.getDate("payment_day_time");
        } catch (Exception e) {
            d = null;
        }

        return new SpeedInfraction(rs.getLong("infraction_id"),
                rs.getString("licence_plate"),
                rs.getLong("radar_id"),
                rs.getDate("infraction_date_time"),
                rs.getDouble("price"),
                rs.getBoolean("paid"),
                d,
                rs.getInt("vehicle_speed"),
                rs.getInt("speed_limit"),
                rs.getString("direction"),
                rs.getInt("chasing_vehicle_speed"));
    }

    private RedLightInfraction mapRedLightInfraction(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;

        Date d;
        try {
            d = rs.getDate("payment_day_time");
        } catch (Exception e) {
            d = null;
        }

        return new RedLightInfraction(rs.getLong("infraction_id"),
                rs.getString("licence_plate"),
                rs.getLong("radar_id"),
                rs.getDate("infraction_date_time"),
                rs.getDouble("price"),
                rs.getBoolean("paid"),
                d,
                rs.getInt("vehicle_speed"),
                rs.getInt("elapsed_time"),
                rs.getInt("yellow_duration"));
    }

    private DistanceInfraction mapDistanceInfraction(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;

        Date d;
        try {
            d = rs.getDate("payment_day_time");
        } catch (Exception e) {
            d = null;
        }

        return new DistanceInfraction(rs.getLong("infraction_id"),
                rs.getString("licence_plate"),
                rs.getLong("radar_id"),
                rs.getDate("infraction_date_time"),
                rs.getDouble("price"),
                rs.getBoolean("paid"),
                d,
                rs.getInt("distance_to_next_vehicle"),
                rs.getInt("distance_limit"));
    }

    private Notification mapNotification(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new Notification(rs.getLong("notification_id"),
                rs.getLong("infraction_id"),
                rs.getString("cc_number"),
                rs.getBoolean("notified"),
                rs.getDate("notification_time"));
    }

    private Radar mapRadar(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new Radar(rs.getLong("radar_id"),
                rs.getString("latitude"),
                rs.getString("longitude"),
                rs.getString("address"),
                rs.getInt("kilometer"),
                rs.getString("direction"));
    }
}