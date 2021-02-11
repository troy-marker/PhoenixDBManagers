/*
    The Phoenix Hospitality Management System
    Database Manager App
    Inventory Pricing Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
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

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInventoryPricing extends Fragment implements View.OnClickListener {

    EditText etTier1count;
    EditText etTier1cost;
    EditText etTier2count;
    EditText etTier2cost;
    EditText etTier3count;
    EditText etTier3cost;
    Button btnEditSave2;
    Boolean bolEdit;
    String item;
    int supplier;
    int category;
    int subcategory;
    int tier1count;
    double tier1cost;
    int tier2count;
    double tier2cost;
    int tier3count;
    double tier3cost;
    ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public FragmentInventoryPricing() {
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
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_inventory_pricing, null, false);
        assert getArguments() != null;
        supplier = getArguments().getInt("supplier");
        category = getArguments().getInt("category");
        subcategory = getArguments().getInt("subcategory");
        item = getArguments().getString("item");
        tier1count = getArguments().getInt("tier1count");
        tier1cost = getArguments().getDouble("tier1cost");
        tier2count = getArguments().getInt("tier2count");
        tier2cost = getArguments().getDouble("tier2cost");
        tier3count = getArguments().getInt("tier3count");
        tier3cost = getArguments().getDouble("tier3cost");
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        etTier1count = view.findViewById(R.id.etTier1Count);
        etTier1cost = view.findViewById(R.id.etTier1Cost);
        etTier2count = view.findViewById(R.id.etTier2Count);
        etTier2cost = view.findViewById(R.id.etTier2Cost);
        etTier3count = view.findViewById(R.id.etTier3Count);
        etTier3cost = view.findViewById(R.id.etTier3Cost);
        btnEditSave2 = view.findViewById(R.id.btnEditSave2);
        tvTitle.setText(getString(R.string.item_pricing, item));
        bolEdit = false;
        btnEditSave2.setOnClickListener(this);
        LoadPricing();
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (v == btnEditSave2) {
            if (!bolEdit) {
                etTier1count.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etTier1cost.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etTier2count.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etTier2cost.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etTier3count.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etTier3cost.setBackground(getResources().getDrawable(R.drawable.x_edit_border_selected, null));
                etTier1cost.setEnabled(true);
                etTier1count.setEnabled(true);
                etTier2cost.setEnabled(true);
                etTier2count.setEnabled(true);
                etTier3cost.setEnabled(true);
                etTier3count.setEnabled(true);
                etTier1cost.requestFocus();
                btnEditSave2.setText(R.string.save_pricing);
                bolEdit = true;
            } else {
                etTier1count.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etTier1cost.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etTier2count.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etTier2cost.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etTier3count.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etTier3cost.setBackground(getResources().getDrawable(R.drawable.x_edit_border_default, null));
                etTier1cost.setEnabled(false);
                etTier1count.setEnabled(false);
                etTier2cost.setEnabled(false);
                etTier2count.setEnabled(false);
                etTier3cost.setEnabled(false);
                etTier3count.setEnabled(false);
                SavePricing(Integer.parseInt(etTier1count.getText().toString()),
                            Double.parseDouble(etTier1cost.getText().toString()),
                            Integer.parseInt(etTier2count.getText().toString()),
                            Double.parseDouble(etTier2cost.getText().toString()),
                            Integer.parseInt(etTier3count.getText().toString()),
                            Double.parseDouble(etTier3cost.getText().toString()));
                btnEditSave2.setText(R.string.edit_pricing);
                bolEdit = false;
            }
        }
    }


    /**
     * Method to save item pricing details
     */
    public void SavePricing(int intTier1count, double dblTier1cost, int intTier2count, double dblTier2cost, int intTier3count, double dblTier3cost) {
        Call<String> call = ManagerAdminApi.getInstance().getApi().pricings(item, supplier, category, subcategory, intTier1count, dblTier1cost, intTier2count, dblTier2cost, intTier3count, dblTier3cost);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if (obj.getBoolean("success")) {
                            Phoenix.Success(getContext(), obj.optString("message"), 2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Phoenix.Error(getContext(), getString(R.string.pricing_details), false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to load/display the items pricing details
     */
    public void LoadPricing() {
        Call<String> call = ManagerAdminApi.getInstance().getApi().pricing(item, supplier, category, subcategory);
        call.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject obj = new JSONObject(response.body());
                        if(obj.getBoolean("success")) {
                            JSONArray data = obj.getJSONArray("data");
                            JSONObject datum = data.getJSONObject(0);
                            etTier1count.setText(Integer.toString(datum.getInt("tier1count")));
                            etTier1cost.setText(NumberFormat.getNumberInstance(new Locale("en", "US")).format(datum.getDouble("tier1cost")));
                            etTier2count.setText(Integer.toString(datum.getInt("tier2count")));
                            etTier2cost.setText(NumberFormat.getNumberInstance(new Locale("en", "US")).format(datum.getDouble("tier2cost")));
                            etTier3count.setText(Integer.toString(datum.getInt("tier3count")));
                            etTier3cost.setText(NumberFormat.getNumberInstance(new Locale("en", "US")).format(datum.getDouble("tier3cost")));
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
