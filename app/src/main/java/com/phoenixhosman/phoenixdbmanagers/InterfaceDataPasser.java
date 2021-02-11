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

import android.os.Bundle;

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

    /**
     * Interface to pass the id of Grade to update
     * @param id Integer - the record number to update
     */
    void onGradeUpdate(int id);

    /**
     * Interface to pass the id of Grade to remove
     * @param id Integer - the record number to remove
     */
    void onGradeRemove(int id);

    /**
     * Interface to pass the id of Department to update
     * @param id Integer - the record number to update
     */
    void onDepartmentUpdate(int id);

    /**
     * Interface to pass the id of Department to remove
     * @param id Integer - the record number to remove
     */
    void onDepartmentRemove(int id);

    /**
     * Interface to add a new supplier to the database
     */
    void onSupplierAdd();

    /**
     * Interface to call the item list fragment
     */
    void onItemList(Bundle data);

    /**
     * Interface to call the item information fragment
     */
    void onItemInfo(Bundle data);

    /**
     * Interface to call the item pricing fragment
     */
    void onItemPrice(Bundle data);

    /**
     * Interface to close the supplier addition frame
     */
    void onCloseSupplier();

}
