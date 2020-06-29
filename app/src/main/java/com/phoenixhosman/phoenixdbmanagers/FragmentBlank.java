/*
    The Phoenix Hospitality Management System
    Database Manager App
    Main Activity Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Code for the blank screen fragment
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentBlank extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        AssetManager assetManager = Objects.requireNonNull(getActivity()).getAssets();
        ImageView imageView =  view.findViewById(R.id.phoenixlogo);
        try {
            InputStream ims = assetManager.open("phoenix.png");
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        } catch (IOException ignored) {

        }
        return view;
    }

}
