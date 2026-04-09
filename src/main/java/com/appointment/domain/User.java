package com.appointment.domain;

/**
 * Represents a system user who can book appointments.
 * Each user has a unique identifier, a name, and an email address.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public class User {

    /** Unique identifier for the user */
    private final String id;

    /** Name of the user */
    private final String name;

    /** Email address of the user */
    private final String email;

    /**
     * Constructs a new User.
     *
     * @param id unique user identifier
     * @param name user's name
     * @param email user's email address
     */
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the user ID.
     *
     * @return user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the user name.
     *
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user email.
     *
     * @return user email
     */
    public String getEmail() {
        return email;
    }
}