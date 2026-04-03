package com.appointment.service;

import com.appointment.domain.Administrator;
import com.appointment.domain.Session;

/**
 * Service responsible for administrator authentication operations,
 * including login and logout.
 */
public class AuthService {

    /**
     * Attempts to log an administrator into the system.
     *
     * @param session current session
     * @param admin administrator object
     * @param username entered username
     * @param password entered password
     * @return true if login succeeds, false otherwise
     */
    public boolean login(Session session, Administrator admin, String username, String password) {

        if (session == null || admin == null || username == null || password == null) {
            return false;
        }

        if (admin.getUsername().equals(username) && admin.isPasswordCorrect(password)) {
            session.login(admin);
            return true;
        }

        return false;
    }

    /**
     * Logs the current administrator out of the system.
     *
     * @param session current session
     */
    public void logout(Session session) {
        if (session != null) {
            session.logout();
        }

    }
}