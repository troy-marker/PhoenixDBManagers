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
     * Gets id.
     *
     * @return the id
     */
    Integer getId() {
        return id;
    }

    /**
     * Gets department.
     *
     * @return the department
     */
    String getDepartment() {
        return department;
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
