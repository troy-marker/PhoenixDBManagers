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

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    String getUsername() {
        return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    String getPassword() {
        return password;
    }

    /**
     * Gets created.
     *
     * @return the created date
     */
    String getCreated() {
        return created;
    }

    /**
     * Gets modified.
     *
     * @return the modified date
     */
    String getModified() {
        return modified;
    }

    /**
     * Gets grade.
     *
     * @return the grade
     */
    int getGrade() {
        return grade;
    }

    /**
     * Gets the GradeName
     *
     * @return gradename
     */
    String getGradeName() {
        return gradename;
    }

    /**
     * Gets department.
     *
     * @return the department
     */
    int getDepartment() {
        return department;
    }

    /**
     * Gets the deaprtment name
     *
     * @return the department name
     */
    String getDepartmentname() {
        return departmentname;
    }
    /**
     * Overrider of the toString method
     *
     * @return String username
     */
    @Override
    public String toString() {
        return username;
    }
}
