package com.appointment.domain;

public class Session {
    private Administrator loggedInAdmin;

    public void login(Administrator admin) {
        this.loggedInAdmin = admin;
    }

    public void logout() {
        this.loggedInAdmin = null;
    }

    public boolean isAdminLoggedIn() {
        return loggedInAdmin != null;
    }

    public Administrator getLoggedInAdmin() {
        return loggedInAdmin;
    }
}