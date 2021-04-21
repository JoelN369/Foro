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
import com.google.firebase.firestore.*;
import com.joelnemi.foro.R;
import com.joelnemi.foro.activities.DetalleActivity;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.models.Post;

import java.util.ArrayList;

public class FragmentPosts extends Fragment implements IOnClickPostListener {
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

        //Guardo instancias de Firestore y Auth para conseguir el usuario registrado
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        //Muestro los post que ha subido dicho usuario
        db.collection("Posts").whereEqualTo("user", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                posts = (ArrayList<Post>) value.toObjects(Post.class);
                Log.d("postjoel", value.getDocuments().toString());
                adaptador = new AdaptadorPosts(getContext(), posts, FragmentPosts.this);
                rvListado.setAdapter(adaptador);
                rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));


            }
        });

        return v;

    }

    /**
     * Cuando seleccione un post entrara en el detalle
     * @param post
     */
    @Override
    public void onUpdateSelected(Post post) {
        Intent i = new Intent(getContext(), DetalleActivity.class);
        i.putExtra("post", post);
        startActivity(i);
    }

}
