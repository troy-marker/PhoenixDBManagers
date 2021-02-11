/*
    The Phoenix Hospitality Management System
    Database Manager App
    User List Fragment Code File
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

import com.phoenixhosman.phoenixapi.ObjectUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User List Fragment code file
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentUserList extends Fragment {
    private RecyclerView.Adapter uAdapter;
    static InterfaceDataPasser dataPasser;
    public static Boolean update;
    public static Boolean remove;
    private final ArrayList<ObjectUser> userList = new ArrayList<>();

    public FragmentUserList() {
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
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        String apiUrl = getArguments().getString("ApiUrl");
        update = getArguments().getBoolean("update");
        remove = getArguments().getBoolean("remove");
        new ManagerAdminApi(apiUrl);
        RecyclerView uRecyclerView = view.findViewById(R.id.recyclerViewUserList);
        LinearLayoutManager uLayoutManager = new LinearLayoutManager(this.getActivity());
        uRecyclerView.setHasFixedSize(true);
        uAdapter = new UserAdapter(userList);
        uRecyclerView.setLayoutManager(uLayoutManager);
        uRecyclerView.setAdapter(uAdapter);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.title_list, coName,getString(R.string.var_user)));
        readUsers();
        return view;
    }

    /**
     * Method to read the user list from the database
     */
    public void readUsers() {
        userList.clear();
        Call<String> call = ManagerAdminApi.getInstance().getApi().user();
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
                                userList.add(new ObjectUser(
                                        datum.getInt("id"),
                                        datum.getString("username"),
                                        datum.getString("password"),
                                        datum.getString("created"),
                                        datum.getString("modified"),
                                        datum.getInt("grade"),
                                        datum.getString("gradename"),
                                        datum.getInt("department"),
                                        datum.getString("departmentname")
                                ));
                            }
                            uAdapter.notifyDataSetChanged();
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
    public static class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        final private List<ObjectUser> mUserList;

        static class UserViewHolder extends RecyclerView.ViewHolder {
            final TextView tvId;
            final TextView tvUsername;
            final TextView tvCreated;
            final TextView tvGrade;
            final TextView tvDepartment;
            UserViewHolder(View userView) {
                super(userView);
                tvId = userView.findViewById(R.id.tvId);
                tvUsername = userView.findViewById(R.id.tvUsername);
                tvCreated = userView.findViewById(R.id.tvCreated);
                tvGrade = userView.findViewById(R.id.tvGrade);
                tvDepartment = userView.findViewById(R.id.tvDepartment);
            }
        }
        UserAdapter(ArrayList<ObjectUser> userlist) {
            mUserList = userlist;
        }
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_list_item, parent,false);
            return new UserViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
            final ObjectUser currentUser = mUserList.get(position);
            holder.tvId.setText(String.valueOf(currentUser.getId()));
            if (update) {
                holder.tvId.setTextColor(Color.parseColor("#06F522"));
            }
            if (remove) {
                holder.tvId.setTextColor(Color.parseColor("#FF2579"));
            }
            holder.tvUsername.setText(currentUser.getUsername());
            holder.tvCreated.setText(currentUser.getCreated());
            holder.tvGrade.setText(currentUser.getGradeName());
            holder.tvDepartment.setText(currentUser.getDepartmentname());
            if (update) {
                holder.tvId.setOnClickListener(v -> dataPasser.onUserUpdate(currentUser.getId()));
            }
            if (remove) {
                holder.tvId.setOnClickListener(v -> dataPasser.onUserRemove(currentUser.getId()));
            }
        }
        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (InterfaceDataPasser) context;
    }
}
