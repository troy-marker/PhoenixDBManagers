/*
    The Phoenix Hospitality Management System
    Database Manager App
    Department Addition Fragment Code File
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
import android.widget.EditText;
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
 * Department Add Fragment code file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentDepartmentAdd extends Fragment implements View.OnClickListener {
    String apiUrl;
    Button btnAddDepartment;
    Button btnCancel;
    EditText etDepartmentname;
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public FragmentDepartmentAdd()  {
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
        View view = inflater.inflate(R.layout.fragment_department_add, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        btnAddDepartment = view.findViewById(R.id.btnAddDepartment);
        btnCancel = view.findViewById(R.id.btnCancel);
        etDepartmentname = view.findViewById(R.id.etDepartmentname);
        new ManagerAdminApi(apiUrl);
        btnAddDepartment.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.title_add, coName, getString(R.string.var_department)));
        etDepartmentname.requestFocus();
        return view;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        String buttonText = button.getText().toString();
        if (buttonText.equals(getResources().getString(R.string.cancel))) { // If the cancel button is pressed
            ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
        } else if (buttonText.equals(getResources().getString(R.string.button_add, getString(R.string.var_department)))) {  // If the add department button is pressed
            if (etDepartmentname.getText().toString().isEmpty()) {
                Phoenix.Error(Phoenix.getApplicationContext(), getResources().getString(R.string.required, "department"), false);
            } else {
                Call<String> call = ManagerAdminApi.getInstance().getApi().department(etDepartmentname.getText().toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        String body = response.body();
                        try {
                            assert body != null;
                            JSONObject obj = new JSONObject(body);
                            if (obj.optString("success").equals("false")) {
                                Phoenix.Error(Phoenix.getApplicationContext(), obj.optString("message"), false);
                            } else {
                                etDepartmentname.setText("");
                                Phoenix.Success(Phoenix.getApplicationContext(), getResources().getString(R.string.created1, "Department"),  5);
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
        } else {
            throw new IllegalStateException(getResources().getString(R.string.unexpected) + buttonText);
        }
    }
}