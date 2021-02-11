/*
    The Phoenix Hospitality Management System
    Database Manager App
    Admin Api Interface Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The Admin API Interface file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public interface InterfaceAdminApi {

    /**
     * Gets the userlist
     * @return the current user list
     */
    @GET("user")
    Call<String> user();

    /**
     * Get a single user from the database
     * @return the single user to read
     */
    @GET("user/{id}")
    Call<String> user(
        @Path("id") int id
    );

    /**
     * Gets the grade list
     * @return the grade list
     */
    @GET("grade")
    Call<String> grade();

    /**
     * Gets the department list
     * @return the department list
     */

    @GET("department")
    Call<String> department();

    /**
     * Returns information about a user
     * @param username String - the username
     * @param password String - the users hashed password
     * @param grade Integer - the user's access grade
     * @param department Integer - the user's department number
     * @return JSON String - this users information
     */
    @FormUrlEncoded
    @POST("user")
    Call<String> user(
        @Field("username") String username,
        @Field("password") String password,
        @Field("grade") int grade,
        @Field("department") int department
    );

    /**
     * Returns information about a user to be replaced/updated
     * @param id Integer - the users record number in the database
     * @param username Sting - the username
     * @param grade Integer - the user's access grade
     * @param department Integer - the user's department number
     * @return JSON String - this users updated information
     */
    @FormUrlEncoded
    @POST("ruser")
    Call<String> ruser(
        @Field("id") int id,
        @Field("username") String username,
        @Field("grade") int grade,
        @Field("department") int department
    );

    /**
     * Delete a user from the database
     * @param id - The id of the user to delete
     * @return JSON String - the deletion results
     */
    @GET("duser/{id}")
    Call<String> duser(
        @Path("id") int id
    );

    /**
     * Delete a grade from the database
     * @param id - The id of the grade to delete
     * @return JSON String - the deletion results
     */
    @GET("dgrade/{id}")
    Call<String> dgrade(
        @Path("id") int id
    );

    /**
     * Read a single grade from the database
     * @param id - The grade id to read
     * @return JSON String - the read results
     */
    @GET("grade/{id}")
    Call<String> grade(
        @Path("id") int id
    );

    /**
     * Add a single grade from the database
     * @param grade grade to add
     * @return JSON string - the addition results
     */
    @FormUrlEncoded
    @POST("grade")
    Call<String> grade(
        @Field("grade") String grade
    );

    /**
     * Replace a grade
     * @param id grade to replace
     * @param grade new grade name
     * @return JSON string - the replacement results
     */
    @FormUrlEncoded
    @POST("rgrade")
    Call<String> rgrade(
        @Field("id") int id,
        @Field("grade") String grade
    );

    /**
     * Add new department to the database
     * @param department department name to add
     * @return JSON string - the addition results
     */
    @FormUrlEncoded
    @POST("department")
    Call<String> department(
        @Field("department") String department
    );

    /**
     * Return a single department form the database
     * @param id the dpeartment id to return
     * @return JSON string - the method results
     */
    @GET("department/{id}")
    Call<String> department(
        @Path("id") int id
    );

    /**
     * Replace a department in the database
     * @param id the department number to replace
     * @param department the new department name
     * @return JSON string containing th emethod results
     */
    @FormUrlEncoded
    @POST("rdepartment")
    Call<String> rdepartment(
        @Field("id") int id,
        @Field("department") String department
    );

    /**
     * delete a single department from the database
     * @param id the department id to delete
     * @return JSON string - the deletion results
     */
    @GET("ddepartment/{id}")
    Call<String> ddepartment(
        @Path("id") int id
    );

    /**
     * Get the complete list of suppliers from the database
     * @return  JSON string - the method results
     */
    @GET("suppliers")
    Call<String> suppliers();

    /**
     * Get the list of categories given a supplier
     * @param supplier the parent supplier
     * @return JSON string - the method results
     */
    @FormUrlEncoded
    @POST("category")
    Call<String> category(
        @Field("supplier") int supplier
    );

    /**
     * Get the list of subcategories given the category number
     * @param category the parent category
     * @return JSON string - the method results
     */
    @FormUrlEncoded
    @POST("subcategory")
    Call<String> subcategory(
        @Field("category") int category
    );

    /**
     * Get this list of item given the supplier, category and subcategory
     * @param supplier int - the supplier number
     * @param category int - the category number
     * @param subcategory int - the subcategory number
     * @return JSON string - the method results
     */
    @FormUrlEncoded
    @POST("item")
    Call<String> item(
            @Field("supplier") int supplier,
            @Field("category") int category,
            @Field("subcategory") int subcategory
    );

    /**
     * Save the item details
     * @param item String - the item number
     * @param supplier int - the supplier number
     * @param category int - the category number
     * @param subcategory int - the subcategory number
     * @param description String - the item description
     * @param casecount int - the case count
     * @param count int - the item count in inventory
     * @return JSON string - the method results
     */
    @FormUrlEncoded
    @POST("details")
    Call<String> details(
            @Field("item") String item,
            @Field("supplier") int supplier,
            @Field("category") int category,
            @Field("subcategory") int subcategory,
            @Field("description") String description,
            @Field("casecount") int casecount,
            @Field("count") int count
    );

    /**
     * Method call to load information on a single item
     * @param item the item number
     * @param supplier the supplier number
     * @param category the category number
     * @param subcategory the subcategory number
     * @return JSON string the method results
     */
    @FormUrlEncoded
    @POST("detail")
    Call<String> detail (
            @Field("item") String item,
            @Field("supplier") int supplier,
            @Field("category") int category,
            @Field("subcategory") int subcategory
    );

    /**
     * Method call to save item detail to the database
     * @param item the item number
     * @param supplier the supplier number
     * @param category the category number
     * @param subcategory the subcategory number
     * @param tier1count tier 1 count
     * @param tier1cost tier 1 cost
     * @param tier2count tier 2 count
     * @param tier2cost tier 2 cost
     * @param tier3count tier 3 count
     * @param tier3cost tier 3 cost
     * @return JSON string the method results
     */
    @FormUrlEncoded
    @POST("pricings")
    Call<String> pricings (
            @Field("item") String item,
            @Field("supplier") int supplier,
            @Field("category") int category,
            @Field("subcategory") int subcategory,
            @Field("tier1count") int tier1count,
            @Field("tier1cost") double tier1cost,
            @Field("tier2count") int tier2count,
            @Field("tier2cost") double tier2cost,
            @Field("tier3count") int tier3count,
            @Field("tier3cost") double tier3cost
    );

    /**
     * Method call to save pricing details to the database
     * @param item the item number
     * @param supplier the supplier number
     * @param category the category number
     * @param subcategory the subcategory number
     * @return JSON string the method results
     */
    @FormUrlEncoded
    @POST("pricing")
    Call<String> pricing (
            @Field("item") String item,
            @Field("supplier") int supplier,
            @Field("category") int category,
            @Field("subcategory") int subcategory
    );

}
