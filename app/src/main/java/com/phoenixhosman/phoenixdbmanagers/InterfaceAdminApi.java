package com.phoenixhosman.phoenixdbmanagers;

import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @FormUrlEncoded
    @POST("user")
    Call<String> user(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grade") int grade,
            @Field("department") int department );

    @FormUrlEncoded
    @POST("ruser")
    Call<String> ruser(
            @Field("id") int id,
            @Field("username") String username,
            @Field("grade") int grade,
            @Field("department") int department);

}
