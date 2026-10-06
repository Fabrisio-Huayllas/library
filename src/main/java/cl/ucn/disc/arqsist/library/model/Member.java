/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** Represents a registered library member. */
@DatabaseTable(tableName = "members")
public final class Member {

    /** Primary key identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Full name of the member. */
    @DatabaseField(canBeNull = false)
    private String name;

    /** Email address of the member. */
    @DatabaseField(canBeNull = false)
    private String email;

    /** Default no-arg constructor for ORMLite. */
    public Member() {
    }

    /**
     * Creates a new Member with name and email.
     *
     * @param name the member's full name
     * @param email the member's email address
     */
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the member identifier.
     *
     * @return the member ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the member identifier.
     *
     * @param id the member ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the member's name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the member's email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the member's email address.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
