package com.phoenixhosman.phoenixdbmanagers;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ManagerAdminApi {
    private static InterfaceAdminApi service;
    private static ManagerAdminApi managerAdminApi;
    private static String ApiUrl;


    ManagerAdminApi(String strApiUrl) {
        ApiUrl = strApiUrl;
        String BASE_URL = ApiUrl;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        service = retrofit.create(InterfaceAdminApi.class);
    }

    @SuppressWarnings("unused")
    static synchronized ManagerAdminApi getInstance() {
        if (managerAdminApi == null) {
            managerAdminApi = new ManagerAdminApi(ApiUrl);
        }
        return managerAdminApi;
    }

    @SuppressWarnings("unused")
    InterfaceAdminApi getApi() {
        return service;
    }

    void user(Callback<ObjectUserResponse> callback) {
        Call<ObjectUserResponse> getusersCall = service.user();
        getusersCall.enqueue(callback);
    }

    /**
     * Gets grade name.
     *  @param grade    the grade
     * @param callback the callback
     */
    void getGradeName(int grade, Callback<ObjectGradeResponse> callback) {
        Call<ObjectGradeResponse> gradeNameCall = service.getGradeName(grade);
        gradeNameCall.enqueue(callback);
    }

    /**
     * Gets department name.
     *
     * @param department the department number
     * @param callback the callback
     */
    void getDepartmentName(int department, Callback<ArrayList<ObjectDepartment>> callback) {
        Call<ArrayList<ObjectDepartment>> departmentNameCall = service.getDepartmentName(department);
        departmentNameCall.enqueue(callback);
    }

    /**
     * Gets all grade in the database
     *
     * @param callback the callback
     */
    void getGradeList(Callback<ArrayList<ObjectGrade>> callback) {
        Call<ArrayList<ObjectGrade>> gradeListCall = service.getGradeList();
        gradeListCall.enqueue(callback);
    }
}
