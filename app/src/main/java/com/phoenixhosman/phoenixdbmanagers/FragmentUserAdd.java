/*
    The Phoenix Hospitality Management System
    Database Manager App
    User Addition Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * User Add Fragment code file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class  FragmentUserAdd extends Fragment implements OnClickListener {

    String apiUrl;
    Button btnAddUser;
    Button btnCancel;
    EditText etUsername;
    EditText etPassword;
    Spinner gradeSpinner;
    Spinner departmentSpinner;
    int gradeIndex;
    int departmentIndex;
    private final ArrayList<String> gradeNames = new ArrayList<>();
    private final ArrayList<String> departmentNames = new ArrayList<>();
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();


    public FragmentUserAdd()  {
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
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        btnAddUser = view.findViewById(R.id.btnAddUser);
        btnCancel = view.findViewById(R.id.btnCancel);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        gradeSpinner = view.findViewById(R.id.gradeSpinner);
        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        btnAddUser.setText(getString(R.string.button_add,getString(R.string.var_user)));
        new ManagerAdminApi(apiUrl);
        gradeIndex = 0;
        departmentIndex = 0;
        btnAddUser.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.title_add, coName, getString(R.string.var_user)));
        readGrades();
        readDepartments();
        return view;
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
                    Toast.makeText(getContext(),getString(R.string.fail_load_list, getString(R.string.var_grade)), Toast.LENGTH_LONG).show();
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
                gradeNames.add(getString(R.string.select, "Grade"));
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
                    Toast.makeText(getContext(),getString(R.string.fail_load_list, "Department"), Toast.LENGTH_LONG).show();
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
                departmentNames.add(getString(R.string.select, "Department"));
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
     * onClick listener
     * @param v the view triggering the action
     */
    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        if (buttonText.equals(getString(R.string.cancel))) {
            ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
        } else if (buttonText.equals(getString(R.string.button_add, getString(R.string.var_user)))) {
            if (etUsername.getText().toString().isEmpty()) {
                Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.required1, getString(R.string.var_username)), false);
            } else {
                if (etPassword.getText().toString().isEmpty()) {
                    Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.required1, getString(R.string.var_password)), false);
                } else {
                    if (gradeIndex == 0) {
                        Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.select, getString(R.string.var_grade)), false);
                    } else {
                        if (departmentIndex == 0) {
                            Phoenix.Error(Phoenix.getApplicationContext(), getString(R.string.select, getString(R.string.var_department)), false);
                        } else {
                            Call<String> call = ManagerAdminApi.getInstance().getApi().user(etUsername.getText().toString(), etPassword.getText().toString(), gradeIndex, departmentIndex);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    String body = response.body();
                                    try {
                                        assert body != null;
                                        JSONObject obj = new JSONObject(body);
                                        if (!obj.optBoolean("success")) {
                                            Phoenix.Error(Phoenix.getApplicationContext(), obj.optString("message"), false);
                                            etUsername.setText("");
                                            etPassword.setText("");
                                            gradeSpinner.setSelection(0);
                                            departmentSpinner.setSelection(0);
                                            etUsername.requestFocus();
                                        } else {
                                            Phoenix.Success(Phoenix.getApplicationContext(), getString(R.string.created1,getString(R.string.var_user)), 5);
                                            ((ActivityMain) Objects.requireNonNull(getActivity())).LoadUserList();
                                            ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
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
        } else {
            throw new IllegalStateException(getString(R.string.unexpected) + v.getId());
        }
    }
}