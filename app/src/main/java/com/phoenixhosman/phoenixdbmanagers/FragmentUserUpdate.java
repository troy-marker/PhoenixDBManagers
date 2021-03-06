/*
    The Phoenix Hospitality Management System
    Database Manager App
    User Upodate Code File
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.phoenixhosman.phoenixapi.ObjectDepartment;
import com.phoenixhosman.phoenixapi.ObjectGrade;
import com.phoenixhosman.phoenixlib.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.R.layout.simple_spinner_item;
import static android.widget.AdapterView.*;


/**
 * User Add Fragment code file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentUserUpdate extends Fragment implements OnClickListener {
    String apiUrl;
    String coName;
    Integer record;
    Button btnUpdateUser;
    Button btnCancel;
    EditText etUsername;
    Spinner gradeSpinner;
    Spinner departmentSpinner;
    int gradeIndex;
    int departmentIndex;
    private final ArrayList<String> gradeNames = new ArrayList<>();
    private final ArrayList<String> departmentNames = new ArrayList<>();
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public FragmentUserUpdate() {
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
        View view = inflater.inflate(R.layout.fragment_user_update, container, false);
        assert getArguments() != null;
        coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        record = getArguments().getInt("record");
        btnUpdateUser = view.findViewById(R.id.btnUpdateUser);
        btnCancel = view.findViewById(R.id.btnCancel);
        etUsername = view.findViewById(R.id.etUsername);
        gradeSpinner = view.findViewById(R.id.gradeSpinner);
        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        new ManagerAdminApi(apiUrl);
        gradeIndex = 0;
        departmentIndex = 0;
        btnUpdateUser.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        etUsername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.title_update, coName, getString(R.string.var_user)));
        readGrades();
        readDepartments();
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
                            gradeIndex = Integer.parseInt(dataObject.getString("grade"));
                            departmentIndex = Integer.parseInt(dataObject.getString("department"));
                            DisplayUser(dataObject.getString("username"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(),getString(R.string.loadfail,getString(R.string.var_user)), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to display user being updated
     * @param username The user's username
     */
    public void DisplayUser(String username) {
        etUsername.setText(username);
        gradeSpinner.setSelection(gradeIndex);
        departmentSpinner.setSelection(departmentIndex);
    }

    /**
     * Method to read the Grade List from the database
     */
    private void readGrades() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().grade();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displayGrades(jsonresponse);
                } else {
                    Toast.makeText(getContext(),getString(R.string.fail_load_list,getString(R.string.var_grade)), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to load the grades into the  grades spinner
     * @param response JSON Object containing the Api response
     */
    private void displayGrades(String response){
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                ArrayList<ObjectGrade> gradeList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectGrade gradeSpinner = new ObjectGrade();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    gradeSpinner.setId(dataObj.getInt("id"));
                    gradeSpinner.setGrade(dataObj.getString("grade"));
                    gradeList.add(gradeSpinner);
                }
                gradeNames.add(getString(R.string.select,getString(R.string.var_grade)));
                for (int i = 0; i < gradeList.size(); i++){
                    gradeNames.add(gradeList.get(i).getGrade());
                }
                ArrayAdapter<String> gradeArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, gradeNames);
                gradeArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                gradeSpinner.setAdapter(gradeArrayAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load the department list from the database
     */
    public void readDepartments() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().department();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displayDepartments(jsonresponse);
                } else {
                    Toast.makeText(getContext(),getString(R.string.fail_load_list,getString(R.string.var_department)), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to load the grades into the  grades spinner
     * @param response JSON Object containing the Api response
     */
    private void displayDepartments(String response){
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                ArrayList<ObjectDepartment> departmentList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectDepartment departmentSpinner = new ObjectDepartment();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    departmentSpinner.setId(dataObj.getInt("id"));
                    departmentSpinner.setDepartment(dataObj.getString("department"));
                    departmentList.add(departmentSpinner);
                }
                departmentNames.add(getString(R.string.select, getString(R.string.var_department)));
                for (int i = 0; i < departmentList.size(); i++){
                    departmentNames.add(departmentList.get(i).getDepartment());
                }
                ArrayAdapter<String> departmentArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, departmentNames);
                departmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                departmentSpinner.setAdapter(departmentArrayAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * The onClick handler
     * @param v the view being clicked
     */
    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        if (buttonText.equals(getString(R.string.cancel))) {
            ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
        } else if (buttonText.equals(getString(R.string.button_update, getString(R.string.var_user)))) {
            if (etUsername.getText().toString().isEmpty()) {
                Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.required1, getString(R.string.var_username)), false);
            } else {
                if (gradeSpinner.getSelectedItemId() == 0) {
                    Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.select, getString(R.string.var_grade)), false);
                } else {
                    if (departmentSpinner.getSelectedItemId() == 0) {
                        Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.select, getString(R.string.var_department)), false);
                    } else {
                        Call<String> call = ManagerAdminApi.getInstance().getApi().ruser(record, etUsername.getText().toString(), (int) gradeSpinner.getSelectedItemId(), (int) departmentSpinner.getSelectedItemId());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                String body = response.body();
                                try {
                                    assert body != null;
                                    JSONObject obj = new JSONObject(body);
                                    if (obj.optString("success").equals("false")) {
                                        Phoenix.Error(Phoenix.getApplicationContext(), obj.optString("message"),false);
                                        ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                                    } else {
                                        Phoenix.Success(Phoenix.getApplicationContext(), obj.optString("message"), 5);
                                        ((ActivityMain) Objects.requireNonNull(getActivity())).LoadUserList();
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
        } else {
            throw new IllegalStateException(getString(R.string.unexpected) + v.getId());
        }
    }
}
