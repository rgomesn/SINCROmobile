package com.isel.sincroserver.config;

import java.util.ArrayList;

import com.isel.sincroserver.controllers.RegistrationController;
import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.services.MySQLService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Qualifier("JwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger logger = LogManager.getLogger(JwtUserDetailsService.class);
    @Qualifier("MySQLService")
    private final MySQLService mySQLService;

    public JwtUserDetailsService(MySQLService mySQLService) {
        this.mySQLService = mySQLService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.isel.sincroserver.entities.User user = null;

        try {
            user = mySQLService.getUser(username);
        } catch (SincroServerException e) {
            logger.error(e);
        }
        if (user != null) {
            return new User(user.getUsername(), user.getPwd(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public boolean authenticate(String username, String password) throws UsernameNotFoundException {
        com.isel.sincroserver.entities.User user = null;

        try {
            user = mySQLService.getUser(username);
        } catch (SincroServerException e) {
            logger.error(e);
        }
        if (user != null) {
            return username.equals(user.getUsername()) && password.equals(user.getPwd());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public com.isel.sincroserver.entities.User loadUser(String username) throws SincroServerException {
        com.isel.sincroserver.entities.User user = mySQLService.getUser(username);

        if (user != null) {
            return user;
        } else {
            throw new SincroServerException("User not found with username: " + username);
        }
    }
}