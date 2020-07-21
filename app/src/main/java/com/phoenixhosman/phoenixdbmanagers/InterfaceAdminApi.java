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
     * @return the sugle user read
     */
    @GET("user/{id}")
    Call<String> user(@Path("id") int id);

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
            @Field("department") int department );

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
            @Field("department") int department);

    /**
     * Delete a user from the database
     * @param id - The id of the user to delete
     * @return JSON String - the deletion results
     */
    @GET("duser/{id}")
    Call<String> duser(
            @Path("id") int id);

    @FormUrlEncoded
    @POST("grade")
    Call<String> grade(
            @Field("grade") String grade);
}
