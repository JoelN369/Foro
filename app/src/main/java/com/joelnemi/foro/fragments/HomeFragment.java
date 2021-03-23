package com.joelnemi.foro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.joelnemi.foro.R;

public class HomeFragment extends Fragment {

    private static HomeFragment fragment;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance() {
        if (fragment == null)
            fragment = new HomeFragment();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}