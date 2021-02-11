/*
    The Phoenix Hospitality Management System
    Database Manager App
    Inventory Details Fragment Code File
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
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.phoenixhosman.phoenixlib.ActivityPhoenixLib;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInventoryDetails extends Fragment implements View.OnClickListener {

    String item;
    int supplier;
    int category;
    int subcategory;
    String description;
    int casecount;
    int count;
    EditText etDescription;
    EditText etCaseCount;
    EditText etCount;
    Button btnEditSave;
    Boolean bolEdit;
    ManagerAdminApi managerAdminApi;
    ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public FragmentInventoryDetails() {
    }

    /**
     * The fragment onCreateView Method
     *
     * @param inflater the layout inflater
     * @param container the container that will hold the fragment
     * @param savedInstanceState the saved instance state
     * @return View object containing the fragment view
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_inventory_details, null, false);
        assert getArguments() != null;
        String strApiUrl = getArguments().getString("ApiUrl");
        managerAdminApi = new ManagerAdminApi(strApiUrl);
        supplier = getArguments().getInt("supplier");
        category = getArguments().getInt("category");
        subcategory = getArguments().getInt("subcategory");
        item = getArguments().getString("item");
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etCaseCount = view.findViewById(R.id.etCaseCount);
        etCount = view.findViewById(R.id.etCount);
        btnEditSave = view.findViewById(R.id.btnEditSave1);
        tvTitle.setText(getString(R.string.item_details, item));
        etDescription.setEnabled(false);
        etCaseCount.setEnabled(false);
        etCount.setEnabled(false);
        bolEdit = false;
        btnEditSave.setOnClickListener(this);
        LoadDetails();
        return view;
    }

    /**
     * The classes onClick Listener
     * @param v - the view that causes the event
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (v == btnEditSave) {
            if (!bolEdit)  {
                etDescription.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etCaseCount.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etCount.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etDescription.setEnabled(true);
                etCaseCount.setEnabled(true);
                etCount.setEnabled(true);
                etDescription.requestFocus();
                btnEditSave.setText(R.string.save_details);
                bolEdit = true;
            } else {
                etDescription.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etCaseCount.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etCount.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etDescription.setEnabled(false);
                etCaseCount.setEnabled(false);
                etCount.setEnabled(false);
                description = etDescription.getText().toString();
                casecount = Integer.parseInt(etCaseCount.getText().toString());
                count = Integer.parseInt(etCount.getText().toString());
                SaveDetails();
                btnEditSave.setText(R.string.edit_details);
                bolEdit = false;
            }
        }
    }

    /**
     * This method will save to item details
     */
    public void SaveDetails() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().details(item, supplier, category, subcategory, description, casecount, count);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if(obj.optBoolean("success")) {
                            Phoenix.Success(getContext(), obj.optString("message"), 2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Phoenix.Error(getContext(), "Could not save details", false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * This method will load the items details and display them
     */
    public void LoadDetails() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().detail(item, supplier, category, subcategory);
        call.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if (obj.optBoolean("success")) {
                            JSONArray data = obj.getJSONArray("data");
                            JSONObject datum = data.getJSONObject(0);
                            etDescription.setText(datum.getString("description"));
                            etCaseCount.setText(Integer.toString(datum.getInt("casecount")));
                            etCount.setText(Integer.toString(datum.getInt("count")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }
}
