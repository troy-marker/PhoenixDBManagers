/*
    The Phoenix Hospitality Management System
    Database Manager App
    Menu Object Definition File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

/**
 * The type Object menu.
 */
public class ObjectMenu {
    /**
     * Private string holder variable for the menu name
     */
    private String mName;

    /**
     * Constructor for a menu item
     */
    ObjectMenu(String name) {
        mName = name;
    }

    /**
     * Gets the menu name.
     * @return String - the menu name
     */
    public String getName() {
        return mName;
    }
}
