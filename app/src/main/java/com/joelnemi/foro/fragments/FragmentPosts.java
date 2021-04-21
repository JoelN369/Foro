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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.joelnemi.foro.R;
import com.joelnemi.foro.activities.DetalleActivity;
import com.joelnemi.foro.activities.MainActivity;
import com.joelnemi.foro.adapters.AdaptadorComentarios;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FragmentPosts extends Fragment implements IOnClickPostListener{
    public static String ARG_FRAGMENT = "com.joelnemi.Foro.TAB";


    private RecyclerView rvListado;
    private AdaptadorPosts adaptador;
    private ArrayList<Post> posts;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        rvListado = v.findViewById(R.id.rvItemPosts);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("Posts").whereEqualTo("user",user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                posts = (ArrayList<Post>) value.toObjects(Post.class);
                Log.d("postjoel", value.getDocuments().toString());
                adaptador = new AdaptadorPosts(getContext(), posts,FragmentPosts.this);
                rvListado.setAdapter(adaptador);
                rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));


            }
        });
        /*db.collection("Posts").whereArrayContainsAny("comentarios", Arrays.asList(user.getUid()))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Comentario> posts = (ArrayList<Comentario>) value.toObjects(Comentario.class);
                Log.d("postjoel", value.getDocuments().toString());
                AdaptadorComentarios adaptador = new AdaptadorComentarios(getContext(), posts);
                rvListado.setAdapter(adaptador);
                rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));


            }
        });*/
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        }


    public void descargarDatos() {



    }

        @Override
        public void onUpdateSelected(Post post) {
            Intent i = new Intent(getContext(), DetalleActivity.class);
            i.putExtra("post",post);
            startActivity(i);
        }

}
