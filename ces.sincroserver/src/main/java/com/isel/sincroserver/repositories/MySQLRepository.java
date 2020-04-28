package com.isel.sincroserver.repositories;

import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.entities.Driver;
import com.isel.sincroserver.entities.Infraction;
import com.isel.sincroserver.entities.Radar;
import com.isel.sincroserver.entities.Vehicle;
import com.isel.sincroserver.interfaces.repositories.Repository;
import com.isel.sincroserver.interfaces.resources.Resources;
import com.isel.sincroserver.resources.Query;
import com.isel.sincroserver.resources.utils.Utils;
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
    public int insertDriver(Driver driver) throws SincroServerException {
        Query query = resources.getQuery(Constants.INSERT_DRIVER);

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("first_name", driver.getFirst_name())
                .addValue("middle_name", driver.getMiddle_name())
                .addValue("last_name", driver.getLast_name())
                .addValue("driver_licence_number", driver.getDriver_licence_number())
                .addValue("cc_number", driver.getCc_number())
                .addValue("phone_number", driver.getPhone_number())
                .addValue("email", driver.getEmail());

        logger.info(Constants.INSERT_DRIVER + ": " + query.query);
        logger.debug(Constants.INSERT_DRIVER + ": " + driver.toString());

        int rows = namedParameterJdbcTemplate.update(query.query, parameters, holder);

        driver.setDriver_id(holder.getKey().longValue());

        return rows;
    }

    @Override
    public int insertVehicle(Vehicle vehicle) throws SincroServerException {
        return 0;
    }

    @Override
    public int associateVehicleAndDriver(Driver driver, Vehicle vehicle) throws SincroServerException {
        return 0;
    }

    @Override
    public int insertRadar(Radar radar) throws SincroServerException {
        return 0;
    }

    @Override
    public int insertInfraction(Infraction infraction) throws SincroServerException {
        return 0;
    }

    @Override
    public int deleteDriver(Driver driver) throws SincroServerException {
        Query query = resources.getQuery(Constants.DELETE_DRIVER);
        QueryObject queryObj = buildQuery(driver, query);

        logger.info(Constants.DELETE_DRIVER + ": " + queryObj.stmt);
        logger.debug(Constants.DELETE_DRIVER + ": " + driver.toString());

        return namedParameterJdbcTemplate.update(queryObj.stmt, queryObj.parameters);
    }

    @Override
    public int deleteVehicle(Vehicle vehicle) throws SincroServerException {
        return 0;
    }

    @Override
    public int dissociateVehicleAndDriver(Driver driver, Vehicle vehicle) throws SincroServerException {
        return 0;
    }

    @Override
    public int deleteRadar(Radar radar) throws SincroServerException {
        return 0;
    }

    @Override
    public int deleteInfraction(Infraction infraction) throws SincroServerException {
        return 0;
    }

    @Override
    public int updateDriver(Driver driver) throws SincroServerException {
        Query query = resources.getQuery(Constants.UPDATE_DRIVER);
        QueryObject queryObj = buildQuery(driver, query);

        logger.info(Constants.UPDATE_DRIVER + ": " + queryObj.stmt);
        logger.debug(Constants.UPDATE_DRIVER + ": " + driver.toString());

        queryObj.parameters.addValue("first_name", driver.getFirst_name())
                           .addValue("middle_name", driver.getMiddle_name())
                           .addValue("last_name", driver.getLast_name())
                           .addValue("driver_licence_number", driver.getDriver_licence_number())
                           .addValue("cc_number", driver.getCc_number())
                           .addValue("phone_number", driver.getPhone_number())
                           .addValue("email", driver.getEmail());

        return namedParameterJdbcTemplate.update(queryObj.stmt, queryObj.parameters);
    }

    @Override
    public int updateVehicle(Vehicle vehicle) throws SincroServerException {
        return 0;
    }

    @Override
    public int updateVehicleAndDriver(Driver driver, Vehicle vehicle) throws SincroServerException {
        return 0;
    }

    @Override
    public int updateRadar(Radar radar) throws SincroServerException {
        return 0;
    }

    @Override
    public int updateInfraction(Infraction infraction) throws SincroServerException {
        return 0;
    }

    @Override
    public Driver getDriver(Driver driver) throws SincroServerException {
        Query query = resources.getQuery(Constants.GET_DRIVER);
        QueryObject queryObj = buildQuery(driver, query);

        logger.info(Constants.GET_DRIVER + ": " + queryObj.stmt);
        logger.debug(Constants.GET_DRIVER + ": " + driver.toString());

        return namedParameterJdbcTemplate.query(queryObj.stmt, queryObj.parameters, rs -> {
            return mapDriver(rs, false);
        });
    }

    @Override
    public List<Driver> getDrivers() throws SincroServerException {
        Query query = resources.getQuery(Constants.GET_DRIVER);

        logger.info(Constants.GET_DRIVERS + ": " + query.query);

        return namedParameterJdbcTemplate.query(query.query, (rs, rowNum) -> {
            return mapDriver(rs, true);
        });
    }

    @Override
    public List<Vehicle> getDriverVehicles(Driver driver) throws SincroServerException {
        return null;
    }

    @Override
    public Vehicle getVehicle(Vehicle vehicle) throws SincroServerException {
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        return null;
    }

    @Override
    public List<Driver> getVehicleDrivers(Vehicle vehicle) throws SincroServerException {
        return null;
    }

    @Override
    public Radar getRadar(Radar radar) throws SincroServerException {
        return null;
    }

    @Override
    public List<Radar> getRadars() throws SincroServerException {
        return null;
    }

    @Override
    public Infraction getInfraction(Infraction infraction) throws SincroServerException {
        return null;
    }

    @Override
    public List<Infraction> getInfractions() throws SincroServerException {
        return null;
    }

    @Override
    public List<Infraction> getDriverInfractions(Driver driver) throws SincroServerException {
        return null;
    }

    @Override
    public List<Infraction> getVehicleInfractions(Vehicle vehicle) throws SincroServerException {
        return null;
    }

    @Override
    public List<Infraction> getRadarInfractions(Radar radar) {
        return null;
    }

    private QueryObject buildQuery(Driver driver, Query query) {
        QueryObject queryObj = new QueryObject(query.query, new MapSqlParameterSource());

        if (driver.getDriver_id() != 0 && query.parameters.containsKey("driver_id")) {
            queryObj.stmt += query.parameters.get("driver_id");
            queryObj.parameters.addValue("driver_id", driver.getDriver_id());
        } else if (!Utils.isNullOrEmpty(driver.getDriver_licence_number()) && query.parameters.containsKey("driver_licence_number")) {
            queryObj.stmt += query.parameters.get("driver_licence_number");
            queryObj.parameters.addValue("driver_licence_number", driver.getDriver_licence_number());
        } else if (!Utils.isNullOrEmpty(driver.getCc_number()) && query.parameters.containsKey("cc_number")) {
            queryObj.stmt += query.parameters.get("cc_number");
            queryObj.parameters.addValue("cc_number", driver.getCc_number());
        } else if (!Utils.isNullOrEmpty(driver.getPhone_number()) && query.parameters.containsKey("phone_number")) {
            queryObj.stmt += query.parameters.get("phone_number");
            queryObj.parameters.addValue("phone_number", driver.getPhone_number());
        } else if (!Utils.isNullOrEmpty(driver.getEmail()) && query.parameters.containsKey("email")) {
            queryObj.stmt += query.parameters.get("email");
            queryObj.parameters.addValue("email", driver.getEmail());
        }

        queryObj.stmt += query.where;

        return queryObj;
    }

    private Driver mapDriver(ResultSet rs, boolean list) throws SQLException {
        if (!list && !rs.next()) return null;
        return new Driver(rs.getLong("driver_id"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("cc_number"),
                rs.getString("driver_licence_number"),
                rs.getString("phone_number"),
                rs.getString("email"));
    }

    private static class QueryObject {
        String stmt;
        MapSqlParameterSource parameters;

        public QueryObject(String stmt, MapSqlParameterSource parameters) {
            this.stmt = stmt;
            this.parameters = parameters;
        }

        public String getStmt() {
            return stmt;
        }

        public MapSqlParameterSource getParameters() {
            return parameters;
        }
    }

    private static class Constants {
        public static final String INSERT_DRIVER = "insertDriver";
        public static final String UPDATE_DRIVER = "updateDriver";
        public static final String DELETE_DRIVER = "deleteDriver";
        public static final String GET_DRIVER = "getDriver";
        public static final String GET_DRIVERS = "getDrivers";
    }
}