package com.joelnemi.foro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;

import java.util.ArrayList;

public class FragmentAbout extends Fragment{
    public static String ARG_FRAGMENT = "com.joelnemi.Foro.TAB";

    public Usuario usuario;

    private TextView tvPostFama;
    private TextView tvComFama;
    private TextView tvPostVal;
    private TextView tvComVal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sobre_mi_profile, container, false);

        //Compruebo si el bundle es nulo y si contiene extras y las guardo
        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if(extras != null) {
                usuario = (Usuario) extras.getSerializable("usuario");
            }
        } else {
            usuario = (Usuario) savedInstanceState.getSerializable("usuario");
        }


        tvPostFama = v.findViewById(R.id.tvPostFama);
        tvComFama = v.findViewById(R.id.tvComentarioFama);
        tvPostVal = v.findViewById(R.id.tvPostsValorados);
        tvComVal = v.findViewById(R.id.tvComentariosValorados);








        return v;

    }


}
