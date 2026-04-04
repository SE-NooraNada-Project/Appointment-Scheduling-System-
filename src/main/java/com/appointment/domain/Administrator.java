package com.appointment.domain;

/**
 * Represents an administrator in the system.
 * An administrator is a specialized user with login credentials.
 * @author Nada, Noora
 * @version 1.0
 */
public class Administrator extends User {

    /** Username used for administrator login */
    private final String username;

    /** Password used for administrator login */
    private final String password;

    /**
     * Constructs a new Administrator.
     *
     * @param id administrator ID
     * @param name administrator name
     * @param username login username
     * @param password login password
     */
    public Administrator(String id, String name, String username, String password) {
        super(id, name);
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the administrator username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks whether the provided password matches the stored password.
     *
     * @param inputPassword password entered by the user
     * @return true if the password is correct, false otherwise
     */
    public boolean isPasswordCorrect(String inputPassword) {
        return password.equals(inputPassword);
    }
}