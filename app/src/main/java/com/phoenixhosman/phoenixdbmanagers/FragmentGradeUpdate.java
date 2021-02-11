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
import com.phoenixhosman.phoenixlib.ActivityPhoenixLib;
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
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

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
        tvTitle.setText(getString(R.string.title_update, coName, getString(R.string.var_grade)));
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
                    Toast.makeText(getContext(),getString(R.string.loadfail, getString(R.string.var_user)), Toast.LENGTH_LONG).show();
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
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        if (buttonText.equals(getString(R.string.cancel))) {
            ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
        } else if (buttonText.equals(getString(R.string.button_update, getString(R.string.var_grade)))) {
            if (etGradename.getText().toString().isEmpty()) {
                Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.required, getString(R.string.var_grade)), false);
            } else {
                Call<String> call = ManagerAdminApi.getInstance().getApi().rgrade(record, etGradename.getText().toString());
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
                                Phoenix.Success(Phoenix.getApplicationContext(), getString(R.string.updated,getString(R.string.var_grade)), 5);
                                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                                ((ActivityMain) Objects.requireNonNull(getActivity())).LoadGradeList();
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
        }
    }
}