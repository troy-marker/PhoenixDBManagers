/*
    The Phoenix Hospitality Management System
    Database Manager App
    Department Remove Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.phoenixhosman.phoenixlib.ActivityPhoenixLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Code for the department remove fragment
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentDepartmentRemove extends Fragment implements View.OnClickListener {
    String apiUrl;
    String  coName;
    Integer record;
    TextView txtQuestion;
    TextView tvTitle;
    Button btnYes;
    Button btnNo;
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_remove, container, false);
        assert getArguments() != null;
        coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        record = getArguments().getInt("record");
        tvTitle = view.findViewById(R.id.tvTitle);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        tvTitle.setText(getString(R.string.title_remove, coName, getString(R.string.var_department)));
        txtQuestion = view.findViewById(R.id.txtDepartmentRemoveQuestion);
        readDepartment();
        return view;
    }

    /**
     * Method to read a department from the database
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
                            String dataString = obj.optString("data");
                            txtQuestion.setText(getString(R.string.remove_question,getString(R.string.var_department), dataString));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Phoenix.Error(Phoenix.getApplicationContext(), getResources().getString(R.string.loadfail, "department"), false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * The view onclick listener
     * @param v - Thew view being clicked
     */
    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        String buttonText = button.getText().toString();
        if (buttonText.equals(getResources().getString(R.string.no))) { // No pressed
            ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
        } else if (buttonText.equals(getResources().getString(R.string.yes))) { // Yes pressed
            Call<String> call = ManagerAdminApi.getInstance().getApi().ddepartment(record);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        try {
                            assert response.body() != null;
                            JSONObject obj = new JSONObject(response.body());
                            if (obj.optBoolean("success")) {
                                Phoenix.Success(Phoenix.getApplicationContext(),obj.optString("message"), 5);
                                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                            }
                            if (!obj.optBoolean("success")) {
                                Phoenix.Error(Phoenix.getApplicationContext(), getResources().getString(R.string.deletefail, "department"), false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Phoenix.Error(Phoenix.getApplicationContext(), getResources().getString(R.string.loadfail, "department"), false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                }
            });
            ((ActivityMain) Objects.requireNonNull(getActivity())).LoadDepartmentList();
        } else {
            throw new IllegalStateException(getResources().getString(R.string.unexpected) + v.getId());
        }
    }
}