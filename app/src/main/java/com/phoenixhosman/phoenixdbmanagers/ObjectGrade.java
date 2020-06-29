/*
    The Phoenix Hospitality Management System
    Database Manager App
    Grade Object Definition File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * The type ObjectGrade.
 */
public class ObjectGrade {
    @SerializedName("id") private Integer id;
    @SerializedName("grade") private String grade;

    /**
     * Instantiates a new ObjectGrade.
     *
     * @param id    the id
     * @param grade the grade
     */
    @SuppressWarnings("unused")
    ObjectGrade(Integer id, String grade) {
        this.id = id;
        this.grade = grade;
    }

    /**
     * Default Constructor
     */
    ObjectGrade() {

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
     * Gets the grade
     * @param id Integer - numeric grade id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets grade.
     *
     * @return the grade
     */
    String getGrade() {
        return grade;
    }

    /**
     * Gets the grade name
     *
     * @param grade - String - the grade name
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Override of the toString method
     *
     * @return String-the access grade name
     */
    @NonNull
    @Override
    public String toString() {
        return grade;
    }
}
