/*
    The Phoenix Hospitality Management System
    Database Manager App
    Grade List Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Grade List Fragment code file
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentDepartmentList extends Fragment {
    private RecyclerView.Adapter dAdapter;
    static InterfaceDataPasser dataPasser;
    public static Boolean update;
    public static Boolean remove;
    private final ArrayList<ObjectDepartment> departmentList = new ArrayList<>();
    private RecyclerView dRecyclerView;

    public FragmentDepartmentList() {
    }

    /**
     * The fragment onCreateView Method
     *
     * @param inflater the layout inflater
     * @param container the container that will hold the fragment
     * @param savedInstanceState the saved instance state
     * @return View object container the fragment view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_list, null, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        String apiUrl = getArguments().getString("ApiUrl");
        update = getArguments().getBoolean("update");
        remove = getArguments().getBoolean("remove");
        new ManagerAdminApi(apiUrl);
        dRecyclerView = view.findViewById(R.id.recyclerViewDepartmentList);
        LinearLayoutManager dLayoutManager = new LinearLayoutManager(this.getActivity());
        dRecyclerView.setHasFixedSize(true);
        dAdapter = new FragmentDepartmentList.DepartmentAdapter(departmentList);
        dRecyclerView.setLayoutManager(dLayoutManager);
        dRecyclerView.setAdapter(dAdapter);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.department_list_title, coName));
        readDepartments();
        return view;
    }

    /**
     * Method to read the Grade list from the database
     */
    public void readDepartments() {
        departmentList.clear();
        Call<String> call = ManagerAdminApi.getInstance().getApi().department();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String body = response.body();
                try {
                    assert body != null;
                    JSONObject obj = new JSONObject(body);
                    if(obj.optString("success").equals("true")) {
                        JSONArray data = obj.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject datum = data.getJSONObject(i);
                            departmentList.add(new ObjectDepartment(
                                    datum.getInt("id"),
                                    datum.getString("department")
                            ));
                        }
                        dAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), obj.optString("message"), Toast.LENGTH_LONG).show();
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

    /**
     * recyclerviewDepartmentList display adapter
     */
    public static class DepartmentAdapter extends RecyclerView.Adapter<FragmentDepartmentList.DepartmentAdapter.DepartmentViewHolder> {
        final private List<ObjectDepartment> mDepartmentList;

        static class DepartmentViewHolder extends RecyclerView.ViewHolder {
            final TextView tvId;
            final TextView tvDepartment;
            DepartmentViewHolder(View departmentView) {
                super(departmentView);
                tvId = departmentView.findViewById(R.id.tvId);
                tvDepartment = departmentView.findViewById(R.id.tvDepartment);
            }
        }
        DepartmentAdapter(ArrayList<ObjectDepartment> departmentlist) {
            mDepartmentList = departmentlist;
        }
        @NonNull
        @Override
        public FragmentDepartmentList.DepartmentAdapter.DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_department_list_item, parent,false);
            return new FragmentDepartmentList.DepartmentAdapter.DepartmentViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final FragmentDepartmentList.DepartmentAdapter.DepartmentViewHolder holder, int position) {
            final ObjectDepartment currentDepartment = mDepartmentList.get(position);
            holder.tvId.setText(String.valueOf(currentDepartment.getId()));
            if (update) {
                holder.tvId.setTextColor(Color.parseColor("#06F522"));
            }
            if (remove) {
                holder.tvId.setTextColor(Color.parseColor("#FF2579"));
            }
            holder.tvDepartment.setText(currentDepartment.getDepartment());
            if (update) {
                holder.tvId.setOnClickListener(v -> dataPasser.onDepartmentUpdate(currentDepartment.getId()));
            }
            if (remove) {
                holder.tvId.setOnClickListener(v -> dataPasser.onDepartmentRemove(currentDepartment.getId()));
            }
        }
        @Override
        public int getItemCount() {
            return mDepartmentList.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (InterfaceDataPasser) context;
    }
}
