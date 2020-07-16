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
public class FragmentGradeList extends Fragment {
    private RecyclerView.Adapter gAdapter;
    static InterfaceDataPasser dataPasser;
    public static Boolean update;
    public static Boolean remove;
    private final ArrayList<ObjectGrade> gradeList = new ArrayList<>();
    private RecyclerView gRecyclerView;

    public FragmentGradeList() {
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
        View view = inflater.inflate(R.layout.fragment_grade_list, null, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        String apiUrl = getArguments().getString("ApiUrl");
        update = getArguments().getBoolean("update");
        remove = getArguments().getBoolean("remove");
        new ManagerAdminApi(apiUrl);
        gRecyclerView = view.findViewById(R.id.recyclerViewGradeList);
        LinearLayoutManager gLayoutManager = new LinearLayoutManager(this.getActivity());
        gRecyclerView.setHasFixedSize(true);
        gAdapter = new FragmentGradeList.GradeAdapter(gradeList);
        gRecyclerView.setLayoutManager(gLayoutManager);
        gRecyclerView.setAdapter(gAdapter);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.grade_list_title, coName));
        readGrades();
        return view;
    }

    /**
     * Method to read the Grade list from the database
     */
    public void readGrades() {
        gradeList.clear();
        Call<String> call = ManagerAdminApi.getInstance().getApi().grade();
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
                            gradeList.add(new ObjectGrade(
                                    datum.getInt("id"),
                                    datum.getString("grade")
                            ));
                        }
                        gAdapter.notifyDataSetChanged();
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
     * recyclerviewUserList display adapter
     */
    public static class GradeAdapter extends RecyclerView.Adapter<FragmentGradeList.GradeAdapter.GradeViewHolder> {
        final private List<ObjectGrade> mGradeList;

        static class GradeViewHolder extends RecyclerView.ViewHolder {
            final TextView tvId;
            final TextView tvGrade;
            GradeViewHolder(View gradeView) {
                super(gradeView);
                tvId = gradeView.findViewById(R.id.tvId);
                tvGrade = gradeView.findViewById(R.id.tvGrade);
            }
        }
        GradeAdapter(ArrayList<ObjectGrade> gradelist) {
            mGradeList = gradelist;
        }
        @NonNull
        @Override
        public FragmentGradeList.GradeAdapter.GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_grade_list_item, parent,false);
            return new FragmentGradeList.GradeAdapter.GradeViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final FragmentGradeList.GradeAdapter.GradeViewHolder holder, int position) {
            final ObjectGrade currentGrade = mGradeList.get(position);
            holder.tvId.setText(String.valueOf(currentGrade.getId()));
            if (update) {
                holder.tvId.setTextColor(Color.parseColor("#06F522"));
            }
            if (remove) {
                holder.tvId.setTextColor(Color.parseColor("#FF2579"));
            }
            holder.tvGrade.setText(currentGrade.getGrade());
            if (update) {
                holder.tvId.setOnClickListener(v -> dataPasser.onGradeUpdate(currentGrade.getId()));
            }
            if (remove) {
                holder.tvId.setOnClickListener(v -> dataPasser.onGradeRemove(currentGrade.getId()));
            }
        }
        @Override
        public int getItemCount() {
            return mGradeList.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (InterfaceDataPasser) context;
    }


}
