package com.appointment.domain;

public class Administrator extends User {
    private final String username;
    private final String password;

    public Administrator(String id, String name, String username, String password) {
        super(id, name);
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public boolean isPasswordCorrect(String inputPassword) {
        return password.equals(inputPassword);
    }
}