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
