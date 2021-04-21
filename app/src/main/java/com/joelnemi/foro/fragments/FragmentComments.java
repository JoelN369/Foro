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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.joelnemi.foro.R;
import com.joelnemi.foro.activities.DetalleActivity;
import com.joelnemi.foro.activities.ProfileActivity;
import com.joelnemi.foro.adapters.AdaptadorComentarios;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IComentClickListener;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentComments extends Fragment implements IOnClickPostListener, IComentClickListener {
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


        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot value = task.getResult();

                        Usuario usuario = value.toObject(Usuario.class);
                        ArrayList<Comentario> comentarios = usuario.getComentariosPublicados();
                        AdaptadorComentarios adaptador = new AdaptadorComentarios(getContext(),
                                comentarios, FragmentComments.this);
                        rvListado.setAdapter(adaptador);
                        rvListado.setLayoutManager(new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.VERTICAL, false));
                        rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(),
                                DividerItemDecoration.VERTICAL));

                    }
                });


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
        i.putExtra("post", post);
        startActivity(i);
    }

    @Override
    public void comentarioSeleccionado(Post post) {
        Intent i = new Intent(getActivity(), DetalleActivity.class);
        i.putExtra("post",post);
        startActivity(i);
    }
}
