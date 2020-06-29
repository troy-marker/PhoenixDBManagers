/*
    The Phoenix Hospitality Management System
    Database Manager App
    Admin API Manager Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class ManagerAdminApi {
    private static InterfaceAdminApi service;
    private static ManagerAdminApi managerAdminApi;
    private static String ApiUrl;

    ManagerAdminApi(String strApiUrl) {
        ApiUrl = strApiUrl;
        String BASE_URL = ApiUrl;
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
}
