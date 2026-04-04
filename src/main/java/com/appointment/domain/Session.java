package com.appointment.domain;

/**
 * Represents the current administrator session.
 * Tracks whether an administrator is logged in.
 * @author Nada, Noora
 * @version 1.0
 */
public class Session {

    /** Currently logged-in administrator */
    private Administrator loggedInAdmin;

    /**
     * Logs an administrator into the session.
     *
     * @param admin the administrator to log in
     */
    public void login(Administrator admin) {
        this.loggedInAdmin = admin;
    }

    /**
     * Logs out the current administrator.
     */
    public void logout() {
        this.loggedInAdmin = null;
    }

    /**
     * Checks whether an administrator is currently logged in.
     *
     * @return true if an admin is logged in, false otherwise
     */
    public boolean isAdminLoggedIn() {
        return loggedInAdmin != null;
    }

    /**
     * Gets the currently logged-in administrator.
     *
     * @return logged-in administrator
     */
    public Administrator getLoggedInAdmin() {
        return loggedInAdmin;
    }
}