package com.appointment.domain;

/**
 * Represents the different types of appointments supported by the system.
 */
public enum AppointmentType {
    /** Urgent appointment */
    URGENT,

    /** Follow-up appointment */
    FOLLOW_UP,

    /** Assessment appointment */
    ASSESSMENT,

    /** Virtual appointment */
    VIRTUAL,

    /** In-person appointment */
    IN_PERSON,

    /** Individual appointment */
    INDIVIDUAL,

    /** Group appointment */
    GROUP
}