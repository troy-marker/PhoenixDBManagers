/*
    The Phoenix Hospitality Management System
    Database Manager App
    Inventory List Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phoenixhosman.phoenixapi.ObjectItem;
import com.phoenixhosman.phoenixlib.ActivityPhoenixLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInventoryList extends Fragment {
    private RecyclerView.Adapter iAdapter;
    static InterfaceDataPasser dataPasser;

    private final ArrayList<ObjectItem> itemList = new ArrayList<>();
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();

    public FragmentInventoryList() {
    }

    /**
     * The fragment onCreateView Method
     *
     * @param inflater the layout inflater
     * @param container the container that will hold the fragment
     * @param savedInstanceState the saved instance state
     * @return View object containing the fragment view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_inventory_list, null, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        String apiUrl = getArguments().getString("ApiUrl");
        Integer supplier = getArguments().getInt("supplier");
        Integer category = getArguments().getInt("category");
        Integer subcategory = getArguments().getInt("subcategory");
        new ManagerAdminApi(apiUrl);
        RecyclerView iRecyclerView = view.findViewById(R.id.recyclerViewItemList);
        LinearLayoutManager iLayoutManager = new LinearLayoutManager(this.getActivity());
        iRecyclerView.setHasFixedSize(true);
        iAdapter = new ItemAdapter(itemList);
        iRecyclerView.setLayoutManager(iLayoutManager);
        iRecyclerView.setAdapter(iAdapter);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("Item List");
        readItems(supplier, category, subcategory);
        return view;
    }

    /**
     * Method to read item list
     * @param supplier
     * @param category
     * @param subcategory
     */
    public void readItems(Integer supplier, Integer category, Integer subcategory) {
        itemList.clear();
        Call<String> call = ManagerAdminApi.getInstance().getApi().item(supplier, category, subcategory);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String body = response.body();
                    try {
                        assert body != null;
                        JSONObject obj  = new JSONObject(body);
                        if(obj.optBoolean("success")) {
                            JSONArray data = obj.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++) {
                                JSONObject datum = data.getJSONObject(i);
                                itemList.add(new ObjectItem(
                                    datum.getString("item"),
                                    datum.getInt("supplier"),
                                    datum.getInt("category"),
                                    datum.getInt("subcategory"),
                                    datum.getString("description"),
                                    datum.getInt("casecount"),
                                    datum.getInt("tier1count"),
                                    datum.getDouble("tier1cost"),
                                    datum.getInt("tier2count"),
                                    datum.getDouble("tier2cost"),
                                    datum.getInt("tier3count"),
                                    datum.getDouble("tier3cost"),
                                    datum.getInt("count")
                                ));
                            }
                            iAdapter.notifyDataSetChanged();
                        } else {
                            Phoenix.Error(getContext(), obj.optString("message"), false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Phoenix.Error(getContext(), getString(R.string.failed_loading, getString(R.string.item)), false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

    public class ItemAdapter extends RecyclerView.Adapter<FragmentInventoryList.ItemAdapter.ItemViewHolder> {
        final private List<ObjectItem> mItemList;

        class ItemViewHolder extends RecyclerView.ViewHolder {
            final TextView tvItem;
            final TextView tvDescription;
            ItemViewHolder(View itemView) {
                super(itemView);
                tvItem = itemView.findViewById(R.id.tvItem);
                tvDescription = itemView.findViewById(R.id.tvDescription);
            }
        }
        ItemAdapter(ArrayList<ObjectItem> itemlist) {
            mItemList = itemlist;
        }

        @NonNull
        @Override
        public FragmentInventoryList.ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_inventory_list_item, parent, false);
            return new FragmentInventoryList.ItemAdapter.ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
            final ObjectItem currentItem = mItemList.get(position);
            holder.tvItem.setText(currentItem.getItem());
            holder.tvItem.setTextColor(getResources().getColor(R.color.Phoenix_Lt_Blue, null));
            holder.tvDescription.setText(currentItem.getDescription());
            holder.tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemInfo(currentItem);
                    ItemPrice(currentItem);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (InterfaceDataPasser) context;
    }

    void ItemInfo(ObjectItem cItem) {
        Bundle data = new Bundle();
        data.putString("item", cItem.getItem());
        data.putString("description", cItem.getDescription());
        data.putInt("casecount", cItem.getCase());
        data.putInt("count", cItem.getCount());
        dataPasser.onItemInfo(data);
    }

    void ItemPrice(ObjectItem cItem) {
        Bundle data = new Bundle();
        data.putString("item", cItem.getItem());
        data.putInt("tier1count", cItem.getTier1count());
        data.putDouble("tier1cost", cItem.getTier1cost());
        data.putInt("tier2count", cItem.getTier2count());
        data.putDouble("tier2cost", cItem.getTier2cost());
        data.putInt("tier3count", cItem.getTier3count());
        data.putDouble("tier3cost", cItem.getTier3cost());
        dataPasser.onItemPrice(data);
    }

}
