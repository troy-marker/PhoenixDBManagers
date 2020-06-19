package com.phoenixhosman.phoenixdbmanagers;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * The type ObjectDepartment.
 */
@Keep
class ObjectDepartment {
    @SerializedName("id") private Integer id;
    @SerializedName("department") private String department;

    /**
     * Instantiates a new ObjectDepartment.
     *
     * @param id         the id
     * @param department the department
     */
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
