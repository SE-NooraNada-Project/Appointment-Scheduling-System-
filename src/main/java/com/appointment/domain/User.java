package com.appointment.domain;

/**
 * Represents a system user who can book appointments.
 * Each user has a unique identifier and a name.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public class User {

    /** Unique identifier for the user */
    private final String id;

    /** Name of the user */
    private final String name;

    /**
     * Constructs a new User.
     *
     * @param id unique user identifier
     * @param name user's name
     */
    public User(String id, String name) {
        this.id = id;
        this.name = name;
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
}