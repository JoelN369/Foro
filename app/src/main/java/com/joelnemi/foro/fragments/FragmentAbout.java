package com.joelnemi.foro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.joelnemi.foro.R;
import com.joelnemi.foro.activities.DetalleActivity;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.models.Post;

import java.util.ArrayList;

public class FragmentAbout extends Fragment{
    public static String ARG_FRAGMENT = "com.joelnemi.Foro.TAB";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sobre_mi_profile, container, false);

        return v;

    }


}
