/*
    The Phoenix Hospitality Management System
    Database Manager App
    User Object Definition File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

/**
 * The ObjectUser type definition
 */
public class ObjectUser {
    private Integer id;
    private String username;
    private String password;
    private String created;
    private String modified;
    private Integer grade;
    private String gradename;
    private Integer department;
    private String departmentname;

    /**
     * Instantiates a new Object user.
     *
     * @param id         the id
     * @param username   the username
     * @param password   the password
     * @param created    the created
     * @param modified   the modified
     * @param grade      the grade
     * @param department the department
     */
    ObjectUser(Integer id, String username, String password, String created, String modified, Integer grade, String gradename, Integer department, String departmentname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.created = created;
        this.modified = modified;
        this.grade = grade;
        this.gradename = gradename;
        this.department = department;
        this.departmentname = departmentname;
    }

    public ObjectUser() {
    }

    /**
     * Gets the value of the id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Set Id
     * @param value int value of the new id
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets username.
     * @return the username
     */
    String getUsername() {
        return username;
    }

    /**
     * Set the username
     * @param value String the value of the username
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets password.
     * @return the password
     */
    String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param value String value for the password
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets date/time the user was created.
     * @return the created date
     */
    String getCreated() {
        return created;
    }

    /**
     * Sets the date/time the user was created
     * @param value String the date/time the user was created
     */
    public void setCreated(String value) {
        this.created = value;
    }

    /**
     * Gets the date/time the user was last modified
     * @return the modified date
     */
    String getModified() {
        return modified;
    }

    /**
     * Sets the date/time the user was last modified
     * @param value the date/time the user was last modified
     */
    public void setModified(String value) {
        this.modified = value;
    }

    /**
     * Gets grade id number
     * @return the grade
     */
    int getGrade() {
        return grade;
    }

    /**
     * Sets users grade id
     * @param value users grade id
     */
    public void setGrade(int value) {
        this.grade = value;
    }

    /**
     * Gets the Grade Name
     * @return gradename
     */
    String getGradeName() {
        return gradename;
    }

    /**
     * Sets the user grade text name
     * @param value the users grade text name
     */
    public void setGradeName(String value) {
        this.gradename = value;
    }

    /**
     * Gets department id
     * @return the department
     */
    int getDepartment() {
        return department;
    }

    /**
     * Sets the user's department id
     * @param value the user's department id
     */
    public void setDepartment(int value) {
        this.department = value;
    }

    /**
     * Gets the users department text name
     * @return the department text name
     */
    String getDepartmentname() {
        return departmentname;
    }

    /**
     * Sets the user's department text name
     * @param value the user's department text name
     */
    public void setDepartmentname(String value) {
        this.departmentname = value;
    }
}
