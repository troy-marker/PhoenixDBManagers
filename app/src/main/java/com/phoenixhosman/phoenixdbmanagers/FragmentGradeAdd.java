/*
    The Phoenix Hospitality Management System
    Database Manager App
    Grade Addition Fragment Code File
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Grade Add Fragment code file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentGradeAdd extends Fragment implements View.OnClickListener {
    String apiUrl;
    Button btnAddGrade;
    Button btnCancel;
    EditText etGradename;

    public FragmentGradeAdd()  {
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
        View view = inflater.inflate(R.layout.fragment_grade_add, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        btnAddGrade = view.findViewById(R.id.btnAddGrade);
        btnCancel = view.findViewById(R.id.btnCancel);
        etGradename = view.findViewById(R.id.etGradename);
        new ManagerAdminApi(apiUrl);
        btnAddGrade.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.grade_add_title, coName));
        etGradename.requestFocus();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCancel:
                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                break;
            case R.id.btnAddGrade:
                if (etGradename.getText().toString().isEmpty()) {
                    ((ActivityMain) Objects.requireNonNull(getActivity())).Error("A grade name is required", false);
                } else {
                    Call<String> call = ManagerAdminApi.getInstance().getApi().grade(etGradename.getText().toString());
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
                                    etGradename.setText("");
                                    ((ActivityMain) Objects.requireNonNull(getActivity())).Success("Grade successfully created");
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
