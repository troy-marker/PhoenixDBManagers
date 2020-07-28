/*
    The Phoenix Hospitality Management System
    Database Manager App
    User Remove Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * Code for the user remove fragment
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentUserRemove extends Fragment implements View.OnClickListener {
    String apiUrl;
    String coName;
    Integer record;
    TextView txtQuestion;
    TextView tvTitle;
    Button btnYes;
    Button btnNo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_remove, container, false);
        assert getArguments() != null;
        coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        record = getArguments().getInt("record");
        tvTitle = view.findViewById(R.id.tvTitle);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        tvTitle.setText(getString(R.string.user_remove_title, coName));
        txtQuestion = view.findViewById(R.id.txtUserRemoveQuestion);
        readUser();
        return view;
    }

    /**
     * Method to read a user from the database
     */
    private void readUser() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().user(record);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if(obj.optString("success").equals("true")) {
                            JSONObject dataObject = obj.getJSONObject("data");
                            txtQuestion.setText(getString(R.string.UserRemoveQuestion, dataObject.getString("username")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ((ActivityMain) Objects.requireNonNull(getActivity())).Error("Failed loading user information", false);
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
        int id = record;
        switch(v.getId()) {
            case R.id.btnNo:
                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                break;
            case R.id.btnYes:
                if (id == 1) {
                    ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                    ((ActivityMain) Objects.requireNonNull(getActivity())).Error("Cannot remove built in administrator.",false);
                } else {
                    Call<String> call = ManagerAdminApi.getInstance().getApi().duser(id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    JSONObject obj = new JSONObject(response.body());
                                    if (obj.optBoolean("success")) {
                                        ((ActivityMain) Objects.requireNonNull(getActivity())).Error(obj.optString("message"),false);
                                        ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                                    }
                                    if (!obj.optBoolean("success")) {
                                        ((ActivityMain) Objects.requireNonNull(getActivity())).Error("Unable to delete user",false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ((ActivityMain) Objects.requireNonNull(getActivity())).Error("Failed loading user information",false);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        }
                    });
                }
                if(id != 1) {
                    ((ActivityMain) Objects.requireNonNull(getActivity())).LoadUserList();
                }
        }
    }
}
