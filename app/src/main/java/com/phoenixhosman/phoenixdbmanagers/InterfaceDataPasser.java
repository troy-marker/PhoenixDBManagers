/*
    The Phoenix Hospitality Management System
    Database Manager App
    Data Passer Interface Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

/**
 * The Data Passer Interface file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public interface InterfaceDataPasser {

    /**
     * Interface to pass the id of User to update
     * @param id Integer - the record number to update
     */
    void onUserUpdate(int id);

    /**
     * Interface to pass the id of User to remove
     * @param id Integer - the record number to remove
     */
    void onUserRemove(int id);
}
