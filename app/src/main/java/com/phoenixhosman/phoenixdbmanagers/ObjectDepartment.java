/*
    The Phoenix Hospitality Management System
    Database Manager App
    Department Object Definition File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * The type ObjectDepartment.
 */

public class ObjectDepartment {
    @SerializedName("id") private Integer id;
    @SerializedName("department") private String department;

    /**
     * Instantiates a new ObjectDepartment.
     *
     * @param id         the id
     * @param department the department
     */
    @SuppressWarnings("unused")
    ObjectDepartment(Integer id, String department) {
        this.id = id;
        this.department = department;
    }

    /**
     * Default Constructor
     */
    ObjectDepartment() {

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    @SuppressWarnings("unused")
    Integer getId() {
        return id;
    }

    /**
     * Sets the department id
     * @param value the department id
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets department name
     * @return the department name
     */
    String getDepartment() {
        return department;
    }

    /**
     * Sets the department name
     * @param value the department name
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Override of the toString method
     *
     * @return String - the department name
     */
    @NonNull
    @Override
    public String toString() {
        return department;
    }
}
