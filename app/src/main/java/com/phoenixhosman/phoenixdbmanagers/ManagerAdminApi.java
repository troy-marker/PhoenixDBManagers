package com.phoenixhosman.phoenixdbmanagers;

//import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class ManagerAdminApi {
    private static InterfaceAdminApi service;
    private static ManagerAdminApi managerAdminApi;
    //private static String factory;
    private static String ApiUrl;

    ManagerAdminApi(String strApiUrl) {
        ApiUrl = strApiUrl;
        String BASE_URL = ApiUrl;
        //ManagerAdminApi.factory = factory;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = retrofit.create(InterfaceAdminApi.class);
    }

    static synchronized ManagerAdminApi getInstance() {
        if (managerAdminApi == null) {
            managerAdminApi = new ManagerAdminApi(ApiUrl);
        }
        return managerAdminApi;
    }

    InterfaceAdminApi getApi() {
        return service;
    }

    /**
     * Gets all grade in the database
     * @param callback the callback function
     */
    void grade(Callback<String> callback) {
        Call<String> gradeListCall = service.grade();
        gradeListCall.enqueue(callback);
    }

    /**
     * Gets all departments in the database
     * @param callback the callback function
     */
    void department(Callback<String> callback) {
        Call<String> departmentListCall = service.department();
        departmentListCall.enqueue(callback);
    }

}
