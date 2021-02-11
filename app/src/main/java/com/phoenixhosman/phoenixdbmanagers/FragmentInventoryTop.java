/*
    The Phoenix Hospitality Management System
    Database Manager App
    Inventory Bottom Fragment Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentInventoryTop extends Fragment {
    private final FragmentBlank blank1 = new FragmentBlank();
    private final FragmentBlank blank2 = new FragmentBlank();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_top, container, false);
        getChildFragmentManager().beginTransaction().add(R.id.topLeftFrame, blank1).commit();
        getChildFragmentManager().beginTransaction().add(R.id.topRightFrame, blank2).commit();

        return view;
    }
}
