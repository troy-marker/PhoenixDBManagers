package com.phoenixhosman.phoenixdbmanagers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentInventorySupplier extends Fragment implements View.OnClickListener{
    Button btnCancel;
    static InterfaceDataPasser dataPasser;

    public FragmentInventorySupplier() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_supplier, container, false);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (InterfaceDataPasser) context;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel) {
            dataPasser.onCloseSupplier();
        }
    }
}
