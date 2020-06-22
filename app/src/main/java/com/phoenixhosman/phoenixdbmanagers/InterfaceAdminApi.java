package com.phoenixhosman.phoenixdbmanagers;

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
}
