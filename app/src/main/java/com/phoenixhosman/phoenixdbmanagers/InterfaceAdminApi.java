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
     *
     * @return the current user list
     */
    @GET("user")
    Call<ObjectUserResponse> user();

    /**
     * Gets grade name.
     *
     * @param grade the grade number
     * @return the grade name
     */
    @FormUrlEncoded
    @POST("gradename")
    Call<ObjectGradeResponse> getGradeName(@Field("id") int grade);

    /**
     * Gets department name.
     *
     * @param department the department number
     * @return the department name
     */
    @GET("getDepartmentName/{department}")
    Call<ArrayList<ObjectDepartment>> getDepartmentName(@Path("department") int department);

    /**
     * Gets the grade list
     *
     * @return the grade list
     */
    @GET("getGradeList")
    Call<ArrayList<ObjectGrade>> getGradeList();
}
