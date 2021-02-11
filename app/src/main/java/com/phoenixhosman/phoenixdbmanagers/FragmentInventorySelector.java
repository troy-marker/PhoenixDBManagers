/*
    The Phoenix Hospitality Management System
    Database Manager App
    Inventory Selector Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.phoenixhosman.phoenixapi.ObjectSubcategory;
import com.phoenixhosman.phoenixapi.ObjectSupplier;
import com.phoenixhosman.phoenixapi.ObjectCategory;

import com.phoenixhosman.phoenixlib.ActivityPhoenixLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.layout.simple_spinner_item;

public class FragmentInventorySelector extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spinnerSuppliers;
    Spinner spinnerCategory;
    Spinner spinnerSubcategory;
    Button buttonSupplier;
    Button buttonCategory;
    Button buttonSubcategory;
    int supplierNumber;
    int categoryNumber;
    int subcategoryNumber;
    Bundle data = new Bundle();
    ManagerAdminApi managerAdminApi;
    static InterfaceDataPasser dataPasser;
    private final ArrayList<ObjectSupplier> supplierList = new ArrayList<>();
    private final ArrayList<ObjectCategory> categoryList = new ArrayList<>();
    private final ArrayList<ObjectSubcategory> subcategoryList = new ArrayList<>();
    private final ArrayList<String> supplierNames = new ArrayList<>();
    private final ArrayList<String> categoryNames = new ArrayList<>();
    private final ArrayList<String> subcategoryNames = new ArrayList<>();
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_selector, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        String apiUrl = getArguments().getString("ApiUrl");
        managerAdminApi = new ManagerAdminApi(apiUrl);
        spinnerSuppliers = view.findViewById(R.id.spinnerSupplier);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerSubcategory = view.findViewById(R.id.spinnerSubcategory);
        buttonSupplier = view.findViewById(R.id.buttonSupplier);
        buttonCategory = view.findViewById(R.id.buttonCategory);
        buttonSubcategory = view.findViewById(R.id.buttonSubcategory);
        buttonSupplier.setEnabled(true);
        buttonSupplier.setOnClickListener(this);
        spinnerSuppliers.setOnItemSelectedListener(this);
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerSubcategory.setOnItemSelectedListener(this);
        readSuppliers();
        return view;
    }

    /**
     * Method to read the supplier list from the database
     */
    private void readSuppliers() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().suppliers();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displaySuppliers(jsonresponse);
                } else {
                    Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.failed_loading, getString(R.string.supplier)),false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to read the category list given the supplier number
     */
    private void readCategories(Integer $supplierid) {
        Call<String> call = ManagerAdminApi.getInstance().getApi().category($supplierid);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displayCategories(jsonresponse);
                } else {
                    Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.failed_loading, getString(R.string.category)), false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * Method to read the subcategory list given the category number
     */
    private void readSubcategory(Integer $categoryid) {
        Call<String> call = ManagerAdminApi.getInstance().getApi().subcategory($categoryid);
        call.enqueue(new Callback<String> () {

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displaySubcategory(jsonresponse);
                } else {
                    Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.failed_loading, getString(R.string.subcategory)), false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }
    /**
     * Method to load the suppliers into the supplier spinner
     * @param response JSON Object containing the Api response
     */
    private void displaySuppliers(String response){
        supplierList.clear();
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectSupplier supplierSpinner = new ObjectSupplier();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    supplierSpinner.setId(dataObj.getInt("id"));
                    supplierSpinner.setName(dataObj.getString("name"));
                    supplierSpinner.setAccount(dataObj.getString("account"));
                    supplierSpinner.setPhone(dataObj.getString("phone"));
                    supplierSpinner.setFax(dataObj.getString("fax"));
                    supplierSpinner.setWebsite(dataObj.getString("website"));
                    supplierList.add(supplierSpinner);
                }
                supplierNames.add(getString(R.string.select, getString(R.string.supplier)));
                for (int i = 0; i < supplierList.size(); i++){
                    supplierNames.add(supplierList.get(i).getName());
                }
                ArrayAdapter<String> supplierArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, supplierNames);
                supplierArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                spinnerSuppliers.setAdapter(supplierArrayAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load the categories into the category spinner given  the supplier number
     * @param response JSON Object containing the Api response
     */
    private void displayCategories(String response) {
        categoryList.clear();
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectCategory categorySpinner = new ObjectCategory();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    categorySpinner.setId(dataObj.getInt("id"));
                    categorySpinner.setPid(dataObj.getInt("p_id"));
                    categorySpinner.setName(dataObj.getString("name"));
                    categoryList.add(categorySpinner);
                }
                categoryNames.clear();
                categoryNames.add(getString(R.string.select, getString(R.string.category)));
                for (int i = 0; i < categoryList.size(); i++) {
                    categoryNames.add(categoryList.get(i).getName());
                }
                ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, categoryNames);
                categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                spinnerCategory.setAdapter(categoryArrayAdapter);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to load the subcategories into the subcategory spinner
     * @param response JSON Object containing the Api response
     */
    private void displaySubcategory(String response) {
        subcategoryList.clear();
        try {
            JSONObject obj  = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectSubcategory subcategorySpinner = new ObjectSubcategory();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    subcategorySpinner.setId(dataObj.getInt("id"));
                    subcategorySpinner.setPid(dataObj.getInt("p_id"));
                    subcategorySpinner.setName(dataObj.getString("name"));
                    subcategoryList.add(subcategorySpinner);
                }
                subcategoryNames.clear();
                subcategoryNames.add(getString(R.string.select, getString(R.string.subcategory)));
                for(int i = 0; i <subcategoryList.size(); i++) {
                    subcategoryNames.add(subcategoryList.get(i).getName());
                }
                ArrayAdapter<String> subcategoryArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, subcategoryNames);
                subcategoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                spinnerSubcategory.setAdapter(subcategoryArrayAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if (v==buttonSupplier) {
            dataPasser.onSupplierAdd();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (InterfaceDataPasser) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerSuppliers) {
            buttonCategory.setEnabled(false);
            spinnerCategory.setEnabled(false);
            buttonSubcategory.setEnabled(false);
            spinnerSubcategory.setEnabled(false);
            if (position > 0) {
                supplierNumber = supplierList.get(position - 1).getId();
                readCategories(supplierNumber);
                buttonCategory.setEnabled(true);
                spinnerCategory.setEnabled(true);
            }
        } else if (parent == spinnerCategory) {
            buttonSubcategory.setEnabled(false);
            spinnerSubcategory.setEnabled(false);
            if (position > 0) {
                categoryNumber = categoryList.get(position - 1).getId();
                readSubcategory(categoryNumber);
                buttonSubcategory.setEnabled(true);
                spinnerSubcategory.setEnabled(true);
            }
        } else if (parent == spinnerSubcategory) {
            if (position > 0) {
                subcategoryNumber = subcategoryList.get(position - 1).getId();
                data.putInt("supplier", supplierNumber);
                data.putInt("category", categoryNumber);
                data.putInt("subcategory", subcategoryNumber);
                dataPasser.onItemList(data);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
