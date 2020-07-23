/*
    The Phoenix Hospitality Management System
    Database Manager App
    Grade Update Fragment Code File
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

public class FragmentGradeUpdate extends Fragment implements View.OnClickListener {
    String apiUrl;
    String coName;
    Integer record;
    Button btnUpdateGrade;
    Button btnCancel;
    EditText etGradename;

    public FragmentGradeUpdate() {
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
        View view = inflater.inflate(R.layout.fragment_grade_update, container, false);
        assert getArguments() != null;
        coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        record = getArguments().getInt("record");
        btnUpdateGrade = view.findViewById(R.id.btnUpdateGrade);
        btnCancel = view.findViewById(R.id.btnCancel);
        etGradename = view.findViewById(R.id.etGradename);
        new ManagerAdminApi(apiUrl);
        btnUpdateGrade.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        etGradename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.user_update_title, coName));
        readGrade();
        return view;
    }

    /**
     * Method to read a grade from the database
     */
    private void readGrade() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().grade(record);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if(obj.optString("success").equals("true")) {
                            DisplayGrade(obj.optString("data"));
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
    public void DisplayGrade(String gradename) {
        etGradename.setText(gradename);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                break;
            case R.id.btnUpdateGrade:
                break;
        }
    }
}
