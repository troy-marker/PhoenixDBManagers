/*
    The Phoenix Hospitality Management System
    Database Manager App
    Department Update Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDepartmentUpdate extends Fragment implements View.OnClickListener {
    String apiUrl;
    String coName;
    Integer record;
    Button btnUpdateDepartment;
    Button btnCancel;
    EditText etDepartmentname;

    public FragmentDepartmentUpdate() {
    }

    /**
     * The fragment onCreateView Method
     *
     * @param inflater the layout inflater
     * @param container the container that will hold the fragment
     * @param savedInstanceState the saved instance state
     * @return View object container the fragment view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_update, container, false);
        assert getArguments() != null;
        coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        record = getArguments().getInt("record");
        btnUpdateDepartment = view.findViewById(R.id.btnUpdateDepartment);
        btnCancel = view.findViewById(R.id.btnCancel);
        etDepartmentname = view.findViewById(R.id.etDepartmentname);
        new ManagerAdminApi(apiUrl);
        btnUpdateDepartment.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        etDepartmentname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.department_update_title, coName));
        readDepartment();
        return view;
    }

    /**
     * Method to read a grade from the database
     */
    private void readDepartment() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().department(record);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if(obj.optString("success").equals("true")) {
                            DisplayDepartment(obj.optString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(),"Failed loading user information", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to display the grade on screen
     */
    public void DisplayDepartment(String departmentname) {
        etDepartmentname.setText(departmentname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                break;
            case R.id.btnUpdateDepartment:
                if (etDepartmentname.getText().toString().isEmpty()) {
                    ((ActivityMain) Objects.requireNonNull(getActivity())).Error("A department name is required", false);
                } else {
                    Call<String> call = ManagerAdminApi.getInstance().getApi().rdepartment(record, etDepartmentname.getText().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            String body = response.body();
                            try {
                                assert body != null;
                                JSONObject obj = new JSONObject(body);
                                if (obj.optString("success").equals("false")) {
                                    ((ActivityMain) Objects.requireNonNull(getActivity())).Error(obj.optString("message"), false);
                                } else {
                                    ((ActivityMain) Objects.requireNonNull(getActivity())).Success(obj.optString("message"));
                                    ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                                    ((ActivityMain) Objects.requireNonNull(getActivity())).LoadDepartmentList();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        }
                    });
                }
                break;
        }
    }
}
