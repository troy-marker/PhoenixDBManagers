/*
    The Phoenix Hospitality Management System
    Database Manager App
    SubMenu Object Definition File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

/**
 * Object Sub Menu Definition
 */
public class ObjectSubMenu {

    /**
     * The name of the sub-menu
     */
    private String mName;

    /**
     * The name of the parent menu
     */
    private String mParent;

    /**
     * Class constructor
     * @param parent The name of the parent menu
     * @param name The menu name
     */
    ObjectSubMenu(String parent, String name) {
        this.mParent = parent;
        this.mName = name;
    }

    /**
     * Get the parent name
     * @return Sting - the menu items parent
     */
    String getParent() {
        return mParent;
    }

    /**
     * Sets the menu's parent
     * @param value the menu parent
     */
    public void setParent(String value) {
        this.mParent = value;
    }

    /**
     * Get the menu name
     * @return String - the menu name
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the menu name
     * @param value the menu's name
     */
    public void setName(String value) {
        this.mName = value;
    }

}
