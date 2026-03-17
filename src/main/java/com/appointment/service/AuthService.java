package com.appointment.service;

import com.appointment.domain.Administrator;
import com.appointment.domain.Session;

public class AuthService {

    public boolean login(Session session, Administrator admin, String username, String password) {

        if (admin.getUsername().equals(username) && admin.isPasswordCorrect(password)) {
            session.login(admin);
            return true;
        }

        return false;
    }

    public void logout(Session session) {
        session.logout();
    }
}